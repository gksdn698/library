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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jafa.domain.BoardAttachVO;
import com.jafa.domain.BoardVO;
import com.jafa.domain.Criteria;
import com.jafa.domain.LikeDTO;
import com.jafa.domain.Pagination;
import com.jafa.service.BoardService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/board")
@Log4j
public class BoardController {
	
	@Autowired
	private BoardService boardService; 
	
	@GetMapping("/list") 
	public void list(Model model, Criteria criteria) {
		model.addAttribute("list", boardService.getList(criteria));
		model.addAttribute("p", new Pagination(criteria, boardService.totalCount(criteria)));
	}
	
	@GetMapping("/get")
	public void get(Long bno, Model model, Criteria criteria) {
		model.addAttribute("board", boardService.get(bno));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify")
	public void modify(Long bno, Model model, Criteria criteria, Authentication auth) {
		BoardVO vo = boardService.get(bno);
		String username = auth.getName(); // 인증된 사용자 계정
		if (!vo.getWriter().equals(username) &&  // 글작성자가 아닌 경우
			!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) { // 관리자가 아닌 경우
				throw new AccessDeniedException("Access denied"); // 접근 금지 
	    }
		model.addAttribute("board", boardService.get(bno));
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/register")
	public void register() {}
	
	@PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN') or principal.username == #vo.writer)")
	@PostMapping("/register")
	public String register(BoardVO vo, RedirectAttributes rttr, Authentication auth) {
		// 관리자인 경우 공지사항 여부를 설정
		if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			// 체크박스가 선택되었을 때 공지사항으로 설정
	        if (vo.isNotice()) {
	            vo.setNotice(true);
	        } else {
	            vo.setNotice(false);
	        }
		}
		boardService.register(vo);
		rttr.addFlashAttribute("result", vo.getBno()); // ${result}
		rttr.addFlashAttribute("operation", "register");
		return "redirect:/board/list";
	}

	@PreAuthorize("isAuthenticated() and principal.username== #vo.writer or hasRole('ROLE_ADMIN')")
	@PostMapping("/modify")
	public String modify(BoardVO vo, RedirectAttributes rttr, Criteria criteria) {
		if(boardService.modify(vo)) {
			rttr.addFlashAttribute("result", vo.getBno());
			rttr.addFlashAttribute("operation", "modify");
		}
		return "redirect:/board/list"+criteria.getListLink();
	}
	
	@PreAuthorize("isAuthenticated() and principal.username== #writer or hasRole('ROLE_ADMIN')")
	@PostMapping("/remove")
	public String remove(Long bno, RedirectAttributes rttr, Criteria criteria, String writer) {
		log.info(writer);
		if(boardService.remove(bno)) {
			rttr.addFlashAttribute("result", bno);
			rttr.addFlashAttribute("operation", "remove");
		}
		return "redirect:/board/list"+criteria.getListLink();
	}
	
	@GetMapping("/getAttachList")
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		
		return new ResponseEntity<List<BoardAttachVO>>(boardService.getAttachList(bno),HttpStatus.OK);
	}
	
	@GetMapping("/getAttachFileInfo")
	@ResponseBody
	public ResponseEntity<BoardAttachVO> getAttach(String uuid){
		return new ResponseEntity<>(boardService.getAttach(uuid),HttpStatus.OK); 
	}
	
	// 게시물 추천
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/like", produces = "plain/text; charset=utf-8")
	public ResponseEntity<String> hitLike(LikeDTO likeDTO){
		log.info(likeDTO.getMemberId());
		log.info(likeDTO.getBno());
		String message = likeDTO.getBno() +"번";
		if(boardService.hitLike(likeDTO)) {
			message += "게시글을 추천하였습니다.";
		} else {
			message += "게시글을 추천을 취소 하였습니다.";
		}
		return new ResponseEntity<String>(message,HttpStatus.OK);
	}
	
	@GetMapping(value = "/islike")
	@ResponseBody
	public ResponseEntity<Boolean> isLike(LikeDTO likeDTO){
		return new ResponseEntity<Boolean>(boardService.isLike(likeDTO), HttpStatus.OK);
	}
	
	@GetMapping("/viewCnt/{bno}")
	public ResponseEntity<String> viewCnt(@PathVariable Long bno) {
		boardService.viewCnt(bno, 1); // 1은 증가할 조회수의 양입니다.
		return ResponseEntity.ok("조회수가 증가되었습니다.");
	}
}
