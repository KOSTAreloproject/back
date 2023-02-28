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

import com.my.relo.dto.AuctionDTO;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.service.AuctionService;

@RestController
@RequestMapping("/auction")
public class AuctionController {
	
	@Autowired
	private AuctionService service;
	
	// 회원 경매 참여할 경우
	@PostMapping(value = "add", produces="text/plain;charset=utf-8")
	public ResponseEntity<?> add(Long pNum, int aPrice, HttpSession session) {
		Long mNum = (Long) session.getAttribute("logined");
//		mNum = 3L;
		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else { 
			try {
				service.addAuction(pNum, mNum, aPrice);
				return new ResponseEntity<>("경매 참여 완료", HttpStatus.OK);
			} catch (AddException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	//회원 경매 진행 중 목록
	@GetMapping(value = "inglist", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> inglist(HttpSession session) {
		Long mNum = (Long) session.getAttribute("logined");
//		mNum = 8L;
		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else { 
			List<AuctionDTO> list = new ArrayList<>();
			
			try {
				list = service.getIngListBymNum(mNum);
				if (list.size()== 0) {
					Map map = new HashMap();
					map.put("msg", "경매 이력이 없습니다.");
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
	
	//회원 경매 종료 목록
	@GetMapping(value = "endlist", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> endlist(HttpSession session) {
		Long mNum = (Long) session.getAttribute("logined");
//			mNum = 8L;
		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else { 
			List<AuctionDTO> list = new ArrayList<>();
	
			try {
				list = service.getEndListBymNum(mNum);
				if (list.size()== 0) {
					Map map = new HashMap();
					map.put("msg", "경매 이력이 없습니다.");
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
