package com.my.relo.control;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.AddressDTO;
import com.my.relo.dto.OrderInfoDTO;
import com.my.relo.dto.OrdersDTO;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.service.AddressService;
import com.my.relo.service.AuctionService;
import com.my.relo.service.OrderDeliveryService;
import com.my.relo.service.OrdersService;
import com.my.relo.service.ProductService;

@RestController
@RequestMapping(value = "/order-delivery/")
public class OrderDeliveryController {

	@Autowired
	private OrderDeliveryService service;

	@Autowired
	private AddressService adService;
	
	@Autowired
	private OrdersService oService;
	
	@Autowired
	private AuctionService aService;
	
	@Autowired
	private ProductService pService;
	
	// 회원 결제 완료시 주문/주문배송tb 값 삽입
	@PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> add(HttpSession session, @RequestBody HashMap<String, Object> param) throws IOException {
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			return new ResponseEntity<>("로그인하세요", HttpStatus.BAD_REQUEST);
		} else {
			try {
				String token = oService.getToken();
				
				//결제 성공 금액 조회하기
				OrderInfoDTO oiDTO = oService.paymentInfo(param.get("imp_uid"), token);
				Integer paidPrice = oiDTO.getAPrice();
				String status = oiDTO.getStatus();
				String impUid = String.valueOf(param.get("imp_uid")); 
				Long aNum = Long.parseLong(String.valueOf(param.get("anum")));
				String oMemo = "";
				System.out.println(status+"원");
				System.out.println(paidPrice+"원");
				//결제 해야 할 금액 조회
				Integer expPrice = aService.priceByaNum(aNum); 
				//+배송비
				expPrice += 2500;

				if (param.get("omemo")!=null) {
					oMemo = String.valueOf(param.get("omemo"));
				}
				AddressDTO adDTO = null;
				OrdersDTO odDTO = new OrdersDTO(); //anum mnum omemo trackingInfo
				System.out.println(expPrice+"expPrice");
				System.out.println(oMemo+"- memo");
				System.out.println(aNum+"- aNum");
				if (expPrice.equals(paidPrice) && status.equals("paid")) {
					adDTO = adService.findByAddrNum(Long.parseLong(String.valueOf(param.get("addrnum"))));
					adDTO.setMNum(mNum);
					
					odDTO.setANum(aNum);
					odDTO.setMNum(mNum);
					odDTO.setOMemo(oMemo);
					odDTO.setImpUid(impUid);
					// 운송장 번호 랜덤으로 발생시키기 6개, 7개 순
					int preValue = (int) (Math.random() * 1000000);
					int postValue = (int) (Math.random() * 10000000);

					// 운송장 번호
					String tracking = String.valueOf(preValue) + "-" + String.valueOf(postValue);
					odDTO.setDTrackingInfo(tracking);
					
					service.addOrderDelivery(aNum, adDTO, odDTO);
					
					Map<String, String> res = new HashMap();
					res.put("msg", "주문 완료.");
					res.put("status", "0");
					return new ResponseEntity<>(res, HttpStatus.OK);
				} else {
					Map<String, String> res = new HashMap();
					res.put("msg", "금액이 다르거나 결제 되지 않음. 주문 취소.");
					res.put("status", "-1");
					return new ResponseEntity<>(res, HttpStatus.OK);
				}

			} catch (FindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}
	}

	// 회원 구매확정
	@PostMapping(value = "confirm", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> confirm(HttpSession session, @RequestBody HashMap<String, Long> body) throws AddException  {
		Long mNum = (Long) session.getAttribute("logined");
		Map<String, Object> map = new HashMap();
		if (mNum == null) {
			map.put("msg", "로그인하세요");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			try {
				Long aNum = body.get("aNum");
				Long addrNum = body.get("addrNum");
				Long pNum = body.get("pNum");
				service.editDstatus(aNum, addrNum);
				pService.updateProductStatus(pNum, 9);
				
				map.put("msg", "구매확정 완료");
				return new ResponseEntity<>(map, HttpStatus.OK);

			} catch (FindException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}
	}
}
