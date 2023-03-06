
package com.my.relo.control;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.my.relo.dto.AuctionDTO;
import com.my.relo.dto.PInfoDTO;
import com.my.relo.dto.ZPResponseDTO;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.service.ProductService;
import com.my.relo.service.ZzimService;
import com.my.relo.util.CustomLocalDateTimeSerializer;

@RestController
@RequestMapping("product/*")
public class ProductController {

	@Autowired
	private ProductService service;

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private ZzimService zService;

	@PostMapping("add")
	public ResponseEntity<?> ProductAdd(HttpSession session, Long sNum) throws AddException {

		Long mNum = (Long) session.getAttribute("logined");

		if (mNum == null) {
			throw new AddException("로그인하세요");
		}
		
		
		service.ProductAdd(sNum, mNum);

		return new ResponseEntity<>(HttpStatus.OK);

	}

	@GetMapping("listById/{currentPage}")
	public ResponseEntity<?> selectByIdProduct(HttpSession session, @PathVariable int currentPage)
			throws FindException {

		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		
		
		Map<String, Object> resultMap = service.selectByIdProduct(mNum, currentPage);

		return new ResponseEntity<>(resultMap, HttpStatus.OK);

	}

	@GetMapping("detailById")
	public ResponseEntity<?> selectByIdProductDetail(HttpSession session, Long pNum) throws FindException {

		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		

		List<PInfoDTO> list = service.selectByIdProductDetail(mNum, pNum);

		return new ResponseEntity<>(list, HttpStatus.OK);

	}

	@GetMapping("EndListById/{currentPage}")
	public ResponseEntity<?> ProductEndListById(HttpSession session, @PathVariable int currentPage)
			throws FindException {

		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		

		Map<String, Object> resultMap = service.ProductEndListById(mNum, currentPage);

		return new ResponseEntity<>(resultMap, HttpStatus.OK);

	}

	@GetMapping("EndDetailById")
	public ResponseEntity<?> ProductEndDetailById(HttpSession session) throws FindException {

		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}

		List<PInfoDTO> list = service.ProductEndDetailById(mNum);

