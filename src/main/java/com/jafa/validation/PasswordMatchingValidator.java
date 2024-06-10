package com.jafa.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jafa.domain.MemberVO;

@Component
public class PasswordMatchingValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return MemberVO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		MemberVO vo = (MemberVO) target;
		 String memberPwd = vo.getMemberPwd();
	    String confirmPwd = vo.getConfirmPwd();
	    
	    if(memberPwd == null) {
	    	memberPwd = "";
	    }
	    
	    if (!memberPwd.equals(confirmPwd)) {
	        errors.rejectValue("confirmPwd", "password.mismatch", "비밀번호와 확인이 일치하지 않습니다.");    
	    }

	}

	
}
