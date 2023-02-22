package com.my.relo.repository;

import static org.junit.jupiter.api.Assumptions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
		Optional<Sizes> optS1 = sir.findById(126L);
		Sizes si1 = optS1.get();
		Stock s = new Stock();
		s.setSNum(1L);
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
		Optional<Stock> optS1 = sr.findById(2L);
		Stock s = optS1.get();
		s.setManagerComment("검수결과:정품,특이사항:없음");
		s.setSGrade("S");
		sr.save(s);
	}
	
	@Test
	void StockBulTest() {
		Optional<Stock> optS1 = sr.findById(1L);
		Stock s = optS1.get();
		s.setManagerComment("검수결과:가품");
		s.setSGrade("불");
		sr.save(s);
		updateByCancleSStatus5Test();
	}
	
	@Test
	void StockAdd3Test() {
		Optional<Stock> optS1 = sr.findById(2L);
		Stock s = optS1.get();
		s.setSHopePrice(8000);
		sr.save(s);
	}
	
	@Test
	//2.판매자 마이페이지-> 판매내역 -> 판매대기
	void selectByIdTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		
		List<Object[]> sList = sr.selectById(m1.getMNum());
		for(int i=0 ;i<sList.size();i++) {
			logger.info("====================");
			for(int j=0;j<=5;j++) {
				logger.info("sList: " + sList.get(i)[j]);			
			}
		}
		logger.info("====================");
		
		
		//부적합한 열이름 오류
//		List<Stock> sList = sr.selectById2(m1.getMNum());
//		for(Stock s : sList) {
//			logger.error("====================");
//			logger.error("Stock: " + s.getSNum());
//			logger.error("Stock: " + s.getSBrand());
//			logger.error("====================");
//		}
		
	}
	
	@Test
	//2.판매자 마이페이지-> 판매내역 -> 판매대기 상세
	void selectByIdDeatilTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		
		Optional<Stock> optS1 = sr.findById(2L);
		Stock s = optS1.get();
		
		List<Object[]> sList = sr.selectByIdDeatil(s.getSNum(),m1.getMNum());
		for(int i=0 ;i<sList.size();i++) {
			logger.info("====================");
			for(int j=0;j<=11;j++) {
				logger.info("sList: " + sList.get(i)[j]);			
			}
		}
		logger.info("====================");
		
	}
	
	
	@Test
	//3. 관리자 상품등록 승인요청 목록 sStatus =2 AND 관리자 상품 최종 등록 목록  sStatus =3
	void selectBySReturnTest() {
		Iterable<Stock> all = sr.findAll();
		all.forEach((s)->{
			Pageable pageable = PageRequest.of(0,3);  //3개씩 페이징
			List<Object[]> sList = sr.selectBySReturn(3,pageable);
			for(int i=0 ;i<sList.size();i++) {
				logger.info("====================");
				for(int j=0;j<=3;j++) {
					logger.info("sList: " + sList.get(i)[j]);			
				}
			}
			logger.info("====================");
		});
		
	}
	
	@Test
	//3. 관리자 상품등록 승인요청 목록 상세 AND 관리자 상품 최종 등록 목록 상세
	void selectBySNumTest() {
		Optional<Stock> optS1 = sr.findById(1L);
		Stock s = optS1.get();
		logger.info("size: " + s.getSizes());
		logger.info("member: " + s.getMember());  // com.my.relo.entity.Member@441d3ddf
		logger.info("sBrand: " + s.getSBrand());
	}
	
	
	
	
	@Test
	void updateByCancleSStatus5Test() {
		Optional<Stock> optS1 = sr.findById(3L);
		Stock s = optS1.get();
		s.setSStatus(5);
		sr.save(s);
	}
	


}
