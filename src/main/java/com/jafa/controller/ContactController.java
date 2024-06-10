package com.jafa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jafa.domain.ContactDTO;

@Controller
public class ContactController {

	@GetMapping("/contact")
	public String contactForm() {
		return "contact";
	}
	
	@PostMapping("/contact")
	public String contactList(ContactDTO contactDTO) {
		System.out.println(contactDTO);
		return "contact";
	}
}
