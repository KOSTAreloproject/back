package com.my.relo.service;

import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	JavaMailSender emailSender;

	// public String ePw = createKey();

	private MimeMessage createMessage(String to) throws Exception {
		String ePw = createKey();
		System.out.println("보내는 대상 : " + to);
		System.out.println("인증 번호 : " + ePw);
		MimeMessage message = emailSender.createMimeMessage();

		message.addRecipients(RecipientType.TO, to);// 보내는 대상
		message.setSubject("[RELO] 회원가입을 위한 인증을 완료해주세요.");// 제목

		String msgg = "";
		msgg += "<div align='center'>";
		msgg += "<h3> 안녕하세요 Relo입니다. 저희 서비스 이용을 위한 회원가입 관련 인증메일입니다.</h3>";
		msgg += "<br>";
		msgg += "<p>아래 코드를 인증번호 입력창에 입력해주세요<p>";
		msgg += "<br>";
		msgg += "<br>";
		msgg += "<div align='center' style='font-family:verdana';>";
		msgg += "<h3 style='color:blue;'>회원가입 인증 코드</h3>";
		msgg += "<div style='font-size:130%'>";
		msgg += "CODE : <strong>";
		msgg += ePw + "</strong><div><br/> ";
		msgg += "</div>";

		String code = ePw;
		message.setDescription(code);
		message.setText(msgg, "UTF-8", "HTML");// 내용
		message.setFrom(new InternetAddress("relokosta@gmail.com", "relokosta"));// 보내는 사람
		return message;
	}

	public String createKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();

		for (int i = 0; i < 8; i++) { // 인증코드 8자리
			int index = rnd.nextInt(3); // 0~2 까지 랜덤

			switch (index) {
			case 0:
				key.append((char) ((int) (rnd.nextInt(26)) + 97));
				// a~z (ex. 1+97=98 => (char)98 = 'b')
				break;
			case 1:
				key.append((char) ((int) (rnd.nextInt(26)) + 65));
				// A~Z
				break;
			case 2:
				key.append((rnd.nextInt(10)));
				// 0~9
				break;
			}
		}
		return key.toString();
	}

	@Override
	public String sendSimpleMessage(String to) throws Exception {
		// TODO Auto-generated method stub
		MimeMessage message = createMessage(to);
		try {// 예외처리
			emailSender.send(message);
			String msg = (String) message.getDescription();
			return msg;
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
	}
}