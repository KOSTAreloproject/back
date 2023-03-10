package com.my.relo.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.service.SMSService;

@RestController
@RequestMapping("orders/*")
public class SMSController {

	@Autowired
	private SMSService service;

	@PostMapping("phoneAuth")
	@ResponseBody
	public Boolean phoneAuth(HttpServletRequest request, @RequestParam String tel) {
		System.out.println(tel);
		String code = service.sendRandomMessage(tel);
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60 * 1000 * 5);
		session.setAttribute("rand", code);
		return false;
	}

	@PostMapping("phoneAuthOk")
	@ResponseBody
	public Boolean phoneAuthOk(HttpServletRequest request, @RequestParam String code) {
		HttpSession session = request.getSession();
		String rand = (String) session.getAttribute("rand");
		System.out.println(rand + " : " + code);

		if (rand.equals(code)) {
			session.removeAttribute("rand");
			return false;
		}

		return true;
	}
}
