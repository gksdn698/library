package com.jafa.repository;

import java.util.List;

import com.jafa.domain.BoardAttachVO;

public interface BoardAttachRepository {
	
	void insert(BoardAttachVO vo);
	
	void delete(String uuid);
	
	List<BoardAttachVO> selectByBno(Long bno);
	
	BoardAttachVO selectByUuid(String uuid);
	
	void deleteAll(Long bno);
	
	List<BoardAttachVO> pastFiles();
}