package com.my.relo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.my.relo.entity.Auction;
import com.my.relo.entity.Product;

public interface AuctionRepository extends CrudRepository<Auction, Long> {

//	@Query(value="SELECT auction_seq.nextval FROM dual", nativeQuery = true)
//	Long getNextANum();
	
	/**
	 * 회원이 해당 상품 경매에 참여한 이력 있는지 select SQL
	 * @param mNum 회원번호 pNum 상품번호
	 * @return Auction 있을 시 경매이력 / 없으면 null
	 */
	Auction findBymNumAndProduct(Long mNum, Product product);
	
	/**
	 * 회원의 입찰 내역 (경매 진행 중) select SQL
	 * 상품번호, 브랜드명, 상품명, 사이즈, 등급, 경매마감일
	 *  
	 * @return List<Auction> 
	 */
	@Query(value="select a.a_num, p.p_num, p.p_end_date, s.s_grade, s.s_brand, s.s_name, sz.size_category_name, a.a_price\r\n"
			+ "from sizes sz, stock s, product p, auction a\r\n"
			+ "where a.m_num = :mNum \r\n"
			+ "and sz.size_category_num = s.size_category_num\r\n"
			+ "and s.s_num = p.s_num\r\n"
			+ "and p.p_num = a.p_num\r\n"
			+ "and p.p_status = 4\r\n"
			+ "order by p_end_date desc", nativeQuery = true)
	List<Auction> findAuctionIngByMNum(@Param("mNum") Long mNum);
	
	/**
	 * 회원의 입찰 내역 (경매 종료) select SQL left join으로 변경
	 * 상품번호, 브랜드명, 상품명, 사이즈, 등급, 경매마감일
	 *  
	 * @return List<Auction>
	 */
	@Query(value="select a.a_num, p.p_num, s.s_grade,s.s_name, sz.size_category_name, a.a_price, p.p_status, p.p_end_date, aw.a_num as aw_num\r\n"
			+ "from sizes sz, stock s, product p, auction a, award aw\r\n"
			+ "where a.m_num = :mNum \r\n"
			+ "and sz.size_category_num=s.size_category_num\r\n"
			+ "and s.s_num = p.s_num\r\n"
			+ "and p.p_num=a.p_num\r\n"
			+ "and a.a_num=aw.a_num(+)\r\n"
			+ "and p.p_status in (6, 7, 8)\r\n"
			+ "order by p_end_date desc", nativeQuery = true)
	List<Auction> findAuctionEndByMNum(@Param("mNum") Long mNum);
}
