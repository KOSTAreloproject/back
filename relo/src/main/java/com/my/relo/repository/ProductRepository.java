package com.my.relo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.my.relo.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

	// @Modifying insert, update, delete
	// @Transactional update, delete
	

	// 판매자 판매내역 진행중 페이지

	//@Modifying    insert, update, delete
		//@Transactional      update, delete
		
		//판매자 판매내역 진행중 페이지

		@Query(value = "select p.S_NAME,s.SIZE_CATEGORY_NAME,p.P_STATUS,p.P_NUM,p.S_BRAND,p.S_NUM \r\n"
				+ "from P_INFO p INNER JOIN sizes s on p.size_category_num = s.size_category_num\r\n"
				+ "where p.M_NUM = :mNum"
				, countQuery = "select count(*) from stock", nativeQuery = true)
		public Page<Object[]> selectByIdProduct(@Param("mNum")Long mNum,Pageable pageable);
		
		
		//판매자 판매내역 진행중 페이지 (경매참여내역 x)
		@Query(value = "select p.s_name,s.SIZE_CATEGORY_NAME,p.P_STATUS,P_END_DATE,p.s_hope_price,p.s_grade,p.s_brand,p.s_num\r\n"
				+ "from p_info p INNER JOIN sizes s on p.size_category_num = s.size_category_num\r\n"
				+ "where p.M_NUM=:mNum and p.p_num=:pNum"
		,nativeQuery = true)
		public List<Object[]> selectByIdProductDetail(@Param("mNum")Long mNum,@Param("pNum")Long pNum);
		
		//판매자 판매내역 진행중 페이지 (경매참여내역 o)
		@Query(value = "select p.s_name,s.SIZE_CATEGORY_NAME,p.P_STATUS,p.P_END_DATE,a.MAX_PRICE,p.s_hope_price,p.s_grade,p.s_brand,p.s_num\r\n"
				+ "from p_info p INNER JOIN a_max a on p.p_num = a.p_num  INNER JOIN sizes s on p.size_category_num = s.size_category_num\r\n"
				+ "where p.M_NUM=:mNum and p.p_num=:pNum"
		,nativeQuery = true)
		public List<Object[]> selectByIdProductDetail2(@Param("mNum")Long mNum,@Param("pNum")Long pNum);
			
		//판매자 판매내역 종료 페이지
		@Query(value = "select p.s_name,s.SIZE_CATEGORY_NAME,p.P_STATUS,p.p_num,p.s_brand,p.S_NUM,p.P_End_Date\r\n"
				+ "from p_info p INNER JOIN sizes s on p.size_category_num = s.size_category_num\r\n"
				+ "where p.M_NUM=:mNum and (p.p_status = 6 or p.p_status = 8 or p.p_status = 7 or p.p_status = 9)"
				, countQuery = "select count(*) from stock", nativeQuery = true)
		public Page<Object[]> selectByEndProduct(@Param("mNum")Long mNum,Pageable pageable);
		
		//판매자 판매내역 종료 페이지 디테일 계좌는 account
		@Query(value = "select p.s_name,s.SIZE_CATEGORY_NAME,p.p_status,p.p_end_date,p.p_num,p.s_brand,a.max_price,p.s_num\r\n"
				+ "from p_info p INNER JOIN a_max a on p.p_num = a.p_num  INNER JOIN sizes s on p.size_category_num = s.size_category_num\r\n"
				+ "where p.M_NUM=:mNum"
		,nativeQuery = true)
		public List<Object[]> selectByEndProductDetail(@Param("mNum")Long mNum);
		
		// SHOP -> 상품 클릭 시 상품 상세 - 상품 정보
		   @Query(value = "SELECT p.p_num, s.s_num, s.m_num, s_brand, s_name, s_type, s_color, s_grade, s_origin_price, s_hope_price, nvl(a.max_price,0) as max_price, sz.size_category_name, manager_comment, p.p_end_date\r\n"
		         + "FROM product p\r\n" + "JOIN stock s ON p.s_num = s.s_num\r\n"
		         + "JOIN sizes sz ON s.size_category_num = sz.size_category_num\r\n"
		         + "LEFT OUTER JOIN (SELECT p_num, max(a_price) as max_price FROM auction GROUP BY p_num) a ON p.p_num = a.p_num\r\n"
		         + "WHERE p_status = 4 AND p.p_num = :pNum", nativeQuery = true)
		   public List<Object[]> selectShopProductDetail(@Param("pNum") Long pNum);

		   // SHOP -> 상품 클릭 시 상품 상세 - 최근 입찰 내역
		   @Query(value = "SELECT * FROM (SELECT rownum rn, t.* FROM\r\n"
		         + "         (SELECT a.p_num, a.m_num, m.id, a_price, a_date FROM auction a \r\n"
		         + "            JOIN product p ON a.p_num = p.p_num \r\n"
		         + "         JOIN stock s ON p.s_num = s.s_num\r\n" + "            JOIN member m ON a.m_num = m.m_num\r\n"
		         + "         WHERE a.p_num = :pNum \r\n"
		         + "            ORDER BY a_price DESC ) t )WHERE rn BETWEEN 1 AND 20", nativeQuery = true)

		   public List<Object[]> recentTenderList(@Param("pNum") Long pNum);

		   // 검색모달창에서 상품검색시 상품 정보 표시
		   @Query(value = "SELECT p.p_num, s.s_num, s.s_name, s.s_hope_price, nvl(a.max_price,0), p.p_end_date,"
		         + "s.s_type, nvl(z.zcount,0) FROM product p JOIN stock s ON p.s_num = s.s_num LEFT OUTER JOIN (SELECT p_num, MAX(a_price) AS max_price "
		         + "FROM auction GROUP BY p_num) a ON p.p_num = a.p_num LEFT OUTER JOIN (SELECT p_num, COUNT(*) zcount FROM zzim GROUP BY p_num) z "
		         + "ON p.p_num = z.p_num WHERE p_status = 4 AND s_name Like %:keyword% ORDER BY p_end_date", nativeQuery = true)
		   Page<Object[]> selectProductListByName(@Param("keyword") String keyword, Pageable pageable);}
