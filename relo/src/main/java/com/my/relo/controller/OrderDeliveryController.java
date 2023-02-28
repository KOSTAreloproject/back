package com.my.relo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.AddressDTO;
import com.my.relo.dto.OrdersDTO;
import com.my.relo.exception.FindException;
import com.my.relo.service.OrderDeliveryService;

@RestController
@RequestMapping("/orderDelivery")
public class OrderDeliveryController {
	
	@Autowired
	private OrderDeliveryService service;
	
	//회원 결제 완료시 주문/주문배송tb 값 삽입
	@PostMapping(value = "add", produces="text/plain;charset=utf-8")
	public ResponseEntity<?> add(HttpSession session, Long aNum, AddressDTO adDTO, OrdersDTO odDTO) {
		Long mNum = (Long) session.getAttribute("logined");
//			mNum = 10L;
		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else { 
			try {
				adDTO.setMNum(mNum);
				odDTO.setMNum(mNum);
				
				// 운송장 번호 랜덤으로 발생시키기 6개, 7개 순
				int preValue = (int) (Math.random() * 1000000);
				int postValue = (int) (Math.random() * 10000000);

				// 운송장 번호
				String tracking = String.valueOf(preValue) + "-" + String.valueOf(postValue);
				odDTO.setDTrackingInfo(tracking);
				
				service.addOrderDelivery(aNum, adDTO, odDTO);
				return new ResponseEntity<>("주문 완료.", HttpStatus.OK);
				
			} catch (FindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(e.getMessage(),  HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	//회원 구매확정
	@PostMapping(value = "confirm", produces="text/plain;charset=utf-8")
	public ResponseEntity<?> confirm(HttpSession session, Long aNum) {
		Long mNum = (Long) session.getAttribute("logined");
//		mNum = 8L;
		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else { 
			try {
				service.editDstatus(aNum);
				return new ResponseEntity<>("구매확정 완료.", HttpStatus.OK);
				
			} catch (FindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(e.getMessage(),  HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	//회원 배송지 변경 - 배송준비 중일 때만
//	@PostMapping(value = "edit", produces="text/plain;charset=utf-8")
//	public ResponseEntity<?> edit(HttpSession session, Long aNum, Long addrNum) {
//		Long mNum = (Long) session.getAttribute("logined");
////		mNum = 8L;
//		if (mNum == null) {
//			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
//		} else { 
//			try {
//				service.editAddrNum(aNum, addrNum);
//				return new ResponseEntity<>("배송지 주소 변경 완료.", HttpStatus.OK);
//			} catch (FindException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return new ResponseEntity<>(e.getMessage(),  HttpStatus.BAD_REQUEST);
//			}
//		}
//	}
}
