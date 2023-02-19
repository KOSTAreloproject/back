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
		Optional<Member> optM1 = mr.findById(1L);
		Member m1 = optM1.get();
		Address a1 = new Address();
		a1.setMember(m1);
		a1.setAddrPostNum("17091");
		a1.setAddrName("이현민");
		a1.setAddrTel("111-111");
		a1.setAddr("경기도 용인시 처인구");
		a1.setAddrDetail("중부대로 1144");
		a1.setAddrRecipient("우리집");
		ar.save(a1);
		
		Optional<Member> optM2 = mr.findById(2L);
		Member m2 = optM2.get();
		Address a2 = new Address();
		a2.setMember(m2);
		a2.setAddrPostNum("17092");
		a2.setAddrName("이로운");
		a2.setAddrTel("222-222");
		a2.setAddr("경기도 용인시 기흥구");
		a2.setAddrDetail("용구대로 2222");
		a2.setAddrRecipient("회사");
		ar.save(a2);
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
		ar.deleteById(1L);
	}
}
