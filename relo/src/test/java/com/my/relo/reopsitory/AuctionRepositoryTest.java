package com.my.relo.reopsitory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.dto.AuctionDTO;
import com.my.relo.entity.Auction;
import com.my.relo.entity.Product;
import com.my.relo.repository.AuctionRepository;
import com.my.relo.repository.AwardRepository;
import com.my.relo.repository.ProductRepository;

@SpringBootTest
class AuctionRepositoryTest {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AuctionRepository ar;
	
	@Autowired
	private ProductRepository pr;
	
	@Autowired
	private AwardRepository awr;
	
	@Autowired
	private ModelMapper mapper;
	
	@Test
	void testSave() {
		//상품에 경매 참여 이력 확인
		Optional<Product> otpP = pr.findById(9L);
		Product p = otpP.get();
		
		Optional<Auction> otpA = ar.findBymNumAndProduct(4L, p);
		Auction a = null;
		if (otpA.isPresent()) { //존재하면 auction값 aPrice 수정
			LocalDate d = LocalDate.now();
			a = Auction.builder()
					.aNum(otpA.get().getANum())
					.aPrice(275000)
					.mNum(4L)
					.product(p)
					.aDate(d).build();
		} else { //해당 상품에 경매 참여이력 없을 경우 새로 값 insert
			a = Auction.builder()
					.aPrice(275000)
					.mNum(4L)
					.product(p).build();
		}
		ar.save(a);
	}
	
	@Test
	void testFindMaxPrice() {
		//8번 상품 max price 찾기
		Integer max = ar.findMaxPriceByPNum(9L);
		assertTrue(max==240000);
	}
	
//	@Test
//	void testFind() {
//		//18번 auction 찾기
//		Optional<Auction> otpA = ar.findById(18L);
//		Auction a = otpA.get();
//		assertTrue(a.getAPrice()==220000);
//		assertTrue(a.getMNum()==3L);
//		assertTrue(a.getProduct().getPNum()==9L);
//		System.out.println(a);
//	}
	@Test
	void testFindIngList() {
		//8번 회원 ing list
		List<Auction> listA = ar.findAuctionIngByMNum(8L);
		List<AuctionDTO> list = new ArrayList<>();
		for (Auction a : listA) {
			AuctionDTO dto = AuctionDTO.builder()
					.aNum(a.getANum())
					.aDate(a.getADate())
					.aPrice(a.getAPrice())
					.pNum(a.getProduct().getPNum())
					.sGrade(a.getProduct().getStock().getSGrade())
					.sBrand(a.getProduct().getStock().getSBrand())
					.sName(a.getProduct().getStock().getSName())
					.sizeCategoryName(a.getProduct().getStock().getSizes().getSizeCategoryName())
					.pEndDate(a.getProduct().getPEndDate()).build();
			list.add(dto);
			System.out.println(dto);
		}
	}
	
	@Test
	void testFindEndList2() {
		//8번 회원 ing list
		List<Auction> listA = ar.findAuctionEndByMNum(9L);
		List<AuctionDTO> list = new ArrayList<>();
		for (Auction a : listA) {
			AuctionDTO dto = AuctionDTO.builder()
					.aNum(a.getANum())
					.pNum(a.getProduct().getPNum())
					.pEndDate(a.getProduct().getPEndDate())
					.aTime(a.getAward() != null ? a.getAward().getATime():null)
					.sGrade(a.getProduct().getStock().getSGrade())
					.sBrand(a.getProduct().getStock().getSBrand())
					.sName(a.getProduct().getStock().getSName())
					.sizeCategoryName(a.getProduct().getStock().getSizes().getSizeCategoryName())
					.aPrice(a.getAPrice())
					.pStatus(a.getProduct().getPStatus())
					.aDate(a.getADate())
					.awNum(a.getAward() != null ? a.getAward().getANum():null).build();
			
			list.add(dto);
			System.out.println(dto);
		}
	}
	
	@Test
	void testFindEndList() {
		//8번 회원 end list
		List<Auction> listA = ar.findAuctionEndByMNum(1L);
		List<AuctionDTO> list = new ArrayList<>();
		for (Auction a : listA) {
			AuctionDTO dto = new AuctionDTO();
			if (a.getAward() == null) {
				dto = AuctionDTO.builder()
						.aNum(a.getANum())
						.pNum(a.getProduct().getPNum())
						.pEndDate(a.getProduct().getPEndDate())
						.sGrade(a.getProduct().getStock().getSGrade())
						.sBrand(a.getProduct().getStock().getSBrand())
						.sName(a.getProduct().getStock().getSName())
						.sizeCategoryName(a.getProduct().getStock().getSizes().getSizeCategoryName())
						.aPrice(a.getAPrice())
						.pStatus(a.getProduct().getPStatus())
						.aDate(a.getADate())
						.awNum(null).build();
			} else {
				dto = AuctionDTO.builder()
						.aNum(a.getANum())
						.pNum(a.getProduct().getPNum())
						.pEndDate(a.getProduct().getPEndDate())
						.aTime(a.getAward().getATime())
						.sGrade(a.getProduct().getStock().getSGrade())
						.sBrand(a.getProduct().getStock().getSBrand())
						.sName(a.getProduct().getStock().getSName())
						.sizeCategoryName(a.getProduct().getStock().getSizes().getSizeCategoryName())
						.aPrice(a.getAPrice())
						.pStatus(a.getProduct().getPStatus())
						.aDate(a.getADate())
						.awNum(a.getAward()!=null ? a.getAward().getANum() : 0).build();
			}
			System.out.println(dto);
			list.add(dto);
			
		}
	}
	
}
