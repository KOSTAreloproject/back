package com.my.relo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.my.relo.entity.Orders;

public interface OrdersRepository extends CrudRepository<Orders, Long> {
	/**
	 * 회원 주문 목록 select SQL
	 * 상품번호, 브랜드명, 상품명, 사이즈, 가격, 주문일, 배송상태
	 *  
	 * @return List<Orders> 
	 */
	@Query(value="select a.m_num, p.p_num, o.a_num, s.s_name, s.s_brand, sz.size_category_name, a.a_price, o.o_date, o.o_memo, od.d_status\r\n"
			+ "from stock s, product p, sizes sz, orders o, auction a, order_delivery od \r\n"
			+ "where a.a_num in (select aw.a_num \r\n"
			+ "from auction a, award aw \r\n"
			+ "where a.m_num= :mNum \r\n"
			+ "and a.a_num=aw.a_num)\r\n"
			+ "and sz.size_category_num = s.size_category_num\r\n"
			+ "and s.s_num=p.s_num\r\n"
			+ "and p.p_num=a.p_num\r\n"
			+ "and a.a_num=o.a_num\r\n"
			+ "and o.a_num=od.a_num\r\n"
			+ "order by o.o_date desc", nativeQuery = true)
	List<Orders> findOrdersListBymNum(@Param("mNum") Long mNum);
	
	/**
	 * 회원 주문 상세 select SQL
	 * 상품번호, 경매번호, 브랜드명, 상품명, 사이즈, 가격, 주문일, 배송상태, 송장번호, 구매확정일, 주소번호, 수령인, 전화번호, 우편번호, 주소, 주소상세
	 *  
	 * @return Orders
	 */
	@Query(value="select a.m_num, o.a_num, o.o_date, o.o_memo, p.p_num , s.s_brand, s.s_name , sz.size_category_name, a.a_price, od.d_status, od.d_tracking_info, od.d_complete_day, ad.addr_num, ad.addr_recipient, ad.addr_tel, ad.addr_post_num, ad.addr, ad.addr_detail\r\n"
			+ "from product p, stock s, sizes sz, auction a, orders o, order_delivery od, address ad\r\n"
			+ "where o.a_num= :aNum \r\n"
			+ "and sz.size_category_num = s.size_category_num\r\n"
			+ "and s.s_num=p.s_num\r\n"
			+ "and p.p_num=a.p_num\r\n"
			+ "and a.a_num=o.a_num\r\n"
			+ "and o.a_num=od.a_num\r\n"
			+ "and od.addr_num=ad.addr_num", nativeQuery = true)
	Optional<Orders> findOrdersByaNum(@Param("aNum") Long aNum);
	
	/**
	 * 관리자가 보는 상품 구매확정 목록 select SQL
	 * 상품번호, 경매번호, 브랜드명, 상품명, 사이즈, 가격, 주문일, 배송상태, 송장번호, 구매확정일, 주소번호, 수령인, 전화번호, 우편번호, 주소, 주소상세
	 *  
	 * @return List<Orders>
	 */
	@Query(value="select a.a_num, p.p_num, s.m_num, s.s_name, a.m_num as buy_m_num, o.o_date, o.o_memo, od.d_complete_day, a.a_price\r\n"
			+ "from stock s, product p, auction a, orders o, order_delivery od\r\n"
			+ "where od.d_status=3\r\n"
			+ "and s.s_num=p.s_num\r\n"
			+ "and p.p_num=a.p_num\r\n"
			+ "and a.a_num=o.a_num\r\n"
			+ "and o.a_num=od.a_num\r\n"
			+ "order by od.d_complete_day desc", nativeQuery = true)
	List<Orders> findOrdersConfirmedListBydStatus3();
	
	
	
	
	
}
