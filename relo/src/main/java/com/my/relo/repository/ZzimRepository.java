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
	 * 
	 * @param mNum : 회원번호
	 * @param pNum : 상품번호
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into zzim(m_num, p_num) values(:mNum,:pNum)", nativeQuery = true)
	void insertZzim(@Param("mNum") Long mNum, @Param("pNum") Long pNum);

	/**
	 * 나의 찜목록에서 삭제한다.
	 * 
	 * @param mNum : 회원번호
	 * @param pNum : 상품번호
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from zzim where m_num =:mNum and p_num=:pNum", nativeQuery = true)
	void deleteZzim(@Param("mNum") Long mNum, @Param("pNum") Long pNum);

	/**
	 * 상품이 삭제되었을 때 모든 회원에게서 찜을 삭제한다
	 * 
	 * @param pNum : 상품번호
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from zzim where p_num=:pNum", nativeQuery = true)
	void deleteZzimProd(@Param("pNum") Long pNum);

}
