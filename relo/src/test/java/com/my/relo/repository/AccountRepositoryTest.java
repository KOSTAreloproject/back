package com.my.relo.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.entity.Account;
import com.my.relo.entity.Member;

@SpringBootTest
class AccountRepositoryTest {

	@Autowired
	private AccountRepository ar;
	@Autowired
	private MemberRepository mr;

	private Logger logger = LoggerFactory.getLogger(getClass());

	// Account - Insert
	@Test
	void testInsertAccount() {
		Long mNum = 2L;
		Optional<Member> optM = mr.findById(mNum);
		if (optM.isPresent()) {
			Optional<Account> optA = ar.findById(optM.get().getMNum());
			if (optA.isEmpty()) { // INSERT
				Account account = new Account();
				account.setMNum(optM.get().getMNum());
				account.setBankCode("123");
				account.setBankAccount("110-365-123423");
				// logger.info("account : " + account);
				ar.save(account);
			} else { // UPDATE
				Account account = optA.get();
				account.setMNum(optM.get().getMNum());
				account.setBankCode("122");
				account.setBankAccount("123-456-123423");
				ar.save(account);
			}
		}
	}
	// Account - FindById

	@Test
	void testFindById() {
		Long mNum = 2L;
		Optional<Member> optM = mr.findById(mNum);
		if (optM.isPresent()) {
			Optional<Account> optA = ar.findById(optM.get().getMNum());
			if (optA.isPresent()) {
				Account account = optA.get();
				logger.info("멤버번호 : " + account.getMNum());
				logger.info("계좌번호 : " + account.getBankAccount());
				logger.info("은행코드 : " + account.getBankCode());
			}
		}
	}

	@Test
	void testDeleteById() {
		Long mNum = 2L;
		Optional<Member> optM = mr.findById(mNum);
		if (optM.isPresent()) {
			ar.deleteById(optM.get().getMNum());
		}
	}
}
