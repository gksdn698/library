package com.jafa.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

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
public class RentVO {
    
    private Long rentId; // 대출 ID
    private Long bookId; // 책 ID
    private String memberId; // 회원 ID
    
    @DateTimeFormat(pattern = "yyyy년MM월dd일 HH시mm분")
    private LocalDateTime rentDate; // 대출일
    @DateTimeFormat(pattern = "yyyy년MM월dd일 HH시mm분")
    private LocalDateTime returnDate; // 반납일
    private int extensionCount; // 대출 연장 확인 코드
    
    // 대출일 설정
    public void setRentDate(LocalDateTime rentDate) {
        this.rentDate = rentDate;
    }
    
    // 반납일 설정
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
}
