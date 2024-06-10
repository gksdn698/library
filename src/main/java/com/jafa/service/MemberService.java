
package com.jafa.service;

import java.util.Map;

import com.jafa.domain.MemberDTO;
import com.jafa.domain.MemberVO;

public interface MemberService {
	
	void join(MemberVO vo);
	
	void modify(MemberVO vo);
	
	void modifyDTO(MemberDTO memberDTO);
	
	MemberVO read(String memberId);
	
	MemberDTO readDTO(String memberId);
	
	void changePassword(Map<String, String> memberMap);
	
	MemberVO getInfo(String memberId); // 내가 쓴 글
	
	MemberVO CommentInfo(String memberId); // 내가 쓴 댓글
	
}
