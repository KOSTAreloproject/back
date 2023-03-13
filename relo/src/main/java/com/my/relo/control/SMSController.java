package com.my.relo.control;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.service.SMSService;

@RestController
@RequestMapping("member/*")
public class SMSController {

	@Autowired
	private SMSService service;

	@PostMapping("phoneAuth")
	@ResponseBody
	public ResponseEntity<?> phoneAuth(HttpSession session, @RequestBody Map<String, String> param) {
		String tel = param.get("tel");
		System.out.println("전화번호: "+tel);
		String code = service.sendRandomMessage(tel);
		session.setMaxInactiveInterval(60 * 1000 * 3);
		session.setAttribute("rand", code);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("phoneAuthOk")
	@ResponseBody
	public ResponseEntity<?> phoneAuthOk(HttpSession session, @RequestBody Map<String, String> param) {
		String code = param.get("code");
		String rand = (String) session.getAttribute("rand");
		System.out.println(rand + " : " + code);

		if (rand.equals(code)) {
			session.removeAttribute("rand");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}