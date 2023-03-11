package com.my.relo.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.service.ZzimService;

@RestController
@RequestMapping("zzim/*")
public class ZzimController {
	@Autowired
	private ZzimService service;

	// 찜 목록보기
	@GetMapping(value = "{cp}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> MyZzimList(HttpServletRequest request, @PathVariable(name = "cp") int cp)
			throws FindException {
		HttpSession session = request.getSession();
		Long mNum = (Long) session.getAttribute("logined");
		Map<String, Object> map = service.readZzimList(mNum, cp);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	// 찜 목록에 추가하기
	@PostMapping(value = "{pNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addMyZzimList(HttpServletRequest request, @PathVariable(name = "pNum") Long pNum)
			throws AddException {
		Map<String, String> map = new HashMap<>();
		HttpSession session = request.getSession();
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			map.put("msg", "관심상품으로 추가하시려면 로그인을 해주세요.");
			return new ResponseEntity<>(map, HttpStatus.OK);
		}
		service.createZzimList(mNum, pNum);
		map.put("msg", "관심상품 목록에 추가했습니다.");
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	// 찜목록에서 삭제하기
	@DeleteMapping(value = "{pNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delMyZzimList(HttpSession session, @PathVariable(name = "pNum") Long pNum)
			throws RemoveException {
		Long mNum = (Long) session.getAttribute("logined");
		Map<String, String> map = new HashMap<>();
		if (mNum == null) {
			map.put("msg", "관심상품에서 삭제하시려면 로그인을 해주세요.");
			return new ResponseEntity<>(map, HttpStatus.OK);
		}
		service.deleteZzimList(mNum, pNum);
		map.put("msg", "관심상품 목록에서 삭제했습니다.");
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

}
