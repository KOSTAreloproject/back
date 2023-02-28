package com.my.relo.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.service.EmailService;

@RestController
@RequestMapping("member/*")
public class MailController {

	@Autowired
	private EmailService emailService;

	// 생략

	@PostMapping("join/emailconfig")
	public void emailConfirm(HttpServletRequest request, @RequestParam String email) throws Exception {

		String confirm = emailService.sendSimpleMessage(email);
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60 * 1000 * 5);
		session.setAttribute("code", confirm);
	}

	@GetMapping("join/checkcode")
	public ResponseEntity<?> codeConfirm(HttpServletRequest request, @RequestParam String code) throws Exception {
		HttpSession session = request.getSession();
		String mailcode = (String) session.getAttribute("code");
		if (mailcode.equals(code)) {
			session.invalidate();
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}