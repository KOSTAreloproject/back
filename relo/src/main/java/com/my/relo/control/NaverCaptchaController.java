package com.my.relo.control;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.util.ApiExamCaptchaImage;
import com.my.relo.util.ApiExamCaptchaNkey;
import com.my.relo.util.ApiExamCaptchaNkeyResult;

@RestController
@RequestMapping("naver/*")
public class NaverCaptchaController {
	
	private final static String  clientId = "3Lz8ultBXAN89X2SzUMR";  //애플리케이션 클라이언트 아이디값";
	private final static String  clientSecret = "caTMvtGKQ6";  //애플리케이션 클라이언트 시크릿값";
	
	/**
	 * 캡차 이미지 키 발급 
	 * @return
	 */
	@GetMapping("captchaKey")
	public String ApiExamCaptchaNkey(){

        String code = "0"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
        String apiURL = "https://openapi.naver.com/v1/captcha/nkey?code=" + code;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String key = ApiExamCaptchaNkey.get(apiURL, requestHeaders);

	    return key.substring(8, 24);
	}
	
	/**
	 * 발급된 키를 가지고 캡차 이미지 생성 및 저장. 
	 * @param key 발급된 키
	 * @return 이미지 name
	 */
	@GetMapping("captchaImg")
	public String ApiExamCaptchaImage(String key){
		
        String apiURL = "https://openapi.naver.com/v1/captcha/ncaptcha.bin?key=" + key;  // https://openapi.naver.com/v1/captcha/nkey 호출로 받은 키값

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String img = ApiExamCaptchaImage.get(apiURL,requestHeaders);
        
	    return img;
	}
	
	/**
	 * 사용자 입력값 비교
	 * @param key 발급된 키
	 * @param value 사용자가 입력한 값
	 * @return key와 value가 일치하면 true 아니면 false 반환
	 */
	@GetMapping("captchaKeyResult")
	public String ApiExamCaptchaNkeyResult(String key,String value){

        String code = "1"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
        String apiURL = "https://openapi.naver.com/v1/captcha/nkey?code=" + code + "&key=" + key + "&value=" + value;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String result = ApiExamCaptchaNkeyResult.get(apiURL, requestHeaders);

	    return result;
	}
}
