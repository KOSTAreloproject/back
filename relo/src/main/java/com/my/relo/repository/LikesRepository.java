package com.my.relo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.my.relo.entity.Likes;
import com.my.relo.entity.Style;

public interface LikesRepository extends CrudRepository<Likes, Style> {

	@Modifying
	@Transactional
	@Query(value ="DELETE FROM likes l WHERE l.style_num = :styleNum AND l.m_num = :mNum",nativeQuery = true)
	void deleteLikes(@Param("styleNum")Long styleNum,@Param("mNum")Long mNum);
}
