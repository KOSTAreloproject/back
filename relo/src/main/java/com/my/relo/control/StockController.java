package com.my.relo.control;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.StockDTO;

import com.my.relo.entity.Stock;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.service.StockService;

@RestController
@RequestMapping("stock/*")
public class StockController {
	
	@Autowired
	StockService stockService;
	
	@PostMapping("add")
	public ResponseEntity<?> StockAdd(HttpSession session, 
		 StockDTO stock) throws AddException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new AddException("로그인하세요");
		}

//		Long mNum = (long) 2;

		stock.stockSetMember(mNum);
		
		stockService.StockAdd(stock);
		
	return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	@PostMapping("editSstatus")
	public ResponseEntity<?> updateSetSStatus(HttpSession session, 
		 StockDTO stock) throws AddException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new AddException("로그인하세요");
		}

//		Long mNum = (long) 2;

		stock.stockSetMember(mNum);
		
		stockService.updateSetSStatus(stock);
		
	return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	@PostMapping("editSstatus5")
	public ResponseEntity<?> updateByCancleSStatus5(HttpSession session, 
		 Long sNum) throws AddException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new AddException("로그인하세요");
		}
		
		stockService.updateByCancleSStatus5(sNum);
		
	return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	@PostMapping("listById")
	public ResponseEntity<?> selectById(HttpSession session) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		
//		Long mNum = (long) 2;
		List<StockDTO> list = stockService.selectById(mNum);
		
	return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
	
	@PostMapping("detailById")
	public ResponseEntity<?> detailById(Long sNum,HttpSession session) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		
//		Long mNum = (long) 2;
		List<StockDTO> list = stockService.detailById(sNum,mNum);
		
	return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
	
	@PostMapping("listBySstatus")
	public ResponseEntity<?> selectBySstatus(HttpSession session,Integer sStatus) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		

		List<StockDTO> list = stockService.selectBySstatus(sStatus);
		
	return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
	
	@PostMapping("detailBySNum")
	public ResponseEntity<?> detailBySNum(Long sNum,HttpSession session) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}

		Stock s = stockService.detailBySNum(sNum);
		
	return new ResponseEntity<>(s,HttpStatus.OK);
	
	}
}
