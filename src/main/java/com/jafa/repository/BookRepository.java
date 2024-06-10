package com.jafa.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jafa.domain.BookCriteria;
import com.jafa.domain.BookVO;

public interface BookRepository {
	
	List<BookVO> getList(BookCriteria bookCriteria); 
	
	void insert(BookVO vo);
	
	// Integer : 삽입된 행의 개수
	Integer insertSelectKey(BookVO vo);
	
	BookVO read(Long bno);
	
	// int : 삭제된 행의 개수
	int delete(Long bno);
	
	// int : 수정된 행의 개수
	int update(BookVO vo);
	
	// 전체 게시물 수
	int getTotalCount(BookCriteria bookCriteria); 
	
	void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	// 추천 수
	void updateLikeCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	void viewCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	boolean isAvailable(Long bno); // 대출가능 여부

    void updateAvailability(@Param("bno") Long bno, @Param("available") boolean available); // 도서 대출 가능 여부 업데이트
}
