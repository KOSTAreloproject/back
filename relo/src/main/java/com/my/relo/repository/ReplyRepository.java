package com.my.relo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.my.relo.entity.Reply;

public interface ReplyRepository extends CrudRepository<Reply, Long>{
	//댓글 리스트 출력 (대댓글구현)
	@Query(value="select level, style_num, m_num, rep_date, rep_num, rep_content, rep_parent\n"
			+ "    from reply\n"
			+ "    where style_num = ?1\n"
			+ "    start with rep_parent is null\n"
			+ "    connect by prior rep_num = rep_parent\n"
			+ "    order siblings by rep_parent desc", nativeQuery = true)
	List<Reply> findByStyleNum(Long styleNum);
}
