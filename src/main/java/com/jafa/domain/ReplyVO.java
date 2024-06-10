package com.jafa.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReplyVO {
	private Long rno; 
	private Long bno; 
	private String reply; //내용
	private String replyer; // 작성자 
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime replyDate; //등록일 
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private  LocalDateTime updateDate;
	
}
