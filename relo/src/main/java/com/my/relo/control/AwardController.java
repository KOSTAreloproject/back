package com.my.relo.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.AuctionDTO;
import com.my.relo.dto.MemberDTO;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.service.AwardService;
import com.my.relo.service.MemberService;
import com.my.relo.service.ProductService;

@RestController
@RequestMapping(value = "/award/")
public class AwardController {
	@Autowired
	private AwardService service;

	@Autowired
	private ProductService pService;
	
	@Autowired
	private MemberService mService;
	
	// 회원 낙찰 포기할 경우
   @PostMapping(value = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> del(@RequestBody Map<String, Long> map, HttpSession session) {
      Long logined = (Long) session.getAttribute("logined");
      if (logined == null) {                                                                                   
         return new ResponseEntity<>("로그인 먼저 하세요", HttpStatus.OK);
      } else {
         try {
            if (!logined.equals(map.get("mnum"))) {
               Map map1 = new HashMap();
               map1.put("msg", "본인만 낙찰 포기 가능합니다.");
               map1.put("status", "-1");
               return new ResponseEntity<>(map1, HttpStatus.OK);
            }
            pService.updateProductStatus(map.get("pnum"), 8);
            service.delAward(map.get("anum"));
            Map map1 = new HashMap();
            map1.put("msg", "낙찰 포기 완료");
            map1.put("status", "0");
            return new ResponseEntity<>(map1, HttpStatus.OK);
         } catch (FindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Map map1 = new HashMap();
            map1.put("msg", "취소 처리 실패");
            map1.put("status", "-1");
            return new ResponseEntity<>(map1, HttpStatus.INTERNAL_SERVER_ERROR);
         } catch (AddException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Map map1 = new HashMap();
            map1.put("msg", "취소 처리 실패");
            map1.put("status", "-1");
            return new ResponseEntity<>(map1, HttpStatus.INTERNAL_SERVER_ERROR);
         }
      }
	}

	// 낙찰상품 목록 - 관리자용
	@GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> inglist(HttpSession session) {
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else {
			// 멤버 관리자 맞는지 확인하는 코드 넣기

			List<AuctionDTO> list = new ArrayList<>();

			try {
				list = service.getAwardList();
				if (list.size() == 0) {
					Map map = new HashMap();
					map.put("msg", "회원들의 낙찰내역이 없습니다.");
					map.put("status", "-1");
					return new ResponseEntity<>(map, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(list, HttpStatus.OK);
				}

			} catch (FindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	// 낙찰상품 목록 - 관리자용
	@GetMapping(value = "list/paging/{currentPage}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listPaging (HttpSession session, @PathVariable Integer currentPage) {
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			Map map = new HashMap();
			map.put("msg", "로그인하세요");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {			
			List<AuctionDTO> list = new ArrayList<>();

			try {
				// 멤버 관리자 맞는지 확인하는 코드 넣기
				MemberDTO m = mService.detailMember(mNum);
				if (m.getType()==0) {
					Map map = new HashMap();
					map.put("msg", "관리자만 볼 수 있습니다.");
					return new ResponseEntity<>(map, HttpStatus.OK);
				}
				Map<String, Object> res = service.getPagingList(currentPage);
				list = (List<AuctionDTO>) res.get("list");
				
				if (list.size() == 0) {
					Map map = new HashMap();
					map.put("msg", "회원들의 낙찰내역이 없습니다.");
					map.put("status", "-1");
					return new ResponseEntity<>(map, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(res, HttpStatus.OK);
				}

			} catch (FindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}
}
