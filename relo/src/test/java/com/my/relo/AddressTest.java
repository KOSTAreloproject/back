package com.my.relo;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.entity.Address;
import com.my.relo.entity.Member;
import com.my.relo.repository.AddressRepository;
import com.my.relo.repository.MemberRepository;
@SpringBootTest
class AddressTest {
	@Autowired
	private AddressRepository ar;
	
	@Autowired
	private MemberRepository mr;
	
	Logger log = LoggerFactory.getLogger(getClass());
	@DisplayName("주소 추가 테스트")
	@Test
	void testAddrSave() {
		Optional<Member> optM = mr.findById(1L);
		Member m = optM.get();
		Address a = new Address();
		a.setMember(m);
		a.setAddrPostNum("17092");
		a.setAddrName("이현민");
		a.setAddrTel("111-111");
		a.setAddr("경기도 용인시 처인구");
		a.setAddrDetail("중부대로 1144");
		a.setAddrRecipient("우리집");
		ar.save(a);
	}
	@DisplayName("해당 아이디에 맞는 주소리스트 찾기 테스트")
	@Test
	void testAddrFind() {
		List<Address> addrList= ar.findBymNum(1L);
		for(Address a : addrList) {
			log.info("주소 : "+ a.getAddr());
		}
	}
	@DisplayName("선택 주소 수정 테스트")
	@Test
	void testAddrUpdate() {
		Optional<Address> optA = ar.findById(1L);
		Address a = optA.get();
		a.setAddrType(0);
		a.setAddrName("본인");
		ar.save(a);
	}
	@DisplayName("선택 주소 삭제 테스트")
	@Test
	void testAddrDelete() {
		ar.deleteById(2L);
	}
}
