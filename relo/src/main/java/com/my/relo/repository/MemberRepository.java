package com.my.relo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.my.relo.entity.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {
	// 탈퇴 여부 확인
	@Query(value="select count(*) from\r\n"
			+ "(select count(*) from auction\r\n"
			+ "where m_num = :mNum\r\n"
			+ "and a_num not in (\r\n"
			+ "select a_num from award\r\n"
			+ ")\r\n"
			+ "UNION\r\n"
			+ "select count(*) from award c, auction a\r\n"
			+ "where\r\n"
			+ "c.a_num = a.a_num\r\n"
			+ "and c.a_num not in (select a_num from orders)\r\n"
			+ "and a.m_num = :mNum\r\n"
			+ "UNION\r\n"
			+ "select count(*)\r\n"
			+ "from orders o, order_delivery od\r\n"
			+ "where o.a_num = od.a_num\r\n"
			+ "and od.d_status not in (3)\r\n"
			+ "and m_num = :mNum\r\n"
			+ "UNION\r\n"
			+ "select count(*)\r\n"
			+ "from stock_return sr, stock s\r\n"
			+ "where sr.s_num = s.s_num\r\n"
			+ "and sr.sr_status not in (3)\r\n"
			+ "and m_num = :mNum\r\n"
			+ "UNION\r\n"
			+ "select count(*)\r\n"
			+ "from stock\r\n"
			+ "where s_status not in (6)\r\n"
			+ "and m_num = :mNum\r\n"
			+ ")", nativeQuery = true)
	Integer checkOutTerms(Long mNum);

	// 아이디 중복 체크
	@Query(value = "select * from member where id=:id", nativeQuery = true)
	Member checkId(String id);
}