package com.jafa.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jafa.domain.RentVO;

public interface RentService {
    RentVO getRentById(Long rentId);
    List<RentVO> getAllRents();
    List<RentVO> getRentsByMemberId(String memberId);
    void saveRent(RentVO rent);
    void updateRent(RentVO rent);
    void deleteRent(Long rentId);
    
    boolean requestRent(Long bookId, String memberId);
    boolean returnBook(Long rentId);
    boolean extendRent(@Param("rentId") Long rentId, @Param("newReturnDate") LocalDateTime newReturnDate, @Param("extensionCount") int extensionCount);
    
    Long getNextRentId();
    int findByBook(@Param("bookId") Long bookId, @Param("memberId") String memberId);
//    int ExtendByBook(@Param("bookId") Long bookId, @Param("memberId") String memberId);
    
}
