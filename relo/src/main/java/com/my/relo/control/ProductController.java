package com.my.relo.control;

import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.PInfoDTO;
import com.my.relo.entity.PInfo;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.service.ProductService;

@RestController
@RequestMapping("product/*")
public class ProductController {
	@Autowired
	ProductService service;
	
	@PostMapping("add")
	public ResponseEntity<?> ProductAdd(HttpSession session, 
			@RequestBody Map<String, Long> sNum) throws AddException{
		
		Long mNum = (Long) session.getAttribute("logined");
		
		if (mNum == null) {
			throw new AddException("로그인하세요");
		}
		Long snum = Long.valueOf(sNum.get("sNum"));
		service.ProductAdd(snum,mNum);
		
	return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	@GetMapping("listById/{currentPage}")
	public ResponseEntity<?> selectByIdProduct(HttpSession session,@PathVariable int currentPage) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}

		Map<String,Object> resultMap = service.selectByIdProduct(mNum,currentPage);
		
	return new ResponseEntity<>(resultMap,HttpStatus.OK);
	
	}
	
	@GetMapping("detailById")
	public ResponseEntity<?> selectByIdProductDetail(HttpSession session,Long pNum) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		

		List<PInfoDTO> list = service.selectByIdProductDetail(mNum,pNum);
		
	return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
	
	@GetMapping("EndListById/{currentPage}")
	public ResponseEntity<?> ProductEndListById(HttpSession session,@PathVariable int currentPage) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		

		Map<String,Object> resultMap = service.ProductEndListById(mNum,currentPage);
		
	return new ResponseEntity<>(resultMap,HttpStatus.OK);
	
	}
	
	@GetMapping("EndDetailById")
	public ResponseEntity<?> ProductEndDetailById(HttpSession session) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		

		List<PInfoDTO> list = service.ProductEndDetailById(mNum);
		
	return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
	
	@PutMapping("editPStatus8")
	public ResponseEntity<?> updatePStatus8(HttpSession session,@RequestBody  Map<String, Long> pNum) throws AddException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new AddException("로그인하세요");
		}
		
		Long pnum = Long.valueOf(pNum.get("pNum"));
		service.updateProductStatus8(pnum);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
