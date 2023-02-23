package com.my.relo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.my.relo.entity.Zzim;
import com.my.relo.entity.ZzimEmbedded;

public interface ZzimRepository extends JpaRepository<Zzim, ZzimEmbedded> {
	/// JPQL , 일반 SQL이 아닌 JPQL문법으로 작성
	// SELECT 컬럼명도 DB의 테이블명이 아닌 엔터티의 멤버변수 명으로 적음
	// FROM 엔터티명, DB의 테이블명이 아님, @Entity에 name이 지정되어 있지 않다면 클래스 이름이 자동 지정됨
	// 대소문자 구분 : 엔터티명과 속성명은 대소문자를 구분한다 하지만 JPQL문법 키워드는 대소문자를 구분하지 않는다(SELECT, select
	/// 특수기호 * 사용못함 엔터티명을 써주면 전체검색이 가능함 -> 엔터티에 별칭을 주고 별칭으로 전체검색
	// nativeQuery = true 속성을 주면 일반SQL쿼리문을 사용할 수 있음
	// NativeSQL 사용하기 : 반환형을 List타입으로 선언할수 없고 배열(Object[])형으로 선언해야함
	/**
	 * 나의 찜목록을 본다
	 * 
	 * @param mNum : 회원번호
	 * @return 나의 찜목록
	 */
	@Query(value = "SELECT z.m_num, z.p_num, s_brand, s_name, s_type, sz.size_category_name, s_color, s_hope_price, s_grade, p_end_date, a.max_price\r\n"
			+ "FROM product p\r\n" + "JOIN zzim z ON p.p_num = z.p_num\r\n" + "JOIN stock s ON p.s_num = s.s_num\r\n"
			+ "JOIN sizes sz ON s.size_category_num = sz.size_category_num\r\n"
			+ "LEFT JOIN (SELECT p_num, max(a_price) as max_price FROM auction GROUP BY p_num) a on p.p_num = a.p_num\r\n"
			+ "WHERE z.m_num = :mNum\r\n" + "ORDER BY p_end_date", nativeQuery = true)
	List<Object[]> findByMNum(@Param("mNum") Long mNum);

	/**
	 * 나의 찜목록에 추가한다.
	 * @param mNum : 회원번호
	 * @param pNum : 상품번호
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into zzim(m_num, p_num) values(:mNum,:pNum)", nativeQuery = true)
	void insertZzim(@Param("mNum") Long mNum, @Param("pNum") Long pNum);

	/**
	 * 나의 찜목록에서 삭제한다.
	 * @param mNum : 회원번호
	 * @param pNum : 상품번호
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from zzim where m_num =:mNum and p_num=:pNum", nativeQuery = true)
	void deleteZzim(@Param("mNum") Long mNum, @Param("pNum") Long pNum);

}
