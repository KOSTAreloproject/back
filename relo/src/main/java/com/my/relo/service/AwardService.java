package com.my.relo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.AuctionDTO;
import com.my.relo.entity.Award;
import com.my.relo.exception.FindException;
import com.my.relo.repository.AwardRepository;

@Service
public class AwardService {
	@Autowired
	private AwardRepository awr;
	
	//낙찰자가 구매 포기할 경우
	public void delAward(Long aNum) throws FindException {
		try {
			Optional<Award> aw = awr.findByaNum(aNum);
			if (aw.isPresent()) {
				awr.delete(aw.get());
			} else {
				throw new FindException("해당 낙찰내역이 존재하지 않습니다.");
			}
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
	}
	
	//낙찰 상품 목록 관리자에게 출력
	public List<AuctionDTO> getAwardList() throws FindException {
		try {
			List<Award> listAw = awr.findAwardList();
			List<AuctionDTO> list = new ArrayList<>();
			if (listAw.size()==0) {
				throw new FindException("낙찰 상품 목록이 존재하지 않습니다.");
			} else {
				for (Award a : listAw) {
					AuctionDTO dto = AuctionDTO.builder()
							.aNum(a.getAuction().getANum())
							.aDate(a.getAuction().getADate())
							.aTime(a.getATime())
							.awNum(a.getANum())
							.aPrice(a.getAuction().getAPrice())
							.pNum(a.getAuction().getProduct().getPNum())
							.pEndDate(a.getAuction().getProduct().getPEndDate())
							.sNum(a.getAuction().getProduct().getStock().getSNum())
							.sBrand(a.getAuction().getProduct().getStock().getSBrand())
							.sGrade(a.getAuction().getProduct().getStock().getSGrade())
							.sColor(a.getAuction().getProduct().getStock().getSColor())
							.sName(a.getAuction().getProduct().getStock().getSName())
							.sizeCategoryName(a.getAuction().getProduct().getStock().getSizes().getSizeCategoryName())
							.build();
					list.add(dto);
				}
				return list;
			}
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
		
	}
}
