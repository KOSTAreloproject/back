package com.my.relo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.relo.dto.OrderInfoDTO;
import com.my.relo.dto.OrdersDTO;
import com.my.relo.entity.Award;
import com.my.relo.entity.Orders;
import com.my.relo.exception.FindException;
import com.my.relo.repository.OrdersRepository;

@Service
public class OrdersService {
	@Autowired
	private OrdersRepository or;

	// 회원의 주문 목록
	public List<OrdersDTO> getListBymNum(Long mNum) throws FindException {
		try {
			List<Orders> listO = or.findOrdersListBymNum(mNum);
			System.out.println(listO);
			List<OrdersDTO> list = new ArrayList<>();
			for (Orders o : listO) {
				OrdersDTO dto = OrdersDTO.builder().aNum(o.getANum())
						.pNum(o.getAward().getAuction().getProduct().getPNum())
						.sNum(o.getAward().getAuction().getProduct().getStock().getSNum())
						.sColor(o.getAward().getAuction().getProduct().getStock().getSColor())
						.sBrand(o.getAward().getAuction().getProduct().getStock().getSBrand())
						.sName(o.getAward().getAuction().getProduct().getStock().getSName())
						.sGrade(o.getAward().getAuction().getProduct().getStock().getSGrade())
						.sizeCategoryName(
								o.getAward().getAuction().getProduct().getStock().getSizes().getSizeCategoryName())
						.aPrice(o.getAward().getAuction().getAPrice())
						.oDate(o.getODate())
						.dStatus(o.getODelivery().getDStatus()).build();
				list.add(dto);
			}
			return list;
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
	}

	// 회원의 주문 상세
	public OrdersDTO getOrdersByaNum(Long aNum) throws FindException {
		try {
			Optional<Orders> otpO = or.findOrdersByaNum(aNum);
			OrdersDTO dto = new OrdersDTO();
			if (otpO.isPresent()) {
				Orders o = otpO.get();
				dto = OrdersDTO.builder().aNum(o.getANum()).pNum(o.getAward().getAuction().getProduct().getPNum())
						.sNum(o.getAward().getAuction().getProduct().getStock().getSNum())
						.sColor(o.getAward().getAuction().getProduct().getStock().getSColor())
						.sBrand(o.getAward().getAuction().getProduct().getStock().getSBrand())
						.sName(o.getAward().getAuction().getProduct().getStock().getSName())
						.sGrade(o.getAward().getAuction().getProduct().getStock().getSGrade())
						.sizeCategoryName(
								o.getAward().getAuction().getProduct().getStock().getSizes().getSizeCategoryName())
						.aPrice(o.getAward().getAuction().getAPrice()).oDate(o.getODate())
						.dStatus(o.getODelivery().getDStatus()).dTrackingInfo(o.getODelivery().getDTrackingInfo())
						.dCompleteDay(o.getODelivery().getDCompleteDay())
						.addrNum(o.getODelivery().getAddress().getAddrNum())
						.addrRecipient(o.getODelivery().getAddress().getAddrRecipient())
						.addrTel(o.getODelivery().getAddress().getAddrTel())
						.addrPostNum(o.getODelivery().getAddress().getAddrPostNum())
						.addr(o.getODelivery().getAddress().getAddr())
						.addrDetail(o.getODelivery().getAddress().getAddrDetail()).build();

			}
			return dto;
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
	}

	// 관리자 구매확정 목록
	public List<OrdersDTO> getListBydStatus() throws FindException {
		try {
			List<Orders> listO = or.findOrdersConfirmedListBydStatus3();
			List<OrdersDTO> list = new ArrayList<>();
			for (Orders o : listO) {
				OrdersDTO dto = OrdersDTO.builder().aNum(o.getANum())
						.pNum(o.getAward().getAuction().getProduct().getPNum())
						.sNum(o.getAward().getAuction().getProduct().getStock().getSNum())
						.sColor(o.getAward().getAuction().getProduct().getStock().getSColor())
						.sBrand(o.getAward().getAuction().getProduct().getStock().getSBrand())
						.sName(o.getAward().getAuction().getProduct().getStock().getSName())
						.sGrade(o.getAward().getAuction().getProduct().getStock().getSGrade())
						.sizeCategoryName(
								o.getAward().getAuction().getProduct().getStock().getSizes().getSizeCategoryName())
						.aPrice(o.getAward().getAuction().getAPrice()).oDate(o.getODate())
						.dStatus(o.getODelivery().getDStatus()).dCompleteDay(o.getODelivery().getDCompleteDay())
						.build();
				list.add(dto);
			}
			return list;
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
	}

	// 회원의 주문 목록 페이징버전
	public Map<String, Object> getPagingBymNum(Long mNum, int currentPage) throws FindException {
		Pageable sortedByoDateDesc = PageRequest.of(currentPage - 1, 10, Sort.by("oDate").descending());

		Page<Orders> p = or.findOrdersBymNum(mNum, sortedByoDateDesc);
		List<Orders> listO = p.getContent();
		int totalPage = p.getTotalPages();

		List<OrdersDTO> list = new ArrayList<>();
		for (Orders o : listO) {
			OrdersDTO dto = OrdersDTO.builder().aNum(o.getANum())
					.pNum(o.getAward().getAuction().getProduct().getPNum())
					.sNum(o.getAward().getAuction().getProduct().getStock().getSNum())
					.sColor(o.getAward().getAuction().getProduct().getStock().getSColor())
					.sBrand(o.getAward().getAuction().getProduct().getStock().getSBrand())
					.sName(o.getAward().getAuction().getProduct().getStock().getSName())
					.sGrade(o.getAward().getAuction().getProduct().getStock().getSGrade())
					.sizeCategoryName(
							o.getAward().getAuction().getProduct().getStock().getSizes().getSizeCategoryName())
					.aPrice(o.getAward().getAuction().getAPrice())
					.oDate(o.getODate())
					.dStatus(o.getODelivery().getDStatus()).build();
			list.add(dto);
		}
		
		Map<String, Object> res = new HashMap<>();
		res.put("totalPage", totalPage);
		res.put("list", list);
		return res;
	}
	
	// 관리자 구매확정 목록 페이징버전
	public Map<String, Object> getPagingBydStatus(int currentPage) throws FindException {

		Pageable sortedByoDateDesc = PageRequest.of(currentPage - 1, 10, Sort.by("oDate").descending());

		Page<Orders> p = or.findConfirmedBydStatus3(sortedByoDateDesc);
		List<Orders> listO = p.getContent();
		int totalPage = p.getTotalPages();

		List<OrdersDTO> list = new ArrayList<>();
		for (Orders o : listO) {
			OrdersDTO dto = OrdersDTO.builder().aNum(o.getANum())
					.pNum(o.getAward().getAuction().getProduct().getPNum())
					.sNum(o.getAward().getAuction().getProduct().getStock().getSNum())
					.sColor(o.getAward().getAuction().getProduct().getStock().getSColor())
					.sBrand(o.getAward().getAuction().getProduct().getStock().getSBrand())
					.sName(o.getAward().getAuction().getProduct().getStock().getSName())
					.sGrade(o.getAward().getAuction().getProduct().getStock().getSGrade())
					.sizeCategoryName(
							o.getAward().getAuction().getProduct().getStock().getSizes().getSizeCategoryName())
					.aPrice(o.getAward().getAuction().getAPrice()).oDate(o.getODate())
					.dStatus(o.getODelivery().getDStatus()).dCompleteDay(o.getODelivery().getDCompleteDay())
					.build();
			list.add(dto);
		}
		Map<String, Object> res = new HashMap<>();
		res.put("totalPage", totalPage);
		res.put("list", list);
		
		return res;

	}

	public String getToken() throws JsonProcessingException {
		String imp_key = "6142015054040341";
		String imp_secret = "6gWL0QGDs50QbSJ1UwFI1yL7uTAOMSUOXB0AXP62zREl0dlrhhc9EZpalXTpQNQsSBXNEpY6S8tZMjSR";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> map = new HashMap<>();
		map.put("imp_key", imp_key);
		map.put("imp_secret", imp_secret);
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(map);

		try {
			HttpEntity<?> entity = new HttpEntity<>(jsonStr, headers);
			ResponseEntity<Map> responseMap = restTemplate.postForEntity("https://api.iamport.kr/users/getToken",
					entity, Map.class);
			Map response = (Map) (responseMap.getBody().get("response"));

			System.out.println(response.get("access_token"));
			System.out.println(response.get("expired_at"));
			System.out.println(response.get("expired_at").getClass().getName());

			String token = response.get("access_token").toString();
			return token;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public OrderInfoDTO paymentInfo(Object impUid, String accessToken) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
	    HttpsURLConnection conn = null;
	    
	    URL url = new URL("https://api.iamport.kr/payments/" + impUid);
	 
	    conn = (HttpsURLConnection) url.openConnection();
	 
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Authorization", accessToken);
	    conn.setDoOutput(true);
	    
	    
	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	    
	    String content = br.readLine();
	    br.close();
	    conn.disconnect();
	    
	   System.out.println(content);
	   Map<String, Object> map = mapper.readValue(content, Map.class);
	   Map<String, Object> response = (Map)(map.get("response"));
	   
	   Integer amount = (Integer)response.get("amount");
	   Integer applyNum = Integer.parseInt(String.valueOf(response.get("apply_num")));
	   String status = String.valueOf(response.get("status"));
	   OrderInfoDTO oiDto = OrderInfoDTO.builder()
			   .aPrice(amount)
			   .applyNum(applyNum)
			   .status(status)
			   .build();
	   return oiDto;
	}
}
