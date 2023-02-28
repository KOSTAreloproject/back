package com.my.relo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.OrdersDTO;
import com.my.relo.exception.FindException;
import com.my.relo.service.OrderDeliveryService;
import com.my.relo.service.OrdersService;

@RestController
@RequestMapping("/orders")
public class OrdersController {
	@Autowired
	private OrdersService service;
	@Autowired
	private OrderDeliveryService odService;
	
	//회원 주문 목록
	@GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> list(HttpSession session) {
		Long mNum = (Long) session.getAttribute("logined");
//		mNum = 8L;
		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else { 
			List<OrdersDTO> list = new ArrayList<>();
			
			try {
				list = service.getListBymNum(mNum);
				if (list.size()== 0) {
					Map map = new HashMap();
					map.put("msg", "구매 이력이 없습니다.");
					map.put("status", "-1");
					return new ResponseEntity<>(map, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(list, HttpStatus.OK);
				}
				
			} catch (FindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	//회원 주문 상세
	@GetMapping(value = "detail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> detail(HttpSession session, Long aNum) {
		Long mNum = (Long) session.getAttribute("logined");
//		mNum = 8L;
		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else { 
			OrdersDTO dto = new OrdersDTO();
			
			try {
				dto = service.getOrdersByaNum(aNum);
				if (dto == null) {
					Map map = new HashMap();
					map.put("msg", "구매 상세내역이 없습니다.");
					map.put("status", "-1");
					return new ResponseEntity<>(map, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(dto, HttpStatus.OK);
				}
				
			} catch (FindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	//관리자용, 회원들의 구매확정 목록
	@GetMapping(value = "confirmlist", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> confirmlist (HttpSession session) {
		Long mNum = (Long) session.getAttribute("logined");
//		mNum = 8L;
		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else { 
			List<OrdersDTO> list = new ArrayList<>();
			
			//관리자인지 확인하는 문구 추가하기
			
			try {
				list = service.getListBypStatus();
				if (list.size()== 0) {
					Map map = new HashMap();
					map.put("msg", "회원들의 구매확정 내역이 없습니다.");
					map.put("status", "-1");
					return new ResponseEntity<>(map, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(list, HttpStatus.OK);
				}
				
			} catch (FindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
}
