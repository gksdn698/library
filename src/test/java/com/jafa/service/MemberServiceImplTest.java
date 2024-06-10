package com.jafa.service;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jafa.AppTest;
import com.jafa.domain.AuthVO;
import com.jafa.domain.MemberVO;
import com.jafa.repository.AuthRepository;

public class MemberServiceImplTest extends AppTest{

	@Autowired
	MemberService memberService;
	
	@Autowired
	AuthRepository authRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	public void test() {
		MemberVO vo = new MemberVO();
		vo.setMemberId("admin");
		vo.setMemberPwd("1234");
		vo.setMemberName("관리자");
		vo.setEmail("admin@test.com");
		vo.setMemberPhone("010-6223-6896");
		vo.setConfirmPwd("1234");
//		vo.setMemberGender("M");
		memberService.join(vo);
		
		AuthVO authVO = new AuthVO("admin","ROLE_ADMIN");
		authRepository.insert(authVO);
	}

	@Test
	public void test2() {
		AuthVO vo = new AuthVO("admin","ROLE_ADMIN");
		authRepository.insert(vo);
	}
	
	@Test
	@Ignore
	public void test3() {
		MemberVO vo = new MemberVO();
		vo.setMemberId("gksdn698");
		vo.setMemberPwd("1234");
		vo.setMemberName("한우");
		vo.setEmail("gksdn@test.com");
		memberService.join(vo);
	}
}
