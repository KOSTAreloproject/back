package com.my.relo.repository;

import java.time.LocalDate;
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
import com.my.relo.entity.PInfo;
import com.my.relo.entity.Product;
import com.my.relo.entity.Stock;


@SpringBootTest
class ProductRepositoryTest {
	@Autowired
	private MemberRepository mr;

	@Autowired
	private StockRepository sr;


	@Autowired
	private ProductRepository pr;
	

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	void ProductAddTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		Optional<Stock> optS1 = sr.findById(2L);
		Stock s1 = optS1.get();
		
		Product p = new Product();
		
		p.setMNum(m1.getMNum());
		
		Integer sHopeDays = s1.getSHopeDays(); // 일 계산
		LocalDate pEndDate = LocalDate.now().plusDays(sHopeDays);
		p.setPEndDate(pEndDate);
		
		p.setStock(s1);
		
		p.setPStatus(4);
		
		pr.save(p);
	}
	
	@Test
	void selectByIdProductTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		
		List<Object[]> pList =  pr.selectByIdProduct(m1.getMNum());
		
		for(int i=0 ;i<pList.size();i++) {
			logger.info("====================");
			for(int j=0;j<5;j++) {
				logger.info(i+1+"번째:" + pList.get(i)[j]);					
			}
		}
		logger.info("====================");
	}
	
	@Test
	//오류남
	void findByPageTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		
		Pageable pageable = PageRequest.of(0,2);
		
		
		List<PInfo> pList =  pr.findByPage(m1.getMNum(),pageable);
		logger.info("pList : " + pList);
		
	}
	
	@Test
	void selectByIdProductDetailTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		
		Optional<Product> optp1 = pr.findById(1L);
		Product p1 = optp1.get();
		
		Object pinfo = pr.selectByIdProductDetail(m1.getMNum(),p1.getPNum());

		logger.info("pinfo: " +pinfo);
		
	}
	
	@Test
	void selectByIdProductDetailTest2() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		
		Optional<Product> optp1 = pr.findById(3L);
		Product p1 = optp1.get();
		
		Object pinfo = pr.selectByIdProductDetail2(m1.getMNum(),p1.getPNum());

		logger.info("pinfo: " +pinfo);
		
	}
	
	@Test
	void selectByEndProductTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		
		List<Object[]> pList =  pr.selectByEndProduct(m1.getMNum());
		
		for(int i=0 ;i<pList.size();i++) {
			logger.info("====================");
			for(int j=0;j<5;j++) {
				logger.info(i+1+"번째:" + pList.get(i)[j]);					
			}
		}
		logger.info("====================");
	}
	
	@Test
	void selectByEndProductDetailTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		
		
		Object pinfo = pr.selectByEndProductDetail(m1.getMNum());

		logger.info("pinfo: " +pinfo);
		
	}
	

}
