package com.my.relo;

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
		
		Style s1 = new Style();
		Optional<Member> optM1 = mr.findById(1L);
		Member m1 = optM1.get();
		s1.setMember(m1);
		sr.save(s1);
		
		for(int i = 1; i < 3; i++) {
			StyleTag tag1 = new StyleTag();
			StyleTagEmbedded ste1 = new StyleTagEmbedded();
			ste1.setHashName("태그"+i);
			tag1.setSte(ste1);
			Optional<Style> optS1=sr.findById(s1.getStyleNum());
			Style style1 = optS1.get();
			tag1.setStyle(style1);
			str.save(tag1);
		}
		
		Style s2 = new Style();
		Optional<Member> optM2 = mr.findById(2L);
		Member m2 = optM2.get();
		s2.setMember(m2);
		sr.save(s2);
		
		for(int i = 1; i < 5; i++) {
			StyleTag tag2 = new StyleTag();
			StyleTagEmbedded ste2 = new StyleTagEmbedded();
			ste2.setHashName("스타일"+i);
			tag2.setSte(ste2);
			Optional<Style> optS2=sr.findById(s2.getStyleNum());
			Style style2 = optS2.get();
			tag2.setStyle(style2);
			str.save(tag2);
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
		
		Reply r1 = new Reply();
		
		Optional<Style> optS1= sr.findById(1L);
		Style s1 = optS1.get();
		
		Optional<Member> optM1=mr.findById(2L);
		Member m1 = optM1.get();
		
		r1.setStyle(s1);
		r1.setMember(m1);
		r1.setRepContent("테스트댓글1");
		rr.save(r1);
		
		Reply r2 = new Reply();
		
		Optional<Style> optS2= sr.findById(2L);
		Style s2 = optS2.get();
		
		Optional<Member> optM2=mr.findById(1L);
		Member m2 = optM2.get();
		
		r2.setStyle(s2);
		r2.setMember(m2);
		r2.setRepContent("테스트댓글2");
		rr.save(r2);
	}
	@DisplayName("대댓글 등록 테스트")
	@Test
	void testReplyParentSave() {
		Reply r1 = new Reply();
		Optional<Style> optS1= sr.findById(1L);
		Style s1 = optS1.get();
		
		Optional<Member> optM1=mr.findById(1L);
		Member m1 = optM1.get();
		
		Optional<Reply> optR1= rr.findById(1L);
		Reply parentR1 = optR1.get();
		
		r1.setStyle(s1);
		r1.setMember(m1);
		r1.setReplyParent(parentR1);
		r1.setRepContent("테스트 대댓글1-1");
		rr.save(r1);
		
		Reply r2 = new Reply();
		Optional<Style> optS2= sr.findById(1L);
		Style s2 = optS2.get();
		
		Optional<Member> optM2=mr.findById(2L);
		Member m2 = optM2.get();
		
		Optional<Reply> optR2= rr.findById(1L);
		Reply parentR2 = optR2.get();
		
		r2.setStyle(s2);
		r2.setMember(m2);
		r2.setReplyParent(parentR2);
		r2.setRepContent("테스트 대댓글1-2");
		rr.save(r2);
		
		Reply r3 = new Reply();
		Optional<Style> optS3= sr.findById(2L);
		Style s3 = optS3.get();
		
		Optional<Member> optM3=mr.findById(2L);
		Member m3 = optM3.get();
		
		Optional<Reply> optR3= rr.findById(2L);
		Reply parentR3 = optR3.get();
		
		r3.setStyle(s3);
		r3.setMember(m3);
		r3.setReplyParent(parentR3);
		r3.setRepContent("테스트 대댓글2-1");
		rr.save(r3);
	}
}
