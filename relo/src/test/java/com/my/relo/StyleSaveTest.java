package com.my.relo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.entity.Likes;
import com.my.relo.entity.Member;
import com.my.relo.entity.Reply;
import com.my.relo.entity.Style;
import com.my.relo.entity.StyleTag;
import com.my.relo.entity.StyleTagEmbedded;
import com.my.relo.repository.LikesRepository;
import com.my.relo.repository.MemberRepository;
import com.my.relo.repository.ReplyRepository;
import com.my.relo.repository.StyleRepository;
import com.my.relo.repository.StyleTagRepository;
@SpringBootTest
class StyleSaveTest {
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
	@DisplayName("게시물 등록 테스트(게시물 정보만!)")
	@Test
	void testSave() {
		Style s1 = new Style();
		Optional<Member> optM = mr.findById(1L);
		Member m = optM.get();
		s1.setMember(m);
		sr.save(s1);
		
		Style s2 = new Style();
		Optional<Member> optM2 = mr.findById(2L);
		Member m2 = optM2.get();
		s2.setMember(m2);
		sr.save(s2);
	}
	@DisplayName("태그 등록 테스트(태그 정보만!)")
	@Test
	void testTagSave() {
		
		for(int i = 1; i < 6; i++) {
			StyleTag tag = new StyleTag();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName("스타일"+i);
			tag.setSte(ste);
			Optional<Style> optS=sr.findById(3L);
			Style s = optS.get();
			tag.setStyle(s);
			str.save(tag);
		}
	}
	@DisplayName("게시물+태그 등록 테스트")
	@Test
	void testAllSave() {
		
		Style s = new Style();
		Optional<Member> optM = mr.findById(1L);
		Member m = optM.get();
		s.setMember(m);
		sr.save(s);
		
		List<StyleTag> tagList = new ArrayList<>();
		for(int i = 1; i < 3; i++) {
			StyleTag tag = new StyleTag();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName("태그"+i);
			tag.setSte(ste);
			Optional<Style> optS=sr.findById(s.getStyleNum());
			Style s1 = optS.get();
			tag.setStyle(s1);
			str.save(tag);
			tagList.add(tag);
		}
	}
	@DisplayName("좋아요+1 등록 테스트")
	@Test
	void testLikeSave() {
		
		Optional<Style> optS=sr.findById(1L);
		Style s = optS.get();
		Optional<Member> optM=mr.findById(2L);
		Member m = optM.get();
		
		Likes like = new Likes();
		like.setStyle(s);
		like.setMember(m);
		lr.save(like);
		
		sr.updateLikes(s.getStyleNum());
	}
	@DisplayName("댓글 등록 테스트")
	@Test
	void testReplySave() {
		
		Reply r = new Reply();
		
		Optional<Style> optS= sr.findById(2L);
		Style s = optS.get();
		
		Optional<Member> optM=mr.findById(1L);
		Member m = optM.get();
		
		r.setStyle(s);
		r.setMember(m);
		r.setRepContent("테스트댓글2");
		rr.save(r);
	}
	@DisplayName("대댓글 등록 테스트")
	@Test
	void testReplyParentSave() {
		Reply r = new Reply();
		Optional<Style> optS= sr.findById(2L);
		Style s = optS.get();
		
		Optional<Member> optM=mr.findById(1L);
		Member m = optM.get();
		
		Optional<Reply> optR= rr.findById(3L);
		Reply parentR = optR.get();
		
		r.setStyle(s);
		r.setMember(m);
		r.setReplyParent(parentR);
		r.setRepContent("테스트 대댓글2-1");
		rr.save(r);
		
	}
}
