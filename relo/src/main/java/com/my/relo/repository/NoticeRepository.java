package com.my.relo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.my.relo.entity.Notice;
 
public interface NoticeRepository extends CrudRepository<Notice, Long>, PagingAndSortingRepository<Notice, Long> {
   
   @Query(value = "select nNum, nTitle, nCategory from Notice where nTitle like %:title% order by nNum desc")
   List<Object[]> findBynTitleLike(@Param("title") String title);
   
   @Query(value = "select nNum, nTitle, nCategory from Notice where nCategory=:category order by nNum desc")
   List<Object[]> findBynCategory(@Param("category") Integer category);
   
   @Query(value = "SELECT n_num, n_title FROM notice\r\n"
         + "WHERE n_num = (SELECT prev_no FROM (SELECT n_num, LAG(n_num, 1, -1) OVER(ORDER BY n_num) AS prev_no FROM notice) n \r\n"
         + "WHERE n_num = :nNum)", nativeQuery = true)
   List<Object[]> findPrevNotice(@Param("nNum") Long nNum);
   
   @Query(value = "SELECT n_num as n_num, n_title as n_title FROM notice "
         + "WHERE n_num = (SELECT next_no FROM (SELECT n_num, LEAD(n_num, 1, -1) OVER(ORDER BY n_num) AS next_no FROM notice) n "
         + "WHERE n_num = :nNum)", nativeQuery = true)
   List<Object[]> findNextNotice(@Param("nNum") Long nNum);
   
   Page<Notice> findAll(Pageable pageable);

}