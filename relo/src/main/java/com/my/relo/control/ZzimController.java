package com.my.relo.controll;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.ZzimDTO;
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
	@GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> myZzimList(@RequestParam Long mNum) throws FindException {
		List<ZzimDTO> list = service.readZzimList(mNum);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

//	@GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> MyZzimList(HttpSession session) throws FindException {
//		Long mNum = (Long) session.getAttribute("logined");
//		List<ZzimDTO> list = service.readZzimList(mNum);
//		return new ResponseEntity<>(list, HttpStatus.OK);
//	}

	// 찜 추가
	@PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addMyZzimList(HttpSession session, @RequestParam(value = "pNum") Long pNum)
			throws AddException {
		Long mNum = (Long) session.getAttribute("logined");
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
	@DeleteMapping(value = "del", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delMyZzimList(HttpSession session, @RequestParam Long pNum) throws RemoveException {
		Long mNum = (Long) session.getAttribute("logined");
		try {
			service.deleteZzimList(mNum, pNum);
		} catch (RemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

//	@DeleteMapping(value = "del", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> delMyZzimList(@RequestParam(value = "mNum") Long mNum,
//			@RequestParam(value = "pNum") Long pNum) throws AddException {
//		System.out.println(mNum + "+" + pNum);
//		try {
//			service.deleteZzimList(mNum, pNum);
//		} catch (RemoveException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return new ResponseEntity<>(HttpStatus.OK);
//	}

}
