package com.my.relo.repository;

import static org.junit.jupiter.api.Assumptions.*;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.my.relo.dto.StockDTO;
import com.my.relo.entity.Member;
import com.my.relo.entity.Product;
import com.my.relo.entity.Sizes;
import com.my.relo.entity.Stock;



@SpringBootTest
class StockRepositoryTest {
	@Autowired
	private MemberRepository mr;
	
	@Autowired
	private StockRepository sr;
	
	@Autowired
	private SizesRepository sir;
	
    private Logger logger = LoggerFactory.getLogger(getClass());
	 
	@Test
	//1.판매자 상품등록 
	void StockAdd1Test() {
		Optional<Member> optM1 = mr.findById(1L);
		Member m1 = optM1.get();
		Optional<Sizes> optS1 = sir.findById(120L);
		Sizes si1 = optS1.get();
		Stock s = new Stock();
		s.setSBrand("나이키");
		s.setSColor("흰색");
		s.setSHopeDays(3);
		s.setSName("조던1");
		s.setSOriginPrice(5000);
		s.setMember(m1);
		s.setSellerComment("새제품!");
		s.setSizes(si1);
		s.setSType("신발");
		sr.save(s);
	}
	
	
	@Test
	void StockAdd2Test() {
		Optional<Stock> optS1 = sr.findById(14L);
		Stock s = optS1.get();
		s.setManagerComment("검수결과:정품,특이사항:없음");
		s.setSGrade("S");
		s.setSStatus(2);
		sr.save(s);
	}
	
	@Test
	void StockBulTest() {
		Optional<Stock> optS1 = sr.findById(1L);
		Stock s = optS1.get();
		s.setManagerComment("검수결과:가품");
		s.setSGrade("불");
		s.setSStatus(5);
		sr.save(s);
	}
	
	@Test
	void StockAdd3Test() {
		Optional<Stock> optS1 = sr.findById(14L);
		Stock s = optS1.get();
		s.setSHopePrice(8000);
		s.setSStatus(3);
		sr.save(s);
	}
	
	@Test
	//2.판매자 마이페이지-> 판매내역 -> 판매대기
	void selectByIdTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		
		List<Object[]> sList = sr.selectById(m1.getMNum());
		
		List<StockDTO> list = new ArrayList<>();
		for (Object[] obj : sList) {
			StockDTO dto = StockDTO.builder()
			.sNum(Long.valueOf(String.valueOf(obj[0])))
			.sName(String.valueOf(obj[1]))
			.sizeCategoryName(String.valueOf(obj[2]))
			.sStatus(Integer.valueOf(String.valueOf(obj[3])))
			.sGrade(String.valueOf(obj[4]))
			.sBrand(String.valueOf(obj[5]))
			.build();
				
			list.add(dto);
		}
			logger.info(list.toString());
	}
		
		
		
	
	
	@Test
	//2.판매자 마이페이지-> 판매내역 -> 판매대기 상세
	void selectByIdDeatilTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		
		Optional<Stock> optS1 = sr.findById(2L);
		Stock s = optS1.get();
		
		List<Object[]> sList = sr.selectByIdDeatil(s.getSNum(),m1.getMNum());
		
		List<StockDTO> list = new ArrayList<>();
		for (Object[] obj : sList) {
			StockDTO dto = StockDTO.builder()
			.sNum(Long.valueOf(String.valueOf(obj[0])))
			.mNum(Long.valueOf(String.valueOf(obj[1])))
			.sName(String.valueOf(obj[2]))
			.sType(String.valueOf(obj[3]))
			.sizeCategoryName(String.valueOf(obj[4]))
			.sColor(String.valueOf(obj[5]))
			.managerComment(String.valueOf(obj[6]))
			.sHopeDays(Integer.valueOf(String.valueOf(obj[7])))
			.sOriginPrice(Integer.valueOf(String.valueOf(obj[8])))
			.sBrand(String.valueOf(obj[9]))
			.sStatus(Integer.valueOf(String.valueOf(obj[10])))
			.sGrade(String.valueOf(obj[11]))
			.build();
				
			list.add(dto);
		}
			logger.info(list.toString());
		
	}
	
	
	@Test
	//3. 관리자 상품등록 승인요청 목록 sStatus =2 AND 관리자 상품 최종 등록 목록  sStatus =3
	void selectBySReturnTest() {
		Iterable<Stock> all = sr.findAll();
		all.forEach((s)->{
			Pageable pageable = PageRequest.of(0,5,Sort.by("s_num"));  //5개씩 페이징
			List<Object[]> sList = sr.selectBySReturn(3,pageable);
			List<StockDTO> list = new ArrayList<>();
			for (Object[] obj : sList) {
				StockDTO dto = StockDTO.builder()
				.sNum(Long.valueOf(String.valueOf(obj[0])))
				.sName(String.valueOf(obj[1]))
				.sizeCategoryName(String.valueOf(obj[2]))
				.sColor(String.valueOf(obj[3]))
				.mNum(Long.valueOf(String.valueOf(obj[4])))
				.build();
					
				list.add(dto);
			}
				logger.info(list.toString());
		});
		
	}
	
	@Test
	//3. 관리자 상품등록 승인요청 목록 상세 AND 관리자 상품 최종 등록 목록 상세
	void selectBySNumTest() {
		Optional<Stock> optS1 = sr.findById(2L);
		Stock s = optS1.get();
		logger.info("===========================");
		logger.info("SizeCategoryName: " + s.getSizes().getSizeCategoryName());
		logger.info("SBrand: " + s.getSBrand());
		logger.info("SName: " + s.getSName());
		logger.info("SColor: " + s.getSColor());
		logger.info("SHopePrice: " + s.getSHopePrice());
		logger.info("SHopeDays: " + s.getSHopeDays());
		logger.info("SGrade: " + s.getSGrade());
		logger.info("ManagerComment: " + s.getManagerComment());
		logger.info("===========================");
	}
	
	
	
	
	@Test
	void updateByCancleSStatus5Test() {
		Optional<Stock> optS1 = sr.findById(3L);
		Stock s = optS1.get();
		s.setSStatus(5);
		sr.save(s);
	}
	


}
