package com.jafa.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class MemberVO {
	
	private String memberId;
	
	@NotEmpty(message = "비밀번호를 입력하세요")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$" , message = "비밀번호는 숫자, 문자, 특수문자를 포함한 8~15자리 이내여야합니다." )
	private String memberPwd;
	private String confirmPwd;
	
	@NotEmpty(message = "이름은 반드시 입력하셔야 합니다.")
	@Pattern(regexp = "^[가-힣|a-zA-Z]{2,5}$" , message = "이름은 2~5글자 사이어야 합니다.")
	private String memberName;
	
	@NotEmpty(message = "이메일은 반드시 입력하셔야 합니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;
	
	@NotEmpty(message = "전화번호를 입력하세요.")
	@Pattern(regexp =  "^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$", message="전화번호를 바르게 입력하세요.")
	private String memberPhone; 
	
	private boolean enabled;  
	
	private LocalDateTime regDate; 
	private LocalDateTime updateDate;

	@NotNull(message = "성별을 선택하세요.")
	private GENDER memberGender;
	
	enum GENDER {M, F};
	
	private List<AuthVO> authList;
	
	private List<BoardVO> userBoardList;
	
	private List<ReplyVO> userReplyList; 
}