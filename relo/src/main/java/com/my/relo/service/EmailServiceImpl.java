package com.my.relo.service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.my.relo.entity.Member;
import com.my.relo.repository.MemberRepository;

@Service
public class EmailServiceImpl implements EmailService {

	final String[] arr = { "!", "@", "#", "$", "%", "/", "^", "&", "+", "=" };

	@Autowired
	JavaMailSender emailSender;

	@Autowired
	private MemberRepository mr;

	public Long findMNum(String tel) {
		Member m = mr.findIdAndPwd(tel);
		return m.getMNum();
	}

	private MimeMessage createMessage(String to) throws Exception {
		String ePw = createKey();
		System.out.println("보내는 대상 : " + to);
		System.out.println("인증 번호 : " + ePw);
		MimeMessage message = emailSender.createMimeMessage();

		message.addRecipients(RecipientType.TO, to);// 보내는 대상
		message.setSubject("[RELO] 비밀번호 재설정 안내입니다.");// 제목

		String msgg = "";
		msgg += "<div align='center'>";
		msgg += "<h3> 안녕하세요 RELO입니다.<br/> 저희 서비스 이용을 위한 로그인 관련 메일입니다.</h3>";
		msgg += "<br>";
		msgg += "<p>RELO 회원님의 임시비밀번호 입니다.<p>";
		msgg += "<br>";
		msgg += "<br>";
		msgg += "<div align='center' style='font-family:verdana';>";
		msgg += "<h3 style='color:blue;'>임시 비밀번호</h3>";
		msgg += "<div style='font-size:130%'>";
		msgg += "CODE : <strong>";
		msgg += ePw + "</strong><div><br/> ";
		msgg += "</div>";
		msgg += "<p style='font-size:10px'> *본 메일은 발신 전용입니다. <br/>더 궁금하신 내용은 고객센터로 문의주시면 신속하게 답변드리겠습니다.</p>";
		msgg += "<p style='font-size:10px'> *고객센터 : 1234-5678(평일 11:00 - 18:00)</p>";

		String code = ePw;
		message.setDescription(code);
		message.setText(msgg, "UTF-8", "HTML");// 내용
		message.setFrom(new InternetAddress("relokosta@gmail.com", "RELO"));// 보내는 사람
		return message;
	}

	public String createKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();
		boolean[] visited = new boolean[4];

