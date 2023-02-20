package com.my.relo;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.entity.Member;
import com.my.relo.entity.Style;
import com.my.relo.repository.LikesRepository;
import com.my.relo.repository.MemberRepository;
import com.my.relo.repository.ReplyRepository;
import com.my.relo.repository.StyleRepository;
@SpringBootTest
class StyleDeleteTest {
	
	@Autowired
	private StyleRepository sr;

	@Autowired
	private ReplyRepository rr;
	
	@Autowired
	private LikesRepository lr;
	
	@Autowired
	private MemberRepository mr;
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@DisplayName("좋아요 삭제 테스트 -> 좋아요 삭제되면 해당 게시글 좋아요-1")
	@Test
	void testLikesRemove() {
		Optional<Style> style = sr.findById(1L);
		Style s = style.get();
		Optional<Member> member = mr.findById(2L);
		Member m = member.get();
		lr.deleteLikes(s.getStyleNum(), m.getMNum());
		sr.deleteLikes(s.getStyleNum());
	}
	
	@DisplayName("댓글 삭제 테스트 -> 부모댓글 삭제되면 자식댓글 삭제")
	@Test
	void testReplyRemove() {
		rr.deleteById(2L);
	}
	@DisplayName("게시물 삭제 테스트 -> 게시물 삭제되면 댓글,좋아요,태그 삭제")
	@Test
	void testStyleRemove() {
		sr.deleteById(1L);
	}

}
