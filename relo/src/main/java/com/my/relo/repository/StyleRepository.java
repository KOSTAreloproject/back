package com.my.relo.repository;

import java.util.List;

import javax.persistence.PreRemove;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.my.relo.entity.Style;

public interface StyleRepository extends CrudRepository<Style, Long>{
	
	List<Style> findAll(Sort by);
	
	//내가 쓴 게시판 목록 출력하기 (최신순)
	@Query(value="SELECT * FROM style s WHERE m_num =?1 ORDER BY s.style_num desc",nativeQuery = true)
	List<Style> findBymNum(Long mNum);
	
	//좋아요 순으로 게시판 목록 출력하기 
	@Query(value = "select s.*\n"
			+ "from style s \n"
			+ "    inner join(\n"
			+ "        select l.style_num\n"
			+ "        from likes l\n"
			+ "        group by l.style_num\n"
			+ "        order by count(*) desc\n"
			+ "        )l on (l.style_num = s.style_num)",nativeQuery = true)
	List<Style> listByLikes();
}
