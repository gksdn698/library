package com.jafa.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jafa.domain.BookVO;
import com.jafa.domain.MemberVO;
import com.jafa.domain.RentVO;
import com.jafa.repository.BookRepository;
import com.jafa.repository.MemberRepository;
import com.jafa.repository.RentRepository;

@Service
public class RentServiceImpl implements RentService {

    @Autowired
    private RentRepository rentRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private BookRepository bookRepository;

    // 대출 ID를 이용하여 대출 정보를 가져오는 메서드
    @Override
    public RentVO getRentById(Long rentId) {
        return rentRepository.getRentById(rentId);
    }

    // 모든 대출 정보를 가져오는 메서드
    @Override
    public List<RentVO> getAllRents() {
        return rentRepository.getAllRents();
    }

    // 로그인한 회원 대출 정보를 가져오는 메서드
    @Override
    public List<RentVO> getRentsByMemberId(String memberId) {
        return rentRepository.getRentsByMemberId(memberId);
    }
    
    // 대출 정보를 저장하는 메서드
    @Override
    public void saveRent(RentVO rent) {
        rentRepository.saveRent(rent);
    }

    // 대출 정보를 수정하는 메서드
    @Override
    public void updateRent(RentVO rent) {
        rentRepository.updateRent(rent);
    }
    // 대출 정보를 삭제하는 메서드
    @Override
    public void deleteRent(Long rentId) {
        rentRepository.deleteRent(rentId);
    }
    
    // 도서대출 
    @Transactional
    @Override
    public boolean requestRent(Long bookId, String memberId) {
    	BookVO book = bookRepository.read(bookId);
        MemberVO member = memberRepository.read(memberId);

        if (book != null && member != null && book.isAvailable()) {
            // 대출 중복 확인
        	int count = rentRepository.findByBook(bookId, memberId);
            if (count > 0) {
                System.out.println("대출 요청 실패: 이미 대출된 책입니다"); // 디버깅 로그 추가
                return false;
            }

            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime returnTime = currentTime.plusDays(14);

            RentVO rent = new RentVO();
            Long rentId = rentRepository.getNextRentId(); // 데이터베이스 시퀀스의 다음 값 가져오기

            rent.setRentId(rentId);
            rent.setBookId(bookId);
            rent.setMemberId(memberId);
            rent.setRentDate(currentTime);
            rent.setReturnDate(returnTime);

            try {
                rentRepository.saveRent(rent);
                bookRepository.updateAvailability(bookId, false); // 도서의 대출 가능 여부를 false로 설정
                System.out.println("대출 요청 성공: " + rent); // 디버깅 로그 추가
                return true;
            } catch (DuplicateKeyException e) {
                // 대출 중복 예외 처리
                System.out.println("대출 요청 실패: 이미 대출된 책입니다"); // 디버깅 로그 추가
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("대출 요청 실패: " + e.getMessage()); // 디버깅 로그 추가
                return false;
            }
        } else {
            System.out.println("대출 요청 실패: 책 또는 회원 정보를 찾을 수 없음"); // 디버깅 로그 추가
            return false;
        }
    }

    // 도서 반납
    @Transactional
    @Override
    public boolean returnBook(Long rentId) {
        try {
        	RentVO rent = rentRepository.getRentById(rentId);
            rentRepository.deleteRent(rentId);
            bookRepository.updateAvailability(rent.getBookId(), true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 도서 연장
    @Override
    public boolean extendRent(Long rentId, LocalDateTime newReturnDate, int extensionCount) {
        try {
            RentVO rent = rentRepository.getRentById(rentId);
            if (rent != null) {
                if (rent.getExtensionCount() >= 1) { // 이미 한 번 연장된 경우
                    return false;
                }
                // 현재 반납일에 14일을 더하여 새로운 반납일을 설정
                LocalDateTime extendedReturnDate = rent.getReturnDate().plusDays(14);
                // rent의 반납일과 연장횟수를 업데이트
                rent.setReturnDate(extendedReturnDate);
                rent.setExtensionCount(extensionCount + 1); // 연장 횟수 증가
                rentRepository.updateRent(rent);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
	@Override
	public Long getNextRentId() {
		return rentRepository.getNextRentId();
	}

	@Override
	public int findByBook(Long bookId, String memberId) {
		 return rentRepository.findByBook(bookId, memberId);
	}

}
