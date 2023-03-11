package com.my.relo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.my.relo.entity.Award;

public interface AwardRepository extends CrudRepository<Award, Long> {
	
	/**
	 * 식별관계 id 기준으로 검색 select SQL
	 */
	Optional<Award> findByaNum(Long aNum);
	
	/**
	 * 식별관계 id 기준으로 삭제 delete SQL
	 */
	@Transactional
	void deleteByaNum(Long aNum);
	/**
	 * 관리자가 보는 낙찰상품 목록 select SQL
	 * 상품번호, 브랜드명, 상품명, 사이즈, 등급, 경매마감일
	 *  
	 * @return List<Award> 
	 */
	@Query(value="select p.p_num, a.a_num, aw.a_time, a.m_num, a.a_price, s.s_brand, s.s_name, sz.size_category_name, p.p_end_date\r\n"
			+ "from product p, award aw, auction a, stock s, sizes sz\r\n"
			+ "where p.p_status =6\r\n"
			+ "and aw.a_num=a.a_num\r\n"
			+ "and a.p_num=p.p_num\r\n"
			+ "and p.s_num = s.s_num\r\n"
			+ "and s.size_category_num=sz.size_category_num\r\n"
			+ "order by aw.a_time desc", nativeQuery = true)
	List<Award> findAwardList();
	
	/**
	 * 관리자가 보는 낙찰상품 목록 페이징 버전 select SQL
	 * 상품번호, 브랜드명, 상품명, 사이즈, 등급, 경매마감일
	 *  
	 * @return List<Award> 
	 */
	@Query(value="select * from (\r\n"
			+ "	    select rownum rn, a.* \r\n"
			+ "	    from (\r\n"
			+ "			select p.p_num, a.a_num, a.m_num, a.a_price, s.s_brand, s.s_name, sz.size_category_name, p.p_end_date\r\n"
			+ "from award aw, auction a, product p, stock s, sizes sz\r\n"
			+ "where p.p_status = 6 \r\n"
			+ "and aw.a_num=a.a_num\r\n"
			+ "and a.p_num=p.p_num\r\n"
			+ "and p.s_num = s.s_num\r\n"
			+ "and s.size_category_num=sz.size_category_num\r\n"
			+ "order by p.p_end_date desc\r\n"
			+ "	    ) \r\n"
			+ "	a) \r\n"
			+ "where rn between :startRow and :endRow", nativeQuery = true)
	List<Award> findAwardPagingList(@Param("startRow") Integer startRow, @Param("endRow") Integer endRow);

	/**
	 * 관리자가 보는 낙찰상품 목록 select SQL 페이징버전
	 * 상품번호, 브랜드명, 상품명, 사이즈, 등급, 경매마감일
	 *  
	 * @return List<Award> 
	 */
	@Query(value="select p.p_num, a.a_num, aw.a_time, a.m_num, a.a_price, s.s_brand, s.s_name, sz.size_category_name, p.p_end_date\r\n"
			+ "from award aw, auction a, product p, stock s, sizes sz\r\n"
			+ "where p.p_status = 6\r\n"
			+ "and aw.a_num=a.a_num\r\n"
			+ "and a.p_num=p.p_num\r\n"
			+ "and p.s_num = s.s_num\r\n"
			+ "and s.size_category_num=sz.size_category_num\r\n"
			+ "order by aw.a_time desc", 
			countQuery = "select count(*)\r\n"
					+ "from award aw, auction a, product p, stock s, sizes sz\r\n"
					+ "where p.p_status = 6\r\n"
					+ "and aw.a_num=a.a_num\r\n"
					+ "and a.p_num=p.p_num\r\n"
					+ "and p.s_num = s.s_num\r\n"
					+ "and s.size_category_num=sz.size_category_num", nativeQuery = true)
	Page<Award> findList(Pageable pageable);
}
