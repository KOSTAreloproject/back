package com.my.relo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.repository.LikesRepository;
import com.my.relo.repository.MemberRepository;
import com.my.relo.repository.ReplyRepository;
import com.my.relo.repository.StyleRepository;
import com.my.relo.repository.StyleTagRepository;
@SpringBootTest
class StyleDeleteTest {
	
	@Autowired
	private StyleRepository sr;
	
	@Autowired
	private MemberRepository mr;
	
	@Autowired
	private StyleTagRepository str;
	
	@Autowired
	private LikesRepository lr;
	
	@Autowired
	private ReplyRepository rr;
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@DisplayName("댓글 삭제 테스트 -> 부모댓글 삭제되면 자식댓글 삭제")
	@Test
	void testReplyRemove() {
		rr.deleteById(1L);
	}
	@DisplayName("게시물 삭제 테스트 -> 게시물 삭제되면 댓글,좋아요,태그 삭제")
	@Test
	void testStyleRemove() {
		sr.deleteById(1L);
	}

}
