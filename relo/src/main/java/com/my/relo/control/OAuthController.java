package com.my.relo.control;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.my.relo.dto.MemberDTO;
import com.my.relo.service.MemberService;
import com.my.relo.service.OAuthService;
 
@PropertySource("classpath:application.properties")
@Controller
@RequestMapping("/oauth/*")
public class OAuthController {

	@Value("${client.ip}")
	private String clientIp;

	@Value("${client.port}")
	private String clientPort;

	@Autowired
	private OAuthService os;

	@Autowired
	private MemberService ms;

	/**
	 * 카카오 callback [GET] /oauth/kakao/callback
	 */

	@GetMapping("kakao")
	public RedirectView kakaoCallback(HttpSession session, @RequestParam String code) {

		String token = os.getKakaoAccessToken(code);
		String email = os.createKakaoUser(token);

		MemberDTO dto = ms.findByEmail(email);
		if (dto != null) {

			session.setAttribute("logined", dto.getMnum());

			return new RedirectView("http://" + clientIp + ":" + clientPort + "/html/index.html");
		} else {
			return new RedirectView("http://" + clientIp + ":" + clientPort + "/html/login.html");
		}
	}
}