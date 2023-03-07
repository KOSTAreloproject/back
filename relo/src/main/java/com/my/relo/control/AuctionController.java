package com.my.relo.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.AuctionDTO;
import com.my.relo.dto.ZPResponseDTO;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.service.AuctionService;
import com.my.relo.service.ProductService;

@RestController
@RequestMapping(value = "/auction/")
public class AuctionController {

   @Autowired
   private AuctionService service;

   @Autowired
   private ProductService pService;

   // 회원 경매 참여할 경우
   @PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> add(HttpServletRequest request, @RequestBody Map<String, Object> tender) {

      Long pNum = Long.valueOf((String) tender.get("pNum"));
      int aPrice = Integer.parseInt((String) tender.get("aPrice"));
      HttpSession session = request.getSession();
      Long mNum = (Long) session.getAttribute("logined");
      if (mNum == null) {
         return new ResponseEntity<>("경매에 참여하려면 로그인 해주세요.", HttpStatus.BAD_REQUEST);
      } else {
         try {
            Map<String, String> map = new HashMap<>();
            // 판매자가 경매에 참여할 경우
            ZPResponseDTO dto = pService.ShopProductDetail(pNum);
            Integer max = service.maxPriceByPNum(pNum);

            if (dto.getMnum() == mNum) {
               map.put("msg", "판매자는 경매에 참여할 수 없습니다.");
               return new ResponseEntity<>(map, HttpStatus.OK);
            } else if (max == null) {
               max = dto.getSHopePrice();
               if (max > aPrice) {
                  map.put("msg", "희망판매가보다 낮은 금액으로 입찰 불가능합니다.");
                  return new ResponseEntity<>(map, HttpStatus.OK);
               }
               if (max * 2 < aPrice) {
                  map.put("msg", "희망판매가의 200% 보다 높은 금액으로 입찰 불가능합니다.");
                  return new ResponseEntity<>(map, HttpStatus.OK);
               }
               service.addAuction(pNum, mNum, aPrice);
               map.put("msg", "경매 참여 완료");
               return new ResponseEntity<>(map, HttpStatus.OK);
            } else if (max >= aPrice) {
               map.put("msg", "경매 최고가보다 낮거나 같은 금액으로 입찰 불가능합니다.");
               return new ResponseEntity<>(map, HttpStatus.OK);
            } else if (max * 2 < aPrice) {
               map.put("msg", "경매 최고가의 200% 보다 높은 금액으로 입찰 불가능합니다.");
               return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("msg", "경매 참여 완료");
            service.addAuction(pNum, mNum, aPrice);
            return new ResponseEntity<>(map, HttpStatus.OK);
         } catch (AddException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         } catch (FindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
      }
   }

   // 회원 경매 진행 중 목록
   @GetMapping(value = "inglist", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> inglist(HttpSession session) {
      Long mNum = (Long) session.getAttribute("logined");
      if (mNum == null) {
         return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
      } else {
         List<AuctionDTO> list = new ArrayList<>();

         try {
            list = service.getIngListBymNum(mNum);
            if (list.size() == 0) {
               Map map = new HashMap();
               map.put("msg", "경매 이력이 없습니다.");
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

   // 회원 경매 종료 목록
   @GetMapping(value = "/endlist", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> endlist(HttpSession session) {
      Long mNum = (Long) session.getAttribute("logined");
      if (mNum == null) {
         return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
      } else {
         List<AuctionDTO> list = new ArrayList<>();

         try {
            list = service.getEndListBymNum(mNum);
            if (list.size() == 0) {
               Map map = new HashMap();
               map.put("msg", "경매 이력이 없습니다.");
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
}