package com.my.relo.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.dto.AddressDTO;
import com.my.relo.entity.Member;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.MemberRepository;
@SpringBootTest
class AddressTest {

 	@Autowired
 	private AddressService as;

	@Autowired
	private MemberRepository mr;

	Logger log = LoggerFactory.getLogger(getClass());

	@DisplayName("새주소 추가 테스트")
	@Test
	void addressSavetest() throws AddException {
		Optional<Member> optM1 = mr.findById(2L);
		Member m = optM1.get();
		AddressDTO addr = AddressDTO.builder().addr("경기도 용인시").addrDetail("처인구 중부대로").addrName("이현민")
											.addrPostNum("12091").addrRecipient("우리집").addrTel("1111-111")
											.addrType(1).mNum(m.getMNum()).build();
		as.write(addr);
	}
	@DisplayName("새 주소 추가 할때 기본주소가 있을경우 원래 있던 기본주소 타입변경 해주고 테스트")
	@Test
	void addressFindTypeSaveTest() throws AddException {
		Optional<Member> optM1 = mr.findById(2L);
		Member m = optM1.get();
		AddressDTO addr = AddressDTO.builder().addr("경기도 용인시").addrDetail("처인구 중부대로").addrName("이현민")
				.addrPostNum("12091").addrRecipient("우리집").addrTel("22222-111")
				.addrType(0).mNum(m.getMNum()).build();
		as.write(addr);
	}

	@DisplayName("해당 주소번호에 맞는 주소 출력 테스트")
	@Test
	void addressFindByAddrNumTest() throws FindException {
		AddressDTO addrDTO = as.findByAddrNum(11L);
		log.info("주소 : "+addrDTO);
	}

	@DisplayName("로그인한 멤버번호에 맞는 주소록 출력 테스트")
	@Test
	void addrListFindByMNumTest() throws FindException{
		List<AddressDTO> addrList = as.findByMNum(1L);
		for(AddressDTO a : addrList) {
			log.info("주소 : "+a.getAddr());
		}
	}

	@DisplayName("주소 수정 테스트")
	@Test
	void addrUpdateTest() throws FindException, AddException {
		AddressDTO addr = as.findByAddrNum(9L);
		addr.setAddrName("바끼렴");
		as.write(addr);
	}

	@DisplayName("주소 삭제 테스트")
	@Test
	void addrDeleteTest() throws RemoveException {
		as.deleteByAddrNum(18L);
	}
}
