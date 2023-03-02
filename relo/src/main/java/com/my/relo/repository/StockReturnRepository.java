package com.my.relo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.my.relo.entity.StockReturn;

public interface StockReturnRepository extends CrudRepository<StockReturn, Long> {

   /*
    * 판매자가 재고배송 확인
    */
   @Query(value = "SELECT sr.s_num,sr.sr_start_date,sr.sr_status,sr.sr_tracking_info,sr.addr_num\r\n"
         + "FROM STOCK_RETURN sr INNER JOIN STOCK s on sr.s_num = s.s_num\r\n"
         + "where m_num=:mNum", nativeQuery = true)
   public Page<Object[]> listById(@Param("mNum") Long mNum, Pageable pageable);
}