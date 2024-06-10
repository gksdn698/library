package com.jafa.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jafa.domain.MemberDTO;
import com.jafa.domain.MemberVO;
import com.jafa.exception.NotFoundMemberException;
import com.jafa.exception.PasswordMisMatchException;
import com.jafa.service.MailSendService;
import com.jafa.service.MemberService;
import com.jafa.validation.PasswordMatchingValidator;

@Controller
public class MemberController {

	@Autowired
	PasswordMatchingValidator passwordMatchingValidator;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MailSendService mailSendService;
	
	 //회원페이지
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@GetMapping({"/mypage","/mypage/{path}"})
	public String myPage(Model model 
			,Principal principal,@PathVariable(required = false) String path) {
		String memberId = principal.getName();
		if(path==null) {
			MemberDTO dto = memberService.readDTO(memberId);
			System.out.println(dto);
			model.addAttribute("memberDTO",dto);
			return "member/mypage";
		}
		return "member/"+path;
	}

	// 회원 정보수정 처리
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@PostMapping("/mypage")
	public String modify(@Valid MemberDTO memberDTO,BindingResult bindingResult,RedirectAttributes rttr,Model model) {
		if(bindingResult.hasErrors()) {
			return "member/mypage"; // redirect 로 요청하면 데이터가 끊긴다.
		}else {
			memberService.modifyDTO(memberDTO);
			rttr.addFlashAttribute("memberId", memberDTO.getMemberId());
			model.addAttribute("memberDTO", memberDTO);
			rttr.addFlashAttribute("result","modify");
			return  "redirect:/mypage";
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/adminPage")
	public void adminPage() {
		
	}

	@RequestMapping("/login")
	public String loginPage(HttpServletRequest request, Authentication authentication, RedirectAttributes rttr) {
		String uri = request.getHeader("Referer");
		if(uri!=null && !uri.contains("/login") && !uri.contains("/accessDenied")) {
			request.getSession().setAttribute("prevPage", uri);
		}
		
		if(authentication != null && authentication.isAuthenticated()) { // 이미 로그인 중
			String memberId = authentication.getName();
			request.getSession().setAttribute("memberId", memberId);
			rttr.addFlashAttribute("duplicateLogin","이미 로그인 중 입니다.");
			if(uri==null) uri = (String) request.getSession().getAttribute("prevPage");
			return "redirect:"+uri;
		}
		return "member/login";
	}
	
	@GetMapping("/accessDenied")
	public String accessDenied() {
		return "accessError";
	}
	
	// 비밀번호 변경 처리
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@PostMapping(value = "/mypage/changePwd", produces = "application/text; charset=utf-8") // 한글깨짐 발생
	@ResponseBody
	public ResponseEntity<String> changePwd(@RequestParam Map<String, String> memberMap){
		try {
			memberService.changePassword(memberMap);
		} catch (PasswordMisMatchException e) {
			return new ResponseEntity<String>("비밀번호가 일치하지 않음",HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<String>("success",HttpStatus.OK);
	}
	
	// 아이디 중복 체크
	@PostMapping("/member/idCheck")
	@ResponseBody
	public ResponseEntity<Boolean> idDuplicateCheck(String memberId){
		MemberVO vo = memberService.read(memberId);
		return vo == null ? new ResponseEntity<Boolean>(Boolean.TRUE,HttpStatus.OK)
				: new ResponseEntity<Boolean>(Boolean.FALSE,HttpStatus.OK);
	}
	
	@GetMapping("/mailCheck")
	@ResponseBody
	public String mailCheck(String email) {
		return mailSendService.joinEmail(email);
	}
	
	// 약관동의
	@GetMapping("/join/step1")
	public void step1() {}
	
	// 약관동의 동의하냐 안하냐
	@PostMapping("/join/step2")
	public String step2(@RequestParam(defaultValue = "false") List<Boolean> agreement) {
		if(agreement.size()>=2 && agreement.stream().allMatch(v->v)) {
			return "join/step2";
		}
		return "join/step1";
	}
	
	// 회원가입처리  
	@PostMapping("/member/join")
	public String step3(@Valid MemberVO memberVO, BindingResult bindingResult) {
	    if (bindingResult == null) {
	        bindingResult = new BeanPropertyBindingResult(memberVO, "memberVO");
	    }
	    passwordMatchingValidator.validate(memberVO, bindingResult);
	    
	    if(bindingResult.hasErrors()) {
	        return "/member/join";
	    } else {
	        memberService.join(memberVO);
	        return "redirect:/";	
	    }
	}
	
	@GetMapping({"/join/step2","/member/join"})
	public String joinForm() {
		return "join/step1";
	}
	
	// 아이디 찾기 또는 임시비밀번호 발급 페이지
	@GetMapping("/findMemberInfo")
	public String findMemberInfo() {
		return "member/findMemberInfo";
	}
	
	// 아이디 찾기 메일 전송
	@PostMapping(value = "/findMemberId", produces = "plain/text; charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> findMemberId(String email){
		String message = null;
		try {
			mailSendService.findIdEmail(email);
			message ="가입하신 이메일로 전송되었습니다.";
		} catch (NotFoundMemberException e) {
			message ="회원정보를 찾을수 없습니다.";
			return new ResponseEntity<String>(message,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(message,HttpStatus.OK);
	}
	
	// 임시비밀번호 메일 전송 
	@PostMapping(value = "/findMemberPwd", produces = "plain/text; charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> findMemberPwd(String email){
		String message = null;
		try {
			mailSendService.findPwdEmail(email);			
			message = "가입하신 이메일로 전송되었습니다.";
		} catch (Exception e) {
			message = "회원정보를 찾을 수 없습니다.";
			return new ResponseEntity<String>(message,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(message,HttpStatus.OK);
	}

	// 내가 쓴글 조회
	@GetMapping("/member/article")
    public String myArticles(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId"); // 세션에서 로그인한 사용자 아이디를 가져옴
        	if(memberId ==null) {
        		return "redirect:/login";
        	}
        	else {
        		MemberVO info = memberService.getInfo(memberId);
        		model.addAttribute("info", info);
        		System.out.println(info);
        		return "member/article"; 
        	}
	}
	
	// 내가 쓴 댓글 조회
	@GetMapping("/member/comment")
    public String myComments(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId"); // 세션에서 로그인한 사용자 아이디를 가져옴
        	if(memberId ==null) {
        		return "redirect:/login";
        	}
        	else {
        		MemberVO CommentInfo = memberService.CommentInfo(memberId);
        		model.addAttribute("CommentInfo", CommentInfo);
        		System.out.println(CommentInfo);
        		return "member/comment"; 
        	}
	}
}                                                                                                     
