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
	ProductService productService;
	
	@PostMapping("add")
	public ResponseEntity<?> ProductAdd(HttpSession session, 
			PInfo product,Long sNum) throws AddException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new AddException("로그인하세요");
		}

		productService.ProductAdd(product,sNum,mNum);
		
	return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	@GetMapping("listById/{currentPage}")
	public ResponseEntity<?> selectByIdProduct(HttpSession session,@PathVariable int currentPage) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		
//		Long mNum = (long) 3;
		Map<String,Object> resultMap = productService.selectByIdProduct(mNum,currentPage);
		
	return new ResponseEntity<>(resultMap,HttpStatus.OK);
	
	}
	
	@GetMapping("detailById")
	public ResponseEntity<?> selectByIdProductDetail(HttpSession session,Long pNum) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		
//		Long mNum = (long) 2;
		List<PInfoDTO> list = productService.selectByIdProductDetail(mNum,pNum);
		
	return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
	
	@GetMapping("EndListById/{currentPage}")
	public ResponseEntity<?> ProductEndListById(HttpSession session,@PathVariable int currentPage) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		
//		Long mNum = (long) 1;
		Map<String,Object> resultMap = productService.ProductEndListById(mNum,currentPage);
		
	return new ResponseEntity<>(resultMap,HttpStatus.OK);
	
	}
	
	@GetMapping("EndDetailById")
	public ResponseEntity<?> ProductEndDetailById(HttpSession session) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		
//		Long mNum = (long) 2;
		List<PInfoDTO> list = productService.ProductEndDetailById(mNum);
		
	return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
}
