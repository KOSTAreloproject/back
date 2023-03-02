package com.my.relo.control;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.service.LikesService;

@RestController
@RequestMapping("style/likes/*")
public class LikesController {
	
	@Autowired
	private LikesService service;
	/**
	 * 좋아요 + 1
	 * @param styleNum
	 * @param session
	 * @return
	 * @throws AddException
	 * @throws FindException 
	 */
	@PostMapping(value="{styleNum}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> likePlus(@PathVariable("styleNum")Long styleNum,
										HttpSession session) throws AddException, FindException{
		Long mNum = (Long)session.getAttribute("logined");
		if(mNum == null) {//로그인 안한 경우
			throw new FindException("로그인하세요");
		}
		service.plusLikes(mNum, styleNum);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	/**
	 * 좋아요 - 1
	 * @param styleNum
	 * @param session
	 * @return
	 * @throws RemoveException
	 * @throws FindException 
	 */
	@DeleteMapping(value="{styleNum}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> likeMinus(@PathVariable("styleNum")Long styleNum,
										HttpSession session) throws RemoveException, FindException{
		Long mNum = (Long)session.getAttribute("logined");
		if(mNum == null) {//로그인 안한 경우
			throw new FindException("로그인하세요");
		}
		service.minusLikes(mNum, styleNum);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