		return new ResponseEntity<>(list, HttpStatus.OK);

	}

	@PutMapping("editPStatus8")
	public ResponseEntity<?> updatePStatus8(HttpSession session, @RequestBody Map<String, Long> pNum)
			throws AddException {

		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new AddException("로그인하세요");
		}

		Long pnum = Long.valueOf(pNum.get("pNum"));
		service.updateProductStatus8(pnum);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "shop/{start}", produces = MediaType.APPLICATION_JSON_VALUE)
	   public ResponseEntity<?> readShopProdList(@PathVariable(value = "start") int start,
	         @RequestParam(name = "prodCate", required = false, defaultValue = "") String prodCate,
	         @RequestParam(name = "tender", required = false, defaultValue = "") String tender,
	         @RequestParam(name = "sort", required = false, defaultValue = "") String sort) throws FindException {
	      System.out.println("조건1" + prodCate);
	      System.out.println("조건2" + tender);
	      System.out.println("조건3" + sort);
	      String pc = "";
	      String td = "";
	      String s = "";
	      switch (prodCate) {
	      case "top":
	         pc = "'상의'";
	         break;
	      case "bottom":
	         pc = "'하의'";
	         break;
	      case "shoes":
	         pc = "'하의'";
	         break;
	      default:
	         pc = "'상의', '하의', '신발'";
	         break;
	      }
	      switch (tender) {
	      case "no":
	         td = "AND a.p_num IS NULL ";
	         break;
	      default:
	         break;
	      }
	      switch (sort) {
	      case "pend":
	         s = "p_end_date ASC";
	         break;
	      case "pnum":
	         s = "p.p_num DESC";
	         break;
	      case "zzim":
	         s = "nvl(z.zcount,0) DESC";
	         break;
	      default:
	         s = "p.p_num DESC";
	         break;
	      }

	      List<ZPResponseDTO> list = service.shopProductList(start, pc, td, s);
	      return new ResponseEntity<>(list, HttpStatus.OK);
	//
	   }

	   @GetMapping(value = "search")
	   public ResponseEntity<?> readSearchProductList(@RequestParam("keyword") String keyword, @RequestParam("cp") int cp)
	         throws FindException, UnsupportedEncodingException {
	      String decode = java.net.URLDecoder.decode(keyword, "UTF-8");
	      System.out.println(decode);
	      Map<String, Object> map = service.searchProductList(decode, cp);
	      return new ResponseEntity<>(map, HttpStatus.OK);
	   }

	   @GetMapping(value = "shoplist/{pNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	   public ResponseEntity<?> readShopProductDetail(@PathVariable Long pNum)
	         throws FindException, JsonProcessingException {
	      SimpleModule simpleModule = new SimpleModule();
	      simpleModule.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
	      ZPResponseDTO zdto = service.ShopProductDetail(pNum);
	      ObjectMapper mapper = new ObjectMapper();
	      mapper.registerModule(simpleModule);
	      String list = mapper.writeValueAsString(zdto);

	      return new ResponseEntity<>(list, HttpStatus.OK);
	   }

	   @GetMapping(value = "recenttender/{pNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	   public ResponseEntity<?> readShopRecentTender(@PathVariable Long pNum) throws FindException {
	      List<AuctionDTO> adto = service.ShopRecentTender(pNum);
	      return new ResponseEntity<>(adto, HttpStatus.OK);
	   }

	   // SHOP > 상품 상세보기 이미지 출력
	   @GetMapping(value = "detail/img/{sNum}")
	   public ResponseEntity<?> getDetailFile(@PathVariable("sNum") Long sNum) throws IOException {
	      String saveDirectory = "C:\\storage\\stock";
	      File saveDirFile = new File(saveDirectory);
	      File[] files = saveDirFile.listFiles();
	      Resource img = null;
	      for (File f : files) {
	         StringTokenizer stk = new StringTokenizer(f.getName(), ".");
	         String fileName = stk.nextToken();
	         if (fileName.equals("st_" + sNum)) {
	            img = new FileSystemResource(f);
	            HttpHeaders responseHeaders = new HttpHeaders();
	            responseHeaders.set(HttpHeaders.CONTENT_LENGTH, f.length() + "");
	            responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(f.toPath()));
	            responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
	                  "inline; filename=" + URLEncoder.encode("a", "UTF-8"));

	            return new ResponseEntity<>(img, responseHeaders, HttpStatus.OK);
	         }
	      }
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	   }

	   // 상품상세 조회 시 찜하기 여부 확인
	   @GetMapping(value = "detail/ckzzim", produces = MediaType.APPLICATION_JSON_VALUE)
	   public ResponseEntity<?> checkExistZzim(HttpSession session, @RequestParam Long pNum) throws FindException {
	      Long mNum = (Long) session.getAttribute("logined");
	      int chz = zService.checkExistlist(mNum, pNum);
	      return new ResponseEntity<>(chz, HttpStatus.OK);

	      
	   }

	   // 상품목록 출력 시 이미지 목록 가져오기
	   @GetMapping(value = "list/img/{sNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	   public ResponseEntity<?> getImgList(@PathVariable("sNum") Long sNum) throws FindException, IOException {
	      String saveDirectory = "C:\\storage\\stock";
	      File saveDirFile = new File(saveDirectory);
	      File[] files = saveDirFile.listFiles();
	      File file = null;
	      String fileName;
	      Resource img = null;
	      for (File thumbF : files) {
	         StringTokenizer stk = new StringTokenizer(thumbF.getName(), ".");
	         fileName = stk.nextToken();
	         if (fileName.equals("t_st_" + sNum)) {
	            img = new FileSystemResource(thumbF);
	            file = thumbF;
	         }
	      }
	      HttpHeaders responseHeaders = new HttpHeaders();
	      responseHeaders.set(HttpHeaders.CONTENT_LENGTH, "" + file.length());
	      responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file.toPath()));
	      responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + URLEncoder.encode("a", "UTF-8"));

	      return new ResponseEntity<>(img, responseHeaders, HttpStatus.OK);
	   }
}

