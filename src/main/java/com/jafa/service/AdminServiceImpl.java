package com.jafa.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jafa.domain.MemberVO;
import com.jafa.domain.RentVO;
import com.jafa.repository.MemberRepository;
import com.jafa.repository.RentRepository;

@Service
public class AdminServiceImpl {

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	RentRepository rentRepository;
	
	public List<MemberVO> getAllUsers() { // 모든 멤버 조회
        return memberRepository.findAll();
    }

    public void deleteMember(String memberId) { // 멤버 삭제
    	memberRepository.deleteAuthByMemberId(memberId); // 자식 삭제
    	memberRepository.deleteRentByMemberId(memberId); // 자식 삭제
    	memberRepository.deleteById(memberId); // 부모 삭제
    }
    
    public List<RentVO> getAllRents() { // 모든 대출 내역 조회
        return rentRepository.getAllRents(); 
    }
    
    public List<RentVO> searchRents(String memberId, LocalDate startDate, LocalDate endDate) {
        return rentRepository.searchRents(memberId, startDate, endDate);
    }

    public void deleteRent(Long rentId) { // 대출 도서 삭제
    	rentRepository.deleteRent(rentId);
    }
}
