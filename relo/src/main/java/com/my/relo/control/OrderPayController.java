package com.my.relo.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.service.OrderDeliveryService;

@RestController
@RequestMapping(value = "/order-pay")
public class OrderPayController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OrderDeliveryService service;
	
	//회원 결제 완료시 주문/주문배송tb 값 삽입
	@PostMapping(value = "/pay", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> add(HttpSession session, @RequestBody HashMap<String, Object> param) {
		Long mNum = (Long) session.getAttribute("logined");

		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else { 
			try {
				Map map = new HashMap();
				
				for (Object key : param.keySet()) {
					logger.info("값 : " + param.get(key));
					map.put(key, param.get(key));
				}
				map.put("msg", "주문완료");
				return new ResponseEntity<>(map, HttpStatus.OK);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("발생에러 : ", e.getStackTrace());
				Map map = new HashMap();
				map.put("error_msg", e.getMessage());
				return new ResponseEntity<>(map,  HttpStatus.BAD_REQUEST);
			}
		}
	}
}