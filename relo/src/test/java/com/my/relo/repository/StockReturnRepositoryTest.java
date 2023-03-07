package com.my.relo.repository;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.entity.Address;

import com.my.relo.entity.Stock;
import com.my.relo.entity.StockReturn;
@SpringBootTest
class StockReturnRepositoryTest {

	@Autowired
	private StockRepository sr;
		
	@Autowired
	private StockReturnRepository srr;
	
	@Autowired
	private AddressRepository ar;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	void StockReturnAddTest() {
		
		Optional<Address> optA1 = ar.findById(1L);
		Address a1 = optA1.get();
		
		Optional<Stock> optS1 = sr.findById(3L);
		Stock s1 = optS1.get();
		
		UUID u = UUID.randomUUID();
		
		
		StockReturn stockreturn = StockReturn.builder()
		.sNum(s1.getSNum())
		.s(s1)
		.addr(a1)
		.srTrackingInfo(String.valueOf(u))
		.build();
		
		
		srr.save(stockreturn);	
	}
	
	@Test
	void StockReturnDelTest() {	
		Optional<Stock> optS1 = sr.findById(3L);
		Stock s1 = optS1.get();

		srr.deleteById(s1.getSNum());
	}
	
	@Test
	void findByIdTest() {	
		Optional<StockReturn> optSr1 = srr.findById(3L);
		StockReturn sr1 = optSr1.get();
		
		Address adrr = sr1.getAddr();
		Optional<Address> optA1 = ar.findById(adrr.getAddrNum());
		Address a1 = optA1.get();

		logger.info("SNum: " + sr1.getSNum());
		logger.info("StdTrackingInfo: " + sr1.getSrTrackingInfo());
		logger.info("StdStatus: " + sr1.getSrStatus());
		logger.info("Addr: " + a1.getAddrName());
		logger.info("StdStartDate: " + sr1.getSrStartDate());
	}

}
