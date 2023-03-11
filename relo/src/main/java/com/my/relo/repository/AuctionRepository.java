package com.my.relo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.my.relo.entity.Auction;
import com.my.relo.entity.Product;

public interface AuctionRepository extends CrudRepository<Auction, Long> {

	/**
	 * 경매 번호에 따른 입찰가 select SQL
	 *  
	 * @return a_price
	 */
	Optional<Auction> findByaNum(Long aNum);
	
	
	/**
	 * 상품의 최고입찰가 select SQL
	 * 상품 최고 입찰가
	 *  
	 * @return a_price
	 */
	@Query(value="select max(a.a_price) as a_price \r\n"
			+ "from auction a,product p\r\n"
			+ "where :pNum = a.p_num\r\n"
			+ "group by a.p_num", nativeQuery = true)
	Integer findMaxPriceByPNum(@Param("pNum") Long pNum);
	
	
	/**
	 * 회원이 해당 상품 경매에 참여한 이력 있는지 select SQL
	 * @param mNum 회원번호 pNum 상품번호
	 * @return Auction 있을 시 경매이력 / 없으면 null
	 */
	Optional<Auction> findBymNumAndProduct(Long mNum, Product product);
	
	/**
	 * 회원의 입찰 내역 (경매 진행 중) select SQL
	 * 상품번호, 브랜드명, 상품명, 사이즈, 등급, 입찰일
	 *  
	 * @return List<Auction> 
	 */
	@Query(value="select a.a_num, p.p_num, a.m_num, a.a_date, s.s_num, s.s_grade, s.s_brand, s.s_name, sz.size_category_name, a.a_price, p.p_end_date\r\n"
			+ "from sizes sz, stock s, product p, auction a\r\n"
			+ "where a.m_num = :mNum \r\n"
			+ "and sz.size_category_num = s.size_category_num\r\n"
			+ "and s.s_num = p.s_num\r\n"
			+ "and p.p_num = a.p_num\r\n"
			+ "and p.p_status = 4\r\n"
			+ "order by p.p_end_date desc", nativeQuery = true)
	List<Auction> findAuctionIngByMNum(@Param("mNum") Long mNum);
	
	/**
	 * 회원의 입찰 내역 (경매 종료) select SQL left join으로 변경
	 * 상품번호, 브랜드명, 상품명, 사이즈, 등급, 입찰일
	 *  
	 * @return List<Auction>
	 */
	@Query(value="select a.m_num, a.a_num, aw.a_time, p.p_num, s.s_grade, s.s_brand, s.s_name, sz.size_category_name, a.a_price, p.p_status, p.p_end_date, a.a_date, aw.a_num as aw_num, a.a_price\r\n"
			+ "from sizes sz, stock s, product p, auction a, award aw\r\n"
			+ "where a.m_num = :mNum \r\n"
			+ "and sz.size_category_num=s.size_category_num\r\n"
			+ "and s.s_num = p.s_num\r\n"
			+ "and p.p_num=a.p_num\r\n"
			+ "and a.a_num=aw.a_num(+)\r\n"
			+ "and p.p_status in (6, 7, 8, 9)\r\n"
			+ "order by p_end_date desc", nativeQuery = true)
	List<Auction> findAuctionEndByMNum(@Param("mNum") Long mNum);
	
	/**
	 * 회원의 입찰 내역 (경매 진행 중) select SQL 페이징 버전
	 * 상품번호, 브랜드명, 상품명, 사이즈, 등급, 입찰일
	 *  
	 * @return List<Auction> 
	 */
	@Query(value="select a.a_num, p.p_num, a.m_num, a.a_date, s.s_num, s.s_grade, s.s_brand, s.s_name, sz.size_category_name, a.a_price, p.p_end_date\r\n"
			+ "from product p, auction a, sizes sz, stock s \r\n"
			+ "where a.m_num = :mNum \r\n"
			+ "and sz.size_category_num = s.size_category_num\r\n"
			+ "and s.s_num = p.s_num\r\n"
			+ "and p.p_num = a.p_num\r\n"
			+ "and p.p_status = 4\r\n"
			+ "order by a.a_date desc",
			countQuery = "select count(*) from auction a, sizes sz, product p, stock s \r\n"
					+ "where a.m_num = :mNum \r\n"
					+ "and sz.size_category_num = s.size_category_num\r\n"
					+ "and s.s_num = p.s_num\r\n"
					+ "and p.p_num = a.p_num\r\n"
					+ "and p.p_status = 4" ,nativeQuery = true)
	Page<Auction> findIngByMNum(@Param("mNum") Long mNum, Pageable pageable);
	
	/**
	 * 회원의 입찰 내역 (경매 종료) 페이징 버전
	 * 상품번호, 브랜드명, 상품명, 사이즈, 등급, 입찰일
	 *  
	 * @return List<Auction>
	 */
	@Query(value="select a.m_num, a.a_num, aw.a_time, p.p_num, s.s_grade, s.s_brand, s.s_name, sz.size_category_name, a.a_price, p.p_status, p.p_end_date, a.a_date, aw.a_num as aw_num\r\n"
			+ "from product p, auction a, sizes sz, stock s, award aw\r\n"
			+ "where a.m_num = :mNum \r\n"
			+ "and sz.size_category_num=s.size_category_num\r\n"
			+ "and s.s_num = p.s_num\r\n"
			+ "and p.p_num=a.p_num\r\n"
			+ "and a.a_num=aw.a_num(+)\r\n"
			+ "and p.p_status in (6, 7, 8, 9)\r\n"
			+ "order by a.a_date desc", 
			countQuery = "select count(*)\r\n"
					+ "from sizes sz, stock s, product p, auction a, award aw\r\n"
					+ "where a.m_num = :mNum \r\n"
					+ "and sz.size_category_num=s.size_category_num\r\n"
					+ "and s.s_num = p.s_num\r\n"
					+ "and p.p_num=a.p_num\r\n"
					+ "and a.a_num=aw.a_num(+)\r\n"
					+ "and p.p_status in (6, 7, 8, 9)", nativeQuery = true)
	Page<Auction> findEndByMNum(@Param("mNum") Long mNum, Pageable pageable);
	
	/**
	 * 결제할 때 정보
	 * @param mNum
	 * @param pNum
	 * @param aNum
	 * @return
	 */
	@Query(value="select s.s_num, s.s_name, s.s_brand, sz.size_category_name, s.s_color, s.s_grade,\r\n"
			+ "m.name, m.email, m.tel, a.a_price, ad.addr_num, ad.addr_recipient,\r\n"
			+ "ad.addr_post_num, ad.addr_tel, ad.addr, ad.addr_detail, ad.addr_type\r\n"
			+ "from product p\r\n"
			+ "INNER JOIN stock s ON s.s_num=p.s_num\r\n"
			+ "INNER JOIN sizes sz ON sz.size_category_num = s.size_category_num\r\n"
			+ "INNER JOIN auction a ON p.p_num=a.p_num\r\n"
			+ "INNER JOIN member m ON m.m_num=a.m_num\r\n"
			+ "LEFT OUTER JOIN address ad ON ad.m_num=m.m_num\r\n"
			+ "where p.p_num= :pNum\r\n"
			+ "and a.a_num= :aNum\r\n"
			+ "and m.m_num= :mNum", nativeQuery = true)
	List<Object[]> findOrderDetailByPNum(@Param("mNum") Long mNum, @Param("pNum") Long pNum, @Param("aNum") Long aNum);
}
