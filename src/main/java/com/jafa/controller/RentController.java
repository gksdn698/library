package com.jafa.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jafa.domain.RentVO;
import com.jafa.service.RentService;

@Controller
@RequestMapping("/rent")
public class RentController {

	@Autowired
	RentService rentService;
	
    // 대출 요청 처리 로직
	@PostMapping("/request")
	public ResponseEntity<String> requestRent(@RequestParam("bookId") Long bookId, @RequestParam("memberId") String memberId) {
	    boolean success = rentService.requestRent(bookId, memberId);
	    if (success) {
	        return new ResponseEntity<>(HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}
	
   // 중복된 대출 요청 처리
   @GetMapping("/check")
    public ResponseEntity<String> findByBook(@RequestParam("bookId") Long bookId, @RequestParam("memberId") String memberId) {
        int count = rentService.findByBook(bookId, memberId);
        if (count > 0) {
            return new ResponseEntity<>("duplicate", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    // 반납 처리
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam Long rentId) {
        // 반납 처리 로직
        boolean success = rentService.returnBook(rentId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 대출 연장 처리
    @PostMapping("/extend")
    public ResponseEntity<String> extendRent(@RequestParam Long rentId, 
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newReturnDate,
                                             @RequestParam int extensionCount) {
        // 대출 연장 처리 로직
        boolean success = rentService.extendRent(rentId, newReturnDate, extensionCount);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 중복된 연장 요청 처리
    @GetMapping("/extendCheck")
    public ResponseEntity<String> checkExtension(@RequestParam("rentId") Long rentId) {
        RentVO rent = rentService.getRentById(rentId);
        if (rent != null && rent.getExtensionCount() >= 1) {
            return new ResponseEntity<>("duplicate", HttpStatus.OK);
        } else {
            System.out.println(rent);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    // 도서대출 정보 상세
    @GetMapping("/{rentId}")
    public ResponseEntity<RentVO> getRentById(@PathVariable Long rentId) {
        RentVO rent = rentService.getRentById(rentId);
        if (rent != null) {
            return new ResponseEntity<>(rent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 로그인한 회원의 모든 대출 정보
    @GetMapping("/list")
    public String getAllRents(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<RentVO> rents = rentService.getRentsByMemberId(userDetails.getUsername());
        model.addAttribute("rents", rents);
        return "rent/list";
    }

    // 도서대출정보 저장
    @PostMapping("/save")
    public ResponseEntity<Void> saveRent(@RequestBody RentVO rent) {
        rentService.saveRent(rent);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateRent(@RequestBody RentVO rent) {
        rentService.updateRent(rent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 대출정보 삭제
    @DeleteMapping("/{rentId}")
    public ResponseEntity<Void> deleteRent(@PathVariable Long rentId) {
        rentService.deleteRent(rentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
