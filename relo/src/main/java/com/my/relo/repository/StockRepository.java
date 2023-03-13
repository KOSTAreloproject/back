package com.my.relo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.my.relo.entity.Stock;

public interface StockRepository extends CrudRepository<Stock, Long> {
	
	//2.판매자 마이페이지-> 판매내역 -> 판매대기
	@Query(value = "select s.s_num,s.s_name,si.SIZE_CATEGORY_NAME ,s.s_status,s.s_grade,s.s_brand\r\n"
			+ "from stock s INNER JOIN sizes si on s.size_category_num = si.size_category_num\r\n"
			+ "where s.M_NUM=:mNum and (s.S_STATUS = 1 or s.S_STATUS = 2 or s.S_STATUS = 3 or s.S_STATUS = 5)"
			, countQuery = "select count(*) from stock where M_NUM=:mNum and (S_STATUS = 1 or S_STATUS = 2 or S_STATUS = 3 or s.S_STATUS = 5)", nativeQuery = true) 
	public Page<Object[]> selectById(@Param("mNum")Long mNum,Pageable pageable);
	
	
	//3. 관리자 상품등록 승인요청 목록 AND 관리자 상품 최종 등록 목록
	@Query(value = "select s.s_num,s.s_name,si.size_category_name ,s.s_color,s.m_num\r\n"
			+ "from stock s INNER JOIN sizes si on s.size_category_num = si.size_category_num\r\n"
			+ "where s.s_status =:sStatus and s_num not in(select s_num from stock_return) "
	, countQuery = "select count(*) from stock where s_status =:sStatus and s_num not in(select s_num from stock_return) ", nativeQuery = true)
	public Page<Object[]> selectBySReturn(@Param("sStatus")Integer sStatus,Pageable pageable);
	
	//2.판매자 마이페이지-> 판매내역 -> 판매대기 상세
	@Query(value = "select s.s_num,s.m_num ,s.s_name,s.s_type,si.SIZE_CATEGORY_NAME ,s.s_color,s.manager_comment,s.s_hope_days,s.s_origin_price,s.s_brand,s.s_status,s.s_grade,s.s_hope_price\r\n"
			+ "from stock s INNER JOIN sizes si on s.size_category_num = si.size_category_num\r\n"
			+ "where s.s_num=:sNum and s.M_NUM=:mNum"
	, nativeQuery = true)
	public List<Object[]> selectByIdDetail(@Param("sNum")Long sNum,@Param("mNum")Long mNum);
	
	

	
	

}
