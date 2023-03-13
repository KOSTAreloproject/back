package com.my.relo.control;

import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.MemberDTO;
import com.my.relo.exception.FindException;
import com.my.relo.service.EmailServiceImpl;
import com.my.relo.service.MemberService;

@RestController
@RequestMapping("member/*")
public class MailController {

	@Autowired
	private EmailServiceImpl emailService;

	@Autowired
	private MemberService ms;

	@PostMapping("findpwd/emailconfig")
	public void emailConfirm(String email, String tel) throws Exception {

		String confirm = emailService.sendSimpleMessage(email);

		Long mNum = emailService.findMNum(tel);
		MemberDTO dto = MemberDTO.builder().pwd(confirm).build();

		ms.updateProfile(mNum, dto);
	}

	@PostMapping("share")
	public ResponseEntity<?> shareEmail(HttpSession session, @RequestBody Map<String, Object> map)
			throws FindException {

		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인을 하세요.");
		}
		try {
			map.put("mnum", mNum);
			emailService.shareMessage(map);
			return new ResponseEntity<>("전송!", HttpStatus.OK);
		} catch (MessagingException e) {
			e.printStackTrace();
			return new ResponseEntity<>("전송실패", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("pay-request")
	public ResponseEntity<?> payRequest(HttpSession session, @RequestBody Map<String, Object> map)
			throws FindException {

		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인을 하세요.");
		}
		try {
			emailService.payRequestMessage(map);
			return new ResponseEntity<>("전송!", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("전송실패", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("pay-confirm")
	public ResponseEntity<?> payConfirm(HttpSession session, @RequestBody Map<String, Object> map)
			throws FindException {

		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인을 하세요.");
		}
		try {
			emailService.payConfirmMessage(map);
			return new ResponseEntity<>("전송!", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("전송실패", HttpStatus.BAD_REQUEST);
		}
	}
}