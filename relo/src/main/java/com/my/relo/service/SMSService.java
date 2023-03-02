package com.my.relo.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.my.relo.util.SMSConfig;

@Service
public class SMSService {
	public String sendRandomMessage(String tel) {
		SMSConfig message = new SMSConfig();
		Random rand = new Random();
		String numStr = "";
		for (int i = 0; i < 6; i++) {
			String ran = Integer.toString(rand.nextInt(10));
			numStr += ran;
		}
		System.out.println("회원가입 문자 인증 => " + numStr);

		message.send_msg(tel, numStr);

		return numStr;
	}
}
