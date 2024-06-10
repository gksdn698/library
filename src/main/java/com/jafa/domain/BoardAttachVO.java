package com.jafa.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardAttachVO {
	private String uuid; 
	private String uploadPath; 
	private String fileName; 
	private boolean fileType;
	private Long bno; 
}