package com.my.relo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.AuctionDTO;
import com.my.relo.entity.Auction;
import com.my.relo.entity.Product;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.repository.AuctionRepository;
import com.my.relo.repository.ProductRepository;

@Service
public class AuctionService {
	@Autowired
	private AuctionRepository ar;
	@Autowired
	private ProductRepository pr;
	
	//상품의 최고 입찰가 조회
	public Integer maxPriceByPNum(Long pNum) throws FindException{
		Integer max = ar.findMaxPriceByPNum(pNum);
		return max;
	}

	//회원이 경매 참여할 경우
	public void addAuction(Long pNum, Long mNum, int aPrice) throws AddException {
		try {
			//상품에 경매 참여 이력 확인
			Optional<Product> otpP = pr.findById(pNum);
			Product p = otpP.get();
			
			Optional<Auction> otpA = ar.findBymNumAndProduct(mNum, p);
			Auction a = null;
			if (otpA.isPresent()) { //존재하면 auction값 aPrice 수정
				LocalDate d = LocalDate.now();
				a = Auction.builder()
						.aNum(otpA.get().getANum())
						.aPrice(aPrice)
						.mNum(mNum)
						.product(p)
						.aDate(d).build();
			} else { //해당 상품에 경매 참여이력 없을 경우 새로 값 insert
				a = Auction.builder()
						.aPrice(aPrice)
						.mNum(mNum)
						.product(p).build();
			}
			ar.save(a);
		} catch (Exception e) {
			throw new AddException(e.getMessage());
		}
	}

	//회원의 경매참여 내역 중 경매 진행 중인 항목
	public List<AuctionDTO> getIngListBymNum(Long mNum) throws FindException {
		try {
			List<Auction> listA = ar.findAuctionIngByMNum(mNum);
			List<AuctionDTO> list = new ArrayList<>();
			for (Auction a : listA) {
				AuctionDTO dto = AuctionDTO.builder()
						.aNum(a.getANum())
						.aDate(a.getADate())
						.aPrice(a.getAPrice())
						.pNum(a.getProduct().getPNum())
						.sNum(a.getProduct().getStock().getSNum())
						.sGrade(a.getProduct().getStock().getSGrade())
						.sBrand(a.getProduct().getStock().getSBrand())
						.sColor(a.getProduct().getStock().getSColor())
						.sName(a.getProduct().getStock().getSName())
						.sizeCategoryName(a.getProduct().getStock().getSizes().getSizeCategoryName())
						.pEndDate(a.getProduct().getPEndDate()).build();
				list.add(dto);
			}
			return list;
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
	}
	
	//회원의 경매참여 내역 중 마감일 지난 항목
	public List<AuctionDTO> getEndListBymNum(Long mNum) throws FindException {
		try {
			List<Auction> listA = ar.findAuctionEndByMNum(mNum);
			List<AuctionDTO> list = new ArrayList<>();
			for (Auction a : listA) {
				AuctionDTO dto = AuctionDTO.builder()
						.aNum(a.getANum())
						.pNum(a.getProduct().getPNum())
						.pEndDate(a.getProduct().getPEndDate())
						.aTime(a.getAward() != null ? a.getAward().getATime():null)
						.sNum(a.getProduct().getStock().getSNum())
						.sGrade(a.getProduct().getStock().getSGrade())
						.sColor(a.getProduct().getStock().getSColor())
						.sBrand(a.getProduct().getStock().getSBrand())
						.sName(a.getProduct().getStock().getSName())
						.sizeCategoryName(a.getProduct().getStock().getSizes().getSizeCategoryName())
						.aPrice(a.getAPrice())
						.pStatus(a.getProduct().getPStatus())
						.aDate(a.getADate())
						.awNum(a.getAward() != null ? a.getAward().getANum():null).build();
				
				list.add(dto);
			}
			return list;
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
	}
}