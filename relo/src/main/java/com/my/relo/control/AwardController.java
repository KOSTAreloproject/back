package com.my.relo.control;

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
import com.my.relo.exception.FindException;
import com.my.relo.service.AwardService;

@RestController
@RequestMapping(value = "/award/")
public class AwardController {
	@Autowired
	private AwardService service;

	// 회원 낙찰 포기할 경우
	@PostMapping(value = "del", produces = "text/plain;charset=utf-8")
	public ResponseEntity<?> del(Long aNum, HttpSession session) {
		Long mNum = (Long) session.getAttribute("logined");

		if (mNum == null) {
			return new ResponseEntity<>("로그인 먼저 하세요", HttpStatus.BAD_REQUEST);
		} else {
			try {
				// 로그인한 사람과 삭제되는 컬럼의 회원번호 일치하는지 확인

				// 계형님께 여쭤보기,,
				// pService.editStatus8(aNum);
				service.delAward(aNum);
				return new ResponseEntity<>("낙찰 포기 완료", HttpStatus.OK);
			} catch (FindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>("취소 처리 실패", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	// 낙찰상품 목록 - 관리자용
	@GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> inglist(HttpSession session) {
		Long mNum = (Long) session.getAttribute("logined");

		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else {
			// 멤버 관리자 맞는지 확인하는 코드 넣기

			List<AuctionDTO> list = new ArrayList<>();

			try {
				list = service.getAwardList();
				if (list.size() == 0) {
					Map map = new HashMap();
					map.put("msg", "회원들의 낙찰내역이 없습니다.");
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