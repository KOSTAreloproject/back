package com.my.relo.control;

import java.util.Map;

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

	// 찜 목록 보기
//	@GetMapping(value = "{mNum}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> myZzimList(@PathVariable Long mNum) throws FindException {
//		List<ZPResponseDTO> list = service.readZzimList(mNum);
//		return new ResponseEntity<>(list, HttpStatus.OK);
//	}

	@GetMapping(value = "{cp}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> MyZzimList(HttpSession session, @PathVariable(name = "cp") int cp) throws FindException {
		Long mNum = (Long) session.getAttribute("logined");
//		Long mNum = 4L;
		Map<String, Object> map = service.readZzimList(mNum, cp);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	// 찜 추가
	@PostMapping(value = "{pNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addMyZzimList(HttpSession session, @PathVariable(value = "pNum") Long pNum)
			throws AddException {
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new AddException("먼저 로그인을 해주세요.");
		}
		service.createZzimList(mNum, pNum);
		return new ResponseEntity<>(HttpStatus.OK);
	}

//	@PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> addMyZzimList(@RequestParam(value = "mNum") Long mNum,
//			@RequestParam(value = "pNum") Long pNum) throws AddException {
//		System.out.println(mNum + "+" + pNum);
//		service.createZzimList(mNum, pNum);
//		return new ResponseEntity<>(HttpStatus.OK);
//	}

	// 찜 삭제
	@DeleteMapping(value = "{pNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delMyZzimList(HttpSession session, @PathVariable Long pNum) throws RemoveException {
		Long mNum = (Long) session.getAttribute("logined");
		service.deleteZzimList(mNum, pNum);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
