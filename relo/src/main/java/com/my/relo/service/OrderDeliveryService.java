package com.my.relo.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.AddressDTO;
import com.my.relo.dto.OrdersDTO;
import com.my.relo.entity.Address;
import com.my.relo.entity.Award;
import com.my.relo.entity.OrderDelivery;
import com.my.relo.entity.Orders;
import com.my.relo.exception.FindException;
import com.my.relo.repository.AddressRepository;
import com.my.relo.repository.AwardRepository;
import com.my.relo.repository.OrderDeliveryRepository;

@Service
public class OrderDeliveryService {
	@Autowired
	private OrderDeliveryRepository odr;

	@Autowired
	private AddressRepository adr;

	@Autowired
	private AwardRepository awr;

	// 주문 완료시 주문/주문배송 추가
	public void addOrderDelivery(Long aNum, AddressDTO adDTO, OrdersDTO odDTO) throws FindException {
		try {
			Optional<Award> otpA = awr.findByaNum(aNum);
			Optional<OrderDelivery> otpOd = odr.findById(aNum);

			if (otpA.isPresent() && !otpOd.isPresent()) {
				Award a = otpA.get();
				Address ad = Address.builder()
						.addrNum(adDTO.getAddrNum())
						.mNum(adDTO.getMNum())
						.addrName(adDTO.getAddrName())
						.addrPostNum(adDTO.getAddrPostNum())
						.addrRecipient(adDTO.getAddrRecipient())
						.addrTel(adDTO.getAddrTel())
						.addrType(adDTO.getAddrType())
						.addr(adDTO.getAddr())
						.addrDetail(adDTO.getAddrDetail()).build();

				Orders o = Orders.builder().aNum(odDTO.getANum())
						.mNum(odDTO.getMNum())
						.award(a)
						.oMemo(odDTO.getOMemo())
						.impUid(odDTO.getImpUid())
						.build();

				OrderDelivery od = OrderDelivery.builder()
						.aNum(a.getANum())
						.address(ad)
						.orders(o)
						.dTrackingInfo(odDTO.getDTrackingInfo()).build();
				odr.save(od);
			} else {
				throw new FindException("해당 낙찰 정보가 존재하지 않음. 혹은 이미 주문완료 상태.");
			}

		} catch (Exception e) {
			throw new FindException("주문배송 추가 실패.");
		}
	}

//	// 주문 주소 수정
//	public void editAddrNum(Long aNum, Long addrNum) throws FindException {
//		try {
//			Optional<OrderDelivery> otpOd = odr.findById(aNum);
//			if (otpOd.isPresent()) {
//				OrderDelivery od = otpOd.get();
//				Optional<Address> otpAd = adr.findById(addrNum);
//				if (!otpAd.isPresent()) {
//					throw new FindException("해당 주소가 존재하지 않습니다.");
//				} else {
//					Address ad = otpAd.get();
//					OrderDelivery res = OrderDelivery.builder().aNum(od.getANum()).address(ad)
//							.dTrackingInfo(od.getDTrackingInfo()).build();
//
//					odr.save(res);
//				}
//			}
//		} catch (Exception e) {
//			throw new FindException("주소 수정 실패.");
//		}
//	}

	// 구매확정 update
	public void editDstatus(Long aNum, Long addrNum) throws FindException {
		try {
			Optional<OrderDelivery> otpOd = odr.findById(aNum);
			Optional<Address> otpAd = adr.findById(addrNum);
			
			System.out.println(otpOd.get().getDStatus()+" 배송상태");
			
			if (otpOd.isPresent() && otpAd.isPresent()) {
				OrderDelivery od = otpOd.get();
				Address ad = otpAd.get();
				od.updateDStatus(3);
				od.updateAddress(ad);
				
				odr.save(od);
			}
		} catch (Exception e) {
			throw new FindException("구매확정 처리 실패.");
		}
	}
}
