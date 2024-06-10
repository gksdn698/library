package com.jafa.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthVO implements Serializable{
	
	private static final long serialVersionUID = 8711092397905686008L;
	
	private String memberId; 
	private String auth; 
	
}