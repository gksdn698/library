package com.jafa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jafa.domain.BookAttachVO;
import com.jafa.domain.BookCriteria;
import com.jafa.domain.BookVO;
import com.jafa.domain.LikeDTO;
import com.jafa.domain.Pagination;
import com.jafa.service.BookService;

@Controller
@RequestMapping("/book")
public class BookController {

	@Autowired
	private BookService bookService;
	
	@GetMapping("/list") 
	public void list(Model model, BookCriteria bookCriteria) {
		model.addAttribute("list", bookService.getList(bookCriteria));
		model.addAttribute("p", new Pagination(bookCriteria, bookService.totalCount(bookCriteria)));
	}
	
	@GetMapping("/get")
	public void get(Long bno, Model model, BookCriteria bookCriteria) {
		model.addAttribute("book", bookService.get(bno));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify")
	public void modify(Long bno, Model model, BookCriteria bookCriteria, Authentication auth) {
		BookVO vo = bookService.get(bno);
		String username = auth.getName(); // 인증된 사용자 계정
		if (!vo.getWriter().equals(username) &&  // 글작성자가 아닌 경우
			!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) { // 관리자가 아닌 경우
				throw new AccessDeniedException("Access denied"); // 접근 금지 
	    }
		model.addAttribute("book", bookService.get(bno));
		model.addAttribute("available", bookService.isAvailable(bno));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/register")
	public void register() {}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/register")
	public String register(BookVO vo, @RequestParam("availability") String availability,RedirectAttributes rttr) {
		boolean isAvailable  = availability.equals("available");
		vo.setAvailable(isAvailable);
		bookService.register(vo);
		rttr.addFlashAttribute("result", vo.getBno()); // ${result}
		rttr.addFlashAttribute("operation", "register");
		return "redirect:/book/list";
	}
	
	@PreAuthorize("isAuthenticated() and principal.username== #vo.writer or hasRole('ROLE_ADMIN')")
	@PostMapping("/modify")
	public String modify(BookVO vo, RedirectAttributes rttr, BookCriteria bookCriteria) {
		if(bookService.modify(vo)) {
			rttr.addFlashAttribute("result", vo.getBno());
			rttr.addFlashAttribute("operation", "modify");
		}
		return "redirect:/book/list"+bookCriteria.getListLink();
	}
	
	@PreAuthorize("isAuthenticated() and principal.username== #writer or hasRole('ROLE_ADMIN')")
	@PostMapping("/remove")
	public String remove(Long bno, RedirectAttributes rttr, BookCriteria bookCriteria, String writer) {
		if(bookService.remove(bno)) {
			rttr.addFlashAttribute("result", bno);
			rttr.addFlashAttribute("operation", "remove");
		}
		return "redirect:/book/list"+bookCriteria.getListLink();
	}
	
	@GetMapping("/getAttachList")
	@ResponseBody
	public ResponseEntity<List<BookAttachVO>> getAttachList(Long bno){
		
		return new ResponseEntity<List<BookAttachVO>>(bookService.getAttachList(bno),HttpStatus.OK);
	}
	
	@GetMapping("/getAttachFileInfo")
	@ResponseBody
	public ResponseEntity<BookAttachVO> getAttach(String uuid){
		return new ResponseEntity<>(bookService.getAttach(uuid),HttpStatus.OK); 
	}
	
	// 게시물 추천
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/like", produces = "plain/text; charset=utf-8")
	public ResponseEntity<String> hitLike(LikeDTO likeDTO){
		String message = likeDTO.getBno() +"번";
		if(bookService.hitLike(likeDTO)) {
			message += "게시글을 추천하였습니다.";
		} else {
			message += "게시글을 추천을 취소 하였습니다.";
		}
		return new ResponseEntity<String>(message,HttpStatus.OK);
	}
	
	@GetMapping(value = "/islike")
	@ResponseBody
	public ResponseEntity<Boolean> isLike(LikeDTO likeDTO){
		return new ResponseEntity<Boolean>(bookService.isLike(likeDTO), HttpStatus.OK);
	}
	
	@GetMapping("/viewCnt/{bno}")
	public ResponseEntity<String> viewCnt(@PathVariable Long bno) {
		bookService.viewCnt(bno, 1); // 1은 증가할 조회수의 양입니다.
		return ResponseEntity.ok("조회수가 증가되었습니다.");
	}
	
	// 도서의 대출 가능 여부
	@GetMapping("/{bno}/available")
    public boolean isBookAvailable(@PathVariable Long bno) {
        return bookService.isAvailable(bno);
    }

	// 도서 대출 가능 여부 업데이트
    @PutMapping("/{bno}/available")
    public void updateBookAvailability(@PathVariable Long bno, @RequestParam boolean available) {
    	bookService.updateAvailability(bno, available);
    }
}
