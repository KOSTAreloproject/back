package com.my.relo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.my.relo.entity.Reply;
import com.my.relo.entity.Style;
import com.my.relo.entity.StyleTag;
import com.my.relo.repository.LikesRepository;
import com.my.relo.repository.ReplyRepository;
import com.my.relo.repository.StyleRepository;
import com.my.relo.repository.StyleTagRepository;
@SpringBootTest
class StyleFindTest {
	@Autowired
	private StyleRepository sr;
	
	@Autowired
	private ReplyRepository rr;
	
	@Autowired
	private LikesRepository lr;
	
	@Autowired
	private StyleTagRepository str;
	Logger log = LoggerFactory.getLogger(getClass());
	@DisplayName("게시판 최신순 리스트 출력 테스트")
	@Test
	void styleFindTest() {
		List<Style> list = sr.findAll(Sort.by(Sort.Direction.DESC,"styleNum"));
		for(Style s : list) {
			List <StyleTag> tagList = s.getTagList();
			int likeCnt = lr.ListByStyleNum(s.getStyleNum()).size();
			log.info("글번호 : "+s.getStyleNum()+" 작성자 : "+s.getMember().getId()+ " 작성일: "+ s.getStyleDate());
			log.info("좋아요 개수 : "+ likeCnt +" 조회수 : " + s.getStyleCnt());
			for(StyleTag t : tagList) {
				log.info("태그 : " + t.getSte().getHashName());
			}
		}
	}
	@DisplayName("게시판 상세보기 테스트")
	@Test
	void styleFindByStyleNumTest() {
		Optional<Style> optS= sr.findById(1L);
		Style s = optS.get();
		String id = s.getMember().getId();
		List<StyleTag> tagList = s.getTagList();
		Date date = s.getStyleDate();
		int styleCnt = s.getStyleCnt();
		List<Reply> repList = rr.findByStyleNum(s.getStyleNum());
		int likeCnt = lr.ListByStyleNum(s.getStyleNum()).size();
		log.info("작성자 : "+id+" 작성일 : "+date+" 조회수 : "+styleCnt+" 좋아요 : " +likeCnt+ " 댓글개수 : "+repList.size());
		for(StyleTag t : tagList) {
			log.info("태그 : " + t.getSte().getHashName());
		}
		for(Reply r : repList) {
			log.info("댓글작성자 : "+ r.getMember().getId() + " 댓글내용 : "+ r.getRepContent()+" 댓글작성일 : "+ r.getRepDate());
		}
	}
	@DisplayName("내가 쓴 게시물리스트 최신순 출력 테스트")
	@Test
	void styleListFindByMNumTest() {
		List<Style> list = sr.findBymNum(1L);
		for(Style s: list) {
			int likeCnt = lr.ListByStyleNum(s.getStyleNum()).size();
			log.info("글번호 : "+s.getStyleNum()+" 작성자 : "+s.getMember().getId()+ " 작성일: "+ s.getStyleDate());
			log.info("좋아요 개수 : "+likeCnt+" 조회수 : " + s.getStyleCnt());
		}
	}
	@DisplayName("해시태그 인기순 출력 테스트")
	@Test
	void styleTagListByCnt() {
		List<String> list = str.ListByCnt();
		for(String st :list) {
			log.info("이름:"+st);
		}
	}
}
