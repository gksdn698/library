package com.jafa.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jafa.domain.BookAttachVO;
import com.jafa.domain.BookCriteria;
import com.jafa.domain.BookVO;
import com.jafa.domain.LikeDTO;

public interface BookService {

	List<BookVO> getList(BookCriteria bookCriteria); // 목록
	
	void register(BookVO book); //등록

	BookVO get(Long bno); // 조회

	boolean modify(BookVO book); // 수정

	boolean remove(Long bno); // 삭제
	
	//게시물수
	int totalCount(BookCriteria bookCriteria);
	
	List<BookAttachVO> getAttachList(Long bno);

	BookAttachVO getAttach(String uuid);
	
	boolean hitLike(LikeDTO likeDTO);
	
	Boolean isLike(LikeDTO likeDTO);
	
	void viewCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	boolean isAvailable(Long bno); // 대출가능 여부

    void updateAvailability(@Param("bno") Long bno, @Param("available") boolean available); // 도서 대출 가능 여부 업데이트

}
