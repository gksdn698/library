package com.jafa.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jafa.domain.AuthVO;
import com.jafa.domain.BoardVO;
import com.jafa.domain.MemberDTO;
import com.jafa.domain.MemberVO;
import com.jafa.domain.ReplyVO;
import com.jafa.exception.PasswordMisMatchException;
import com.jafa.repository.AuthRepository;
import com.jafa.repository.MemberRepository;

import lombok.extern.log4j.Log4j;


@Service
@Log4j
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private AuthRepository authRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private ReplyService replyService;
	
	@Transactional
	@Override
	public void join(MemberVO vo) {
		AuthVO authVO = new AuthVO(vo.getMemberId(),"ROLE_MEMBER"); // 이부분 으로 관리자 등록 가능
		vo.setMemberPwd(passwordEncoder.encode(vo.getMemberPwd()));
		memberRepository.insert(vo);
		authRepository.insert(authVO);
	}

	@Override
	public void modify(MemberVO vo) {
		memberRepository.update(vo);
	}

	@Override
	public MemberVO read(String memberId) {
		return memberRepository.selectById(memberId);
	}

	@Transactional
	@Override
	public void changePassword(Map<String, String> memberMap) {
		String memberId = memberMap.get("memberId");
		String newPwd = memberMap.get("newPwd");
		String currentPwd = memberMap.get("currentPwd");
		MemberVO vo = memberRepository.selectById(memberId);
		log.info(vo);
		if(!passwordEncoder.matches(currentPwd, vo.getMemberPwd())) {
			throw new PasswordMisMatchException(); 
		}
		memberRepository.updatePassword(memberId, passwordEncoder.encode(newPwd));
	}

	@Override
	public void modifyDTO(MemberDTO memberDTO) {
		memberRepository.updateDTO(memberDTO);
	}

	@Override
	public MemberDTO readDTO(String memberId) {
		return memberRepository.selectByIdDTO(memberId);
	}

	@Override
	public MemberVO getInfo(String memberId) {
	    MemberVO memberInfo = memberRepository.getInfo(memberId); // 회원 정보를 조회합니다.
	    List<BoardVO> userBoardList = boardService.getBoardsByWriter(memberId); // 회원이 작성한 게시글 목록을 조회합니다.
	    memberInfo.setUserBoardList(userBoardList); // 조회한 게시글 목록을 MemberVO 객체의 userBoardList 필드에 설정합니다.
	    return memberInfo;
	}

	@Override
	public MemberVO CommentInfo(String memberId) {
		MemberVO commentInfo = memberRepository.CommentInfo(memberId);
		List<ReplyVO> replyer = replyService.getReplyByReplyer(memberId); // 회원이 작성한 댓글 목록을 조회
		commentInfo.setUserReplyList(replyer); // 조회한 댓글 목록을 MemberVO 객체의 userReplyList 필드에 설정
		return commentInfo;
	}
}
