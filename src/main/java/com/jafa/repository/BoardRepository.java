package com.jafa.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jafa.domain.BoardVO;
import com.jafa.domain.Criteria;

public interface BoardRepository {
	
	List<BoardVO> getList(Criteria criteria); //원래 있던거
	
	void insert(BoardVO vo);
	
	// Integer : 삽입된 행의 개수
	Integer insertSelectKey(BoardVO vo);
	
	BoardVO read(Long bno);
	
	// int : 삭제된 행의 개수
	int delete(Long bno); //원래 있던거
	
	// int : 수정된 행의 개수
	int update(BoardVO vo);
	
	// 전체 게시물 수
	int getTotalCount(Criteria criteria); 
	
	void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	// 추천 수
	void updateLikeCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	List<BoardVO> getBoardsByWriter(String writer);
	
	void viewCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
}
