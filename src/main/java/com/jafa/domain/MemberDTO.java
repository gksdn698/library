package com.jafa.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

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
public class MemberDTO {
	
	private String memberId;
	
	@NotEmpty(message = "이름을 입력하세요.")
	@Pattern(regexp = "^[가-힣|a-zA-Z]{2,5}$" , message = "이름은 2~5글자 사이어야 합니다.")
	private String memberName;
	
	@NotEmpty(message = "이메일을 입력하세요.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;
	
	@NotEmpty(message = "전화번호를 입력하세요.")
	@Pattern(regexp =  "^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$", message="전화번호를 바르게 입력하세요.")
	private String memberPhone; 
	
	private GENDER memberGender;
	enum GENDER {M, F};
	
}