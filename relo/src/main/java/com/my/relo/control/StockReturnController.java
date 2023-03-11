package com.my.relo.control;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.entity.StockReturn;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.service.StockReturnService;

@RestController
@RequestMapping("stockReturn/*")
public class StockReturnController {
	@Autowired
	StockReturnService stockReturnService;
	
	@PostMapping("add")
	public ResponseEntity<?> addStockReturn(@RequestBody  Map<String, Long> num) throws AddException{
		Long mnum = Long.valueOf(num.get("mNum"));
		Long snum = Long.valueOf(num.get("sNum"));
		stockReturnService.addStockReturn(mnum,snum);
		
		
		
	return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	@GetMapping("listById/{currentPage}")
	public ResponseEntity<?> selectByIdStockReturn(HttpSession session,@PathVariable int currentPage) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		

		Map<String,Object> resultMap  = stockReturnService.selectByIdStockReturn(mNum,currentPage);
		
	return new ResponseEntity<>(resultMap,HttpStatus.OK);
	
	}
	
	@GetMapping("detailById")
	public ResponseEntity<?> selectByIdStockReturnDetail(HttpSession session,Long sNum) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		

		Map<String, Object> list = stockReturnService.selectByIdStockReturnDetail(mNum,sNum);
		
		return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
	
	//재고반송 개수 세기
	@GetMapping("count")
	   public ResponseEntity<?> selectCntSrById(HttpSession session) throws FindException {

	      Long mNum = (Long) session.getAttribute("logined");
	      if (mNum == null) {
	         throw new FindException("로그인하세요");
	      }
	      Integer cnt = stockReturnService.selectCntSrById(mNum);

	      return new ResponseEntity<>(cnt, HttpStatus.OK);

	   }
	
	
}
