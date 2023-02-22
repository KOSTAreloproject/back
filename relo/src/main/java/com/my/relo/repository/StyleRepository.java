package com.my.relo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.my.relo.entity.Style;

public interface StyleRepository extends CrudRepository<Style, Long>{
	
	public List<Style> findAll(Sort by);
	
	//내가 쓴 게시판 목록 출력하기 (최신순)
	@Query(value="SELECT * FROM style s WHERE m_num =?1 ORDER BY s.style_num desc",nativeQuery = true)
	public List<Style> findBymNum(Long mNum);
	
	//좋아요 순으로 게시판 목록 출력하기 
	@Query(value = "select s.*\n"
			+ "from style s \n"
			+ "    inner join(\n"
			+ "        select l.style_num\n"
			+ "        from likes l\n"
			+ "        group by l.style_num\n"
			+ "        order by count(*) desc\n"
			+ "        )l on (l.style_num = s.style_num)",nativeQuery = true)
	public List<Style> listByLikes();
	
	//해시태그별 게시판 목록 출력하기
	@Query(value = "select s.*\n"
			+ "from style s\n"
			+ "    inner join(\n"
			+ "        select t.style_num\n"
			+ "        from style_tag t\n"
			+ "        where hash_name = ?1\n"
			+ "        order by style_num desc\n"
			+ "        )t on (t.style_num = s.style_num)", nativeQuery = true)
	public List<Style> listByHashName(String hashName);
	
	//조회수 + 1
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE style s SET s.style_cnt=s.style_cnt+1 WHERE s.style_num=:styleNum", nativeQuery = true)
	public void updateCnt(Long styleNum);
}
