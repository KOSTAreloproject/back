package com.my.relo.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.my.relo.entity.Product;



public interface ProductRepository extends CrudRepository<Product, Long> {
	
	//@Modifying    insert, update, delete
	//@Transactional      update, delete
	
	//판매자 판매내역 진행중 페이지

	@Query(value = "select p.S_NAME,s.SIZE_CATEGORY_NAME,p.P_STATUS,p.P_NUM,p.S_BRAND \r\n"
			+ "from P_INFO p INNER JOIN sizes s on p.size_category_num = s.size_category_num\r\n"
			+ "where p.M_NUM = :mNum"
			, countQuery = "select count(*) from stock", nativeQuery = true)
	public Page<Object[]> selectByIdProduct(@Param("mNum")Long mNum,Pageable pageable);
	
	
	//판매자 판매내역 진행중 페이지 (경매참여내역 x)
	@Query(value = "select p.s_name,s.SIZE_CATEGORY_NAME,p.P_STATUS,P_END_DATE,p.s_hope_price,p.s_grade,p.s_brand\r\n"
			+ "from p_info p INNER JOIN sizes s on p.size_category_num = s.size_category_num\r\n"
			+ "where p.M_NUM=:mNum and p.p_num=:pNum"
	,nativeQuery = true)
	public List<Object[]> selectByIdProductDetail(@Param("mNum")Long mNum,@Param("pNum")Long pNum);
	
	//판매자 판매내역 진행중 페이지 (경매참여내역 o)
	@Query(value = "select p.s_name,s.SIZE_CATEGORY_NAME,p.P_STATUS,p.P_END_DATE,a.MAX_PRICE,p.s_hope_price,p.s_grade,p.s_brand\r\n"
			+ "from p_info p INNER JOIN a_max a on p.p_num = a.p_num  INNER JOIN sizes s on p.size_category_num = s.size_category_num\r\n"
			+ "where p.M_NUM=:mNum and p.p_num=:pNum"
	,nativeQuery = true)
	public List<Object[]> selectByIdProductDetail2(@Param("mNum")Long mNum,@Param("pNum")Long pNum);
		
	//판매자 판매내역 종료 페이지
	@Query(value = "select p.s_name,s.SIZE_CATEGORY_NAME,p.P_STATUS,p.p_num,p.s_brand\r\n"
			+ "from p_info p INNER JOIN sizes s on p.size_category_num = s.size_category_num\r\n"
			+ "where p.M_NUM=:mNum and p.p_status = 6 "
			, countQuery = "select count(*) from stock", nativeQuery = true)
	public Page<Object[]> selectByEndProduct(@Param("mNum")Long mNum,Pageable pageable);
	
	//판매자 판매내역 종료 페이지 디테일 계좌는 account
	@Query(value = "select p.s_name,s.SIZE_CATEGORY_NAME,p.p_status,p.p_end_date,p.p_num,p.s_brand,a.max_price\r\n"
			+ "from p_info p INNER JOIN a_max a on p.p_num = a.p_num  INNER JOIN sizes s on p.size_category_num = s.size_category_num\r\n"
			+ "where p.M_NUM=:mNum"
	,nativeQuery = true)
	public List<Object[]> selectByEndProductDetail(@Param("mNum")Long mNum);
}

