package com.my.relo.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.dto.ZzimDTO;
import com.my.relo.entity.Zzim;
import com.my.relo.entity.ZzimEmbedded;
import com.my.relo.exception.FindException;
import com.my.relo.service.ZzimService;

@SpringBootTest
class ZzimRepositoryTest {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ZzimRepository zr;
	@Autowired
	ZzimService zs;

//	@Test
//	void test() {
//		ZzimEmbedded ze = ZzimEmbedded.builder().mNum(1L).build();
//
//		Optional<Zzim> optZ = zr.findById(ze);
//		Zzim z = optZ.get();
//		Product p = z.getProduct();
//		List<Auction> auctions = p.getAuction();
//		Stock s = p.getStock();
//		Sizes sz = s.getSizes();
//
//		List<Integer> prices = new ArrayList<>();
//		for (Auction a : auctions) {
//			if (p.getPNum() == a.getProduct().getPNum()) {
//				prices.add(a.getAPrice());
//			}
//		}
//		Integer maxPirce = Collections.max(prices);
//
//		ZzimDTO dto = ZzimDTO.builder().mNum(z.getMember().getMNum()).pNum(p.getPNum()).sName(s.getSName())
//				.sColor(s.getSColor()).sBrand(s.getSBrand()).sType(s.getSType()).pEndDate(p.getPEndDate())
//				.hopePrice(s.getSHopePrice()).sizeCategoryName(sz.getSizeCategoryName()).maxPrice(maxPirce).build();
//		logger.error("dto : " + dto);
//	}

	@Test
	void TestZzimlist() {
		Long mNum = 1L;
		List<ZzimDTO> list;
		try {
			list = zs.readZzimList(mNum);
			for (int i = 0; i < list.size(); i++) {
				System.out.println("list " + list);
			}
		} catch (FindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	void TestZziminsert() {
		Long mNum = 1L;
		Long pnum = 1L;
		ZzimEmbedded ze = ZzimEmbedded.builder().mNum(mNum).pNum(pnum).build();
		Optional<Zzim> optZ = zr.findById(ze);
		if (!optZ.isPresent())
			zr.insertZzim(mNum, pnum);
	}

	@Test
	void TestZimDel() {
		Long mNum = 1L;
		Long pNum = 1L;
		zr.deleteZzim(mNum, pNum);

	}

	@Test
	void Testzzimdelall() {
		Long pNum = 1L;
		zr.deleteZzimProd(pNum);
	}
}