package com.jafa.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ContactDTO {
	List<ContactVO> contactList; 
}
