package com.jafa.repository;

import java.util.List;

import com.jafa.domain.BookAttachVO;

public interface BookAttachRepository {
	
	void insert(BookAttachVO vo);
	
	void delete(String uuid);
	
	List<BookAttachVO> selectByBno(Long bno);
	
	BookAttachVO selectByUuid(String uuid);
	
	void deleteAll(Long bno);
	
	List<BookAttachVO> pastFiles();
}