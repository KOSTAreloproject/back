package com.my.relo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.my.relo.entity.Style;

public interface StyleRepository extends CrudRepository<Style, Long>{
	
	List<Style> findAll(Sort by);
	
	//내가 쓴 게시판 목록 출력하기
	@Query(value="SELECT * FROM style s WHERE m_num =?1 ORDER BY s.style_num desc",nativeQuery = true)
	List<Style> findBymNum(Long mNum);
	
	
}