		for (int i = 0; i < 8; i++) { // 인증코드 8자리
			int idx = rnd.nextInt(arr.length);
			int index = rnd.nextInt(4); // 0~3 까지 랜덤

			switch (index) {
			case 0:
				key.append((char) ((int) (rnd.nextInt(26)) + 97));
				visited[0] = true;
				// a~z (ex. 1+97=98 => (char)98 = 'b')
				break;
			case 1:
				key.append((char) ((int) (rnd.nextInt(26)) + 65));
				visited[1] = true;
				// A~Z
				break;
			case 2:
				key.append((rnd.nextInt(10)));
				visited[2] = true;
				// 0~9
				break;
			case 3:
				key.append(arr[idx]);
				visited[3] = true;
			}

			if (i == 7) {
				for (int j = 1; j < visited.length; j++) {
					if (!visited[j]) {
						i--;
						break;
					}
				}
			}
			if (key.length() > 16) {
				key.delete(0, key.length());
				i = 0;
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

	public void shareMessage(Map<String, Object> map) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();

		Long mnum = (Long) map.get("mnum");
		Optional<Member> m = mr.findById(mnum);
		Member member = m.get();
		message.addRecipients(RecipientType.TO, member.getEmail());// 보내는 대상

		String writerId = (String) map.get("writerId");
		message.setSubject(writerId + "님의 게시물 | RELO");

		String hashList = (String) map.get("hashList");
		String msgg = "";
		msgg += hashList + "<br/><br/>";
		String url = (String) map.get("url");
		msgg += url;
		message.setText(msgg, "UTF-8", "HTML");
		emailSender.send(message);

	}

	public void payRequestMessage(Map<String, Object> map) throws Exception {
		MimeMessage message = emailSender.createMimeMessage();
		Long mnum = Long.valueOf(map.get("mnum").toString());
		String sName = map.get("sname").toString();
		Integer aPrice = Integer.valueOf(map.get("aprice").toString());

		Optional<Member> m = mr.findById(mnum);
		Member member = m.get();
		message.addRecipients(RecipientType.TO, member.getEmail());// 보내는 대상

		message.setSubject("[RELO] 결제 요청");// 제목

		String msgg = "";
		msgg += "<div align='center'>";
		msgg += "<h3> 안녕하세요 RELO입니다.<br/>상품 배송을 위한 결제 요청 메일입니다.</h3>";
		msgg += "<br>";
		msgg += "<p>상품 배송을 위해 결제 부탁 드립니다.</p>";
		msgg += "<br>";
		msgg += "<br>";
		msgg += "<div align='center' style='font-family:verdana';>";
		msgg += "<h3 style='color:blue;'>상품 정보</h3>";
		msgg += "<div style='font-size:130%'>";
		msgg += "상품명 : <strong>";
		msgg += sName + "</strong></div><br/> ";
		msgg += "<div style='font-size:130%'>";
		msgg += "결제 요청 금액 : <strong>";
		msgg += aPrice + "</strong></div><br/> ";
		msgg += "</div>";
		msgg += "<p style='font-size:10px'> *본 메일은 발신 전용입니다. <br/>더 궁금하신 내용은 고객센터로 문의주시면 신속하게 답변드리겠습니다.</p>";
		msgg += "<p style='font-size:10px'> *고객센터 : 1234-5678(평일 11:00 - 18:00)</p></div>";

		message.setText(msgg, "UTF-8", "HTML");// 내용
		message.setFrom(new InternetAddress("relokosta@gmail.com", "RELO"));// 보내는 사람
		emailSender.send(message);
	}

	public void payConfirmMessage(Map<String, Object> map) throws Exception {
		MimeMessage message = emailSender.createMimeMessage();
		Long mnum = Long.valueOf(map.get("mnum").toString());
		String sName = map.get("sname").toString();
		Integer aPrice = Integer.valueOf(map.get("aprice").toString());

		Optional<Member> m = mr.findById(mnum);
		Member member = m.get();
		message.addRecipients(RecipientType.TO, member.getEmail());// 보내는 대상

		message.setSubject("[RELO] 결제 요청");// 제목

		String msgg = "";
		msgg += "<div align='center'>";
		msgg += "<h3> 안녕하세요 RELO입니다.<br/>정산을 위한 구매확정 요청 메일입니다.</h3>";
		msgg += "<br>";
		msgg += "<p>금액 정산을 위해 구매확정 부탁 드립니다.</p>";
		msgg += "<br>";
		msgg += "<br>";
		msgg += "<div align='center' style='font-family:verdana';>";
		msgg += "<h3 style='color:blue;'>상품 정보</h3>";
		msgg += "<div style='font-size:130%'>";
		msgg += "상품명 : <strong>";
		msgg += sName + "</strong></div><br/> ";
		msgg += "<div style='font-size:130%'>";
		msgg += "결제 금액 : <strong>";
		msgg += aPrice + "</strong></div><br/> ";
		msgg += "</div>";
		msgg += "<p style='font-size:10px'> *본 메일은 발신 전용입니다. <br/>더 궁금하신 내용은 고객센터로 문의주시면 신속하게 답변드리겠습니다.</p>";
		msgg += "<p style='font-size:10px'> *고객센터 : 1234-5678(평일 11:00 - 18:00)</p></div>";

		message.setText(msgg, "UTF-8", "HTML");// 내용
		message.setFrom(new InternetAddress("relokosta@gmail.com", "RELO"));// 보내는 사람
		emailSender.send(message);
	}
}