package com.jafa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jafa.domain.MemberVO;
import com.jafa.repository.MemberRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO vo = memberRepository.read(username);
		if(vo==null) {
			throw new UsernameNotFoundException("회원을 찾을 수 없음 : " + username);
		}
		return new CustomUser(vo);
	}

}
