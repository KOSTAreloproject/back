package com.my.relo.control;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
		 StockDTO stock, @RequestPart(value = "f", required = false) MultipartFile f) throws AddException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new AddException("로그인하세요");
		}

		stock.stockSetMember(mNum);
		
		Long sNum = stockService.StockAdd(stock);
		
	      String saveDirectory = "C:\\storage\\stock";
	      File saveDirFile = new File(saveDirectory);

	      if (f != null && f.getSize() > 0) {

	         String orignFileName = f.getOriginalFilename();
	         // 파일 저장
	         String fileName = "st_" + sNum + "." + orignFileName.substring(orignFileName.lastIndexOf(".") + 1);
	         File file = new File(saveDirFile, fileName);

	         try {
	            f.transferTo(file);
	         } catch (IOException e) {
	            e.printStackTrace();
	            throw new AddException(e.getMessage());
	         }
	      }
		
	return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	
	@PutMapping(value = "editSstatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateSetSStatus(HttpSession session,
		@RequestBody Map<String, Object> stock) throws AddException{

//		Long mNum = (Long) session.getAttribute("logined");
//		if (mNum == null) {
//			throw new AddException("로그인하세요");
//		}
//		
		Long mNum = (long)2;
		Long snum = Long.valueOf((Integer) stock.get("sNum"));
		String sGrade = (String) stock.get("sGrade");
		String managerComment = (String) stock.get("managerComment");
		Integer sHopePrice = (Integer) stock.get("sHopePrice");
		
		StockDTO sDto = StockDTO.builder()
				.sNum(snum)
				.sGrade(sGrade)
				.managerComment(managerComment)
				.mNum(mNum)
				.sHopePrice(sHopePrice)
				.build();

		stockService.updateSetSStatus(sDto);
		
	return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	@PutMapping("editSstatus5")
	   public ResponseEntity<?> updateByCancleSStatus5(HttpSession session,
			   @RequestBody Map<String, Long> sNum)
	         throws AddException {
	      Long snum = Long.valueOf(sNum.get("sNum"));
	      
	      Long mNum = (Long) session.getAttribute("logined");
	      if (mNum == null) {
	         throw new AddException("로그인하세요");
	      }

	      stockService.updateByCancleSStatus5(snum);

	      return new ResponseEntity<>(HttpStatus.OK);

	   }
	
	@GetMapping("listById/{currentPage}")
	public ResponseEntity<?> selectById(HttpSession session,@PathVariable int currentPage) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		


		Map<String,Object> resultMap  = stockService.selectById(mNum,currentPage);
		
	return new ResponseEntity<>(resultMap,HttpStatus.OK);
	
	}
	
	@GetMapping("detailById")
	public ResponseEntity<?> detailById( Long sNum,HttpSession session) throws FindException{
		
//		Long mNum = (Long) session.getAttribute("logined");
//		if (mNum == null) {
//			throw new FindException("로그인하세요");
//		}
		Long mNum	= (long)2;
		List<StockDTO> list = stockService.detailById(sNum,mNum);
		
	return new ResponseEntity<>(list,HttpStatus.OK);
	
	}
	
	@GetMapping("listBySstatus/{currentPage}")
	public ResponseEntity<?> selectBySstatus(HttpSession session, Integer sStatus,@PathVariable int currentPage) throws FindException{
		
//		Long mNum = (Long) session.getAttribute("logined");
//		if (mNum == null) {
//			throw new FindException("로그인하세요");
//		}

		
		Map<String,Object> resultMap = stockService.selectBySstatus(sStatus,currentPage);
		
	return new ResponseEntity<>(resultMap,HttpStatus.OK);
	
	}
	
	@GetMapping("detailBySNum")
	public ResponseEntity<?> detailBySNum(Long sNum,HttpSession session) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}

		Stock s = stockService.detailBySNum(sNum);
		
	return new ResponseEntity<>(s,HttpStatus.OK);
	
	}
	
	// 재고 이미지 출력
	   @GetMapping(value = "img/{sNum}")
	   public ResponseEntity<?> showImage(@PathVariable Long sNum) throws IOException  {
	      String saveDirectory = "C:\\storage\\stock";
	      File saveDirFile = new File(saveDirectory);

	      // 해당 경로의 정보 필요 -> listFiles로 해당 경로의 정보 얻어옴
	      File[] files = saveDirFile.listFiles();
	      Resource img = null;
	      for (File f : files) {
	         // .을 기준으로, 확장자명 앞에 있는 파일명 얻음
	         StringTokenizer stk = new StringTokenizer(f.getName(), ".");
	         String fileName = stk.nextToken();
	         
	         // 해당 파일들 중에서 원하는 파일 이름과 일치하는 파일명이 존재할 경우
	         if (fileName.equals("st_" + sNum)) {
	            img = new FileSystemResource(f);
	            
	            // response 헤더 설정을 따로 하지 않을 경우, 응답 헤더의 content-type이 application/json 타입으로 되므로 설정 필요함
	            HttpHeaders responseHeaders = new HttpHeaders();
	            // 파일 크기 설정
	            responseHeaders.set(HttpHeaders.CONTENT_LENGTH, f.length()+"");
	            // 파일 타입 설정
	            responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(f.toPath()));
	            // 파일 인코딩 설정
	            responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+URLEncoder.encode("a", "UTF-8"));
	            return new ResponseEntity<>(img, responseHeaders, HttpStatus.OK);
	         }
	      }

	         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	   }

}
