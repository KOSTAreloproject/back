package com.my.relo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.OrdersDTO;
import com.my.relo.entity.Orders;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.repository.OrdersRepository;

@Service
public class OrdersService {
	@Autowired
	private OrdersRepository or;
	
	//회원의 주문 목록
	public List<OrdersDTO> getListBymNum(Long mNum) throws FindException {
		try {
			List<Orders> listO = or.findOrdersListBymNum(mNum);
			System.out.println(listO);
			List<OrdersDTO> list = new ArrayList<>();
			for (Orders o : listO) {
				OrdersDTO dto = OrdersDTO.builder()
						.aNum(o.getANum())
						.pNum(o.getAward().getAuction().getProduct().getPNum())
						.sNum(o.getAward().getAuction().getProduct().getStock().getSNum())
						.sColor(o.getAward().getAuction().getProduct().getStock().getSColor())
						.sBrand(o.getAward().getAuction().getProduct().getStock().getSBrand())
						.sName(o.getAward().getAuction().getProduct().getStock().getSName())
						.sGrade(o.getAward().getAuction().getProduct().getStock().getSGrade())
						.sizeCategoryName(o.getAward().getAuction().getProduct().getStock().getSizes().getSizeCategoryName())
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
	
	//회원의 주문 상세
	public OrdersDTO getOrdersByaNum(Long aNum) throws FindException {
		try {
			Optional<Orders> otpO = or.findOrdersByaNum(aNum);
			OrdersDTO dto = new OrdersDTO();
			if (otpO.isPresent()) {
				Orders o = otpO.get();
				dto = OrdersDTO.builder()
						.aNum(o.getANum())
						.pNum(o.getAward().getAuction().getProduct().getPNum())
						.sNum(o.getAward().getAuction().getProduct().getStock().getSNum())
						.sColor(o.getAward().getAuction().getProduct().getStock().getSColor())
						.sBrand(o.getAward().getAuction().getProduct().getStock().getSBrand())
						.sName(o.getAward().getAuction().getProduct().getStock().getSName())
						.sGrade(o.getAward().getAuction().getProduct().getStock().getSGrade())
						.sizeCategoryName(o.getAward().getAuction().getProduct().getStock().getSizes().getSizeCategoryName())
						.aPrice(o.getAward().getAuction().getAPrice())
						.oDate(o.getODate())
						.dStatus(o.getODelivery().getDStatus())
						.dTrackingInfo(o.getODelivery().getDTrackingInfo())
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
	
	//관리자 구매확정 목록 
	public List<OrdersDTO> getListBypStatus() throws FindException {
		try {
			List<Orders> listO = or.findOrdersConfirmedListBydStatus3();
			List<OrdersDTO> list = new ArrayList<>();
			for (Orders o : listO) {
				OrdersDTO dto = OrdersDTO.builder()
						.aNum(o.getANum())
						.pNum(o.getAward().getAuction().getProduct().getPNum())
						.sNum(o.getAward().getAuction().getProduct().getStock().getSNum())
						.sColor(o.getAward().getAuction().getProduct().getStock().getSColor())
						.sBrand(o.getAward().getAuction().getProduct().getStock().getSBrand())
						.sName(o.getAward().getAuction().getProduct().getStock().getSName())
						.sGrade(o.getAward().getAuction().getProduct().getStock().getSGrade())
						.sizeCategoryName(o.getAward().getAuction().getProduct().getStock().getSizes().getSizeCategoryName())
						.aPrice(o.getAward().getAuction().getAPrice())
						.oDate(o.getODate())
						.dStatus(o.getODelivery().getDStatus())
						.dCompleteDay(o.getODelivery().getDCompleteDay())
						.build();
				list.add(dto);
			}
			return list;
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
	}
	
	
	
}
