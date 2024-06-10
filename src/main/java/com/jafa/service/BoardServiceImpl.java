package com.jafa.service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jafa.domain.BoardAttachVO;
import com.jafa.domain.BoardVO;
import com.jafa.domain.Criteria;
import com.jafa.domain.LikeDTO;
import com.jafa.repository.ArticleLikeRepository;
import com.jafa.repository.BoardAttachRepository;
import com.jafa.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

//@Component
//@Repository
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	
	@Autowired
	private BoardAttachRepository boardAttachRepository;

	@Autowired
	private ArticleLikeRepository articleLikeRepository;
	
	@Override
	public List<BoardVO> getList(Criteria criteria) {
		List<BoardVO> list = boardRepository.getList(criteria);
        // 각 BookVO 객체에 attachList를 설정하는 작업 수행
        for (BoardVO board : list) {
            board.setAttachList(boardAttachRepository.selectByBno(board.getBno()));
        }
        return list;	
	}

	@Transactional
	@Override
	public void register(BoardVO board) {
		boardRepository.insertSelectKey(board);
		List<BoardAttachVO> attachList = board.getAttachList();
		if(attachList!=null && !attachList.isEmpty()) { // 첨부파일 있다면
			attachList.forEach(attachFile -> {attachFile.setBno(board.getBno());
			boardAttachRepository.insert(attachFile);
			});
		}
	}

	@Override
	public BoardVO get(Long bno) {
		return boardRepository.read(bno);
	}

	@Transactional
	@Override
	public boolean modify(BoardVO board) {
		List<BoardAttachVO> attachList = board.getAttachList();
		
		if(attachList!=null && !attachList.isEmpty()) { // 첨부파일 삭제 또는 추가
			List<BoardAttachVO> delList = attachList.stream()
				.filter(attach ->attach.getBno()!=null)
				.collect(Collectors.toList());
			// 파일 삭제
			deleteFiles(delList);
			
			// 데이터베이스 기록삭제
			delList.forEach(vo->{
				boardAttachRepository.delete(vo.getUuid());
			});
			
			// 새로운 파일 추가
			attachList.stream().filter(attach ->attach.getBno()==null).forEach(vo->{
				vo.setBno(board.getBno());
				boardAttachRepository.insert(vo);
			});
		}
		
		return boardRepository.update(board)==1;
	}

	@Override
	public boolean remove(Long bno) {
		List<BoardAttachVO> attachList = getAttachList(bno);
		if(attachList!=null && !attachList.isEmpty()) {
			deleteFiles(attachList);
			boardAttachRepository.deleteAll(bno);
		}
		
		return boardRepository.delete(bno)==1;
	}

	@Override
	public int totalCount(Criteria criteria) {
		return boardRepository.getTotalCount(criteria);
	}

	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		return boardAttachRepository.selectByBno(bno);
	}

	@Override
	public BoardAttachVO getAttach(String uuid) {
		return boardAttachRepository.selectByUuid(uuid);
	}
	
	private void deleteFiles(List<BoardAttachVO> delList) {
		delList.forEach(vo->{
			File file = new File("C:/storage/board/"+vo.getUploadPath(),vo.getUuid() + "_" + vo.getFileName());
			file.delete();
			if(vo.isFileType()) {
				file = new File("C:/storage/board/"+vo.getUploadPath(),"s_"+vo.getUuid() + "_" + vo.getFileName());
				file.delete();
			}
		});
	}
	
	// 게시물 추천 기능
	@Transactional
	@Override
	public boolean hitLike(LikeDTO likeDTO) {
		LikeDTO result = articleLikeRepository.get(likeDTO);
		if(result==null) { // 추천 
			articleLikeRepository.insert(likeDTO);
			boardRepository.updateLikeCnt(likeDTO.getBno(), 1);
			return true; 
		} else { // 추천 취소 
			articleLikeRepository.delete(likeDTO);
			boardRepository.updateLikeCnt(likeDTO.getBno(), -1);
			return false;
		}	
	}

	@Override
	public Boolean isLike(LikeDTO likeDTO) {
		return articleLikeRepository.get(likeDTO) != null ;
	}

	@Override
	public List<BoardVO> getBoardsByWriter(String writer) {
	    return boardRepository.getBoardsByWriter(writer);
	}

	@Override
	public void viewCnt(Long bno, int amount) {
		boardRepository.viewCnt(bno, amount);
	}

}
