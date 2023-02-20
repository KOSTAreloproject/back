package com.my.relo;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.entity.Reply;
import com.my.relo.entity.Style;
import com.my.relo.entity.StyleTag;
import com.my.relo.entity.StyleTagEmbedded;
import com.my.relo.repository.ReplyRepository;
import com.my.relo.repository.StyleRepository;
import com.my.relo.repository.StyleTagRepository;
@SpringBootTest
class StyleUpdateTest {
	@Autowired
	private StyleRepository sr;
	
	@Autowired
	private StyleTagRepository str;
	
	@Autowired
	private ReplyRepository rr;
	
	Logger log = LoggerFactory.getLogger(getClass());
	@DisplayName("게시물 업데이트시 해당 태그들 삭제 후 태그 업데이트 테스트")
	@Test
	void testUpdate() {
		
		Optional<Style> style = sr.findById(2L);
		Style s = style.get();
		str.deleteByStyleNum(s.getStyleNum());
		StyleTag tag = new StyleTag();
		for(int i = 1; i < 3; i++) {
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName("수정 태그"+i);
			tag.setSte(ste);
			tag.setStyle(s);
			str.save(tag);
		}
	}
	@DisplayName("댓글 업데이트 테스트")
	@Test
	void testReplyUpdate() {
		Optional<Reply> reply =rr.findById(3L);
		Reply r =reply.get();
		r.setRepContent("수정 대댓글1-1");
		rr.save(r);
	}

}
