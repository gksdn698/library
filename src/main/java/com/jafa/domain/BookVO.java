package com.jafa.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookVO {

	private Long bno; // 번호
	private String title; // 제목
	private String content; // 내용
	private String writer; // 저자
	private String publisher; // 출판사
	private boolean available; // 대출 가능 여부
	private String category; // 도서 분류 정보
	private int likeHit;
	private int viewCnt;	
	
	@DateTimeFormat(pattern = "yyyy년MM월dd일 HH시mm분")
	private LocalDateTime regDate; // 작성일
	
	@DateTimeFormat(pattern = "yyyy년MM월dd일 HH시mm분")
	private LocalDateTime updateDate; // 수정일
	
	private int replyCnt;
	
	private List<BookAttachVO> attachList; // 파일리스트
	
	 // 대출 상태에 따라 적절한 문자열 반환
    public String getLoanStatus() {
        return available ? "대출 가능" : "대출중";
    }
}
