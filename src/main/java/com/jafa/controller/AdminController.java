package com.jafa.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jafa.domain.MemberVO;
import com.jafa.domain.RentVO;
import com.jafa.service.AdminServiceImpl;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminServiceImpl adminService;
	
	// 관리자페이지
	@PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
	@GetMapping("/admin")
    public String adminPage(Model model) {
        return "admin/adminPage";
    }
	
	// 모든 회원 조회
	@PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @GetMapping("/members")
    public String getAllUsers(Model model) {
        List<MemberVO> members = adminService.getAllUsers();
        model.addAttribute("members", members);
        return "admin/memberList";
    }

    // 회원 삭제
	@PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @PostMapping("/members/delete")
    public String deleteMember(@RequestParam("memberId") String memberId) {
        adminService.deleteMember(memberId);
        return "redirect:/admin/members";
    }

    // 모든 대출 내역 조회
	@PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @GetMapping("/rents")
    public String getAllRents(
            @RequestParam(value = "memberId", required = false) String memberId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        List<RentVO> rents = adminService.searchRents(memberId, startDate, endDate);
        model.addAttribute("rents", rents);
        return "admin/rentList";
    }

    // 대출 내역 삭제
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @PostMapping("/rents/delete")
    public String deleteRent(@RequestParam("rentId") Long rentId) {
        adminService.deleteRent(rentId);
        return "redirect:/admin/rents";
    }
}
