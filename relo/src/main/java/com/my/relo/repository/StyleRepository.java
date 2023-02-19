package com.my.relo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.my.relo.entity.Member;
import com.my.relo.entity.Style;

public interface StyleRepository extends CrudRepository<Style, Long>{

	@Modifying
	@Transactional
	@Query("update Style s set s.styleLikes = s.styleLikes+1 where s.styleNum= :styleNum")
	void updateLikes(@Param("styleNum") Long styleNum);

	List<Style> findAll(Sort by);
	
	@Query(value="SELECT * FROM style s WHERE m_num =?1 ORDER BY s.style_num desc",nativeQuery = true)
	List<Style> findBymNum(Long mNum);
}
