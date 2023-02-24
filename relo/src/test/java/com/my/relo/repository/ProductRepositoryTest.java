package com.my.relo.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.dto.PInfoDTO;
import com.my.relo.entity.Member;
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

	@Autowired
	ModelMapper modelMapper;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	void ProductAddTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();
		Optional<Stock> optS1 = sr.findById(11L);
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

		List<Object[]> pList = pr.selectByIdProduct(m1.getMNum());

		List<PInfoDTO> list = new ArrayList<>();
		for (Object[] obj : pList) {
			PInfoDTO dto = PInfoDTO.builder()
			.sName(String.valueOf(obj[0]))
			.sizeCategoryName(String.valueOf(obj[1]))
			.pStatus(Integer.valueOf(String.valueOf(obj[2])))
			.pNum(Long.valueOf(String.valueOf(obj[3])))
			.sBrand(String.valueOf(obj[4]))
			.build();
				
			list.add(dto);
		}
			logger.info(list.toString());
		}

	

	@Test
	void selectByIdProductDetailTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();

//		logger.info("m: "+m1.getId());

		Optional<Product> optp1 = pr.findById(1L);
		Product p1 = optp1.get();
//		logger.info("p: "+p1.getPStatus());

		List<Object[]> pinfo = pr.selectByIdProductDetail(m1.getMNum(), p1.getPNum());
		List<PInfoDTO> list = new ArrayList<>();
		for (Object[] obj : pinfo) {
			PInfoDTO dto = PInfoDTO.builder()
			.sName(String.valueOf(obj[0]))
			.sizeCategoryName(String.valueOf(obj[1]))
			.pStatus(Integer.valueOf(String.valueOf(obj[2])))
			.pEndDate((Date)obj[3])
			.sHopePrice(Integer.valueOf(String.valueOf(obj[4])))
			.sGrade(String.valueOf(obj[5]))
			.sBrand(String.valueOf(obj[6]))
			.build();
			
			list.add(dto);
		}
		logger.info(list.toString());

//		logger.info("pinfo: " + dto.getPStatus());

	}

	@Test
	void selectByIdProductDetailTest2() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();

		Optional<Product> optp1 = pr.findById(2L);
		Product p1 = optp1.get();

		List<Object[]> pinfo = pr.selectByIdProductDetail2(m1.getMNum(), p1.getPNum());
		List<PInfoDTO> list = new ArrayList<>();
		for (Object[] obj : pinfo) {
			PInfoDTO dto = PInfoDTO.builder()
			.sName(String.valueOf(obj[0]))
			.sizeCategoryName(String.valueOf(obj[1]))
			.pStatus(Integer.valueOf(String.valueOf(obj[2])))
			.pEndDate((Date)obj[3])
			.maxPrice(Long.valueOf(String.valueOf(obj[4])))
			.sHopePrice(Integer.valueOf(String.valueOf(obj[5])))
			.sGrade(String.valueOf(obj[6]))
			.sBrand(String.valueOf(obj[7]))
			.build();
			
			list.add(dto);
		}
		logger.info(list.toString());
		

	}

	@Test
	void selectByEndProductTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();

		List<Object[]> pList = pr.selectByEndProduct(m1.getMNum());

		List<PInfoDTO> list = new ArrayList<>();
		for (Object[] obj : pList) {
			PInfoDTO dto = PInfoDTO.builder()
			.sName(String.valueOf(obj[0]))
			.sizeCategoryName(String.valueOf(obj[1]))
			.pStatus(Integer.valueOf(String.valueOf(obj[2])))
			.pNum(Long.valueOf(String.valueOf(obj[3])))
			.sBrand(String.valueOf(obj[4]))
			.build();
			
			list.add(dto);
		}
		logger.info(list.toString());
		logger.info("====================");
	}

	@Test
	void selectByEndProductDetailTest() {
		Optional<Member> optM1 = mr.findById(2L);
		Member m1 = optM1.get();

		List<Object[]> pinfo = pr.selectByEndProductDetail(m1.getMNum());
		List<PInfoDTO> list = new ArrayList<>();
		for (Object[] obj : pinfo) {
			PInfoDTO dto = PInfoDTO.builder()
			.sName(String.valueOf(obj[0]))
			.sizeCategoryName(String.valueOf(obj[1]))
			.pStatus(Integer.valueOf(String.valueOf(obj[2])))
			.pEndDate((Date)obj[3])
			.pNum(Long.valueOf(String.valueOf(obj[4])))
			.sBrand(String.valueOf(obj[5]))
			.maxPrice(Long.valueOf(String.valueOf(obj[6])))
			.build();
			
			list.add(dto);
		}
		logger.info(list.toString());

	}

}
