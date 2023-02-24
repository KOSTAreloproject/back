package com.my.relo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.dto.DateTimeFormat;
import com.my.relo.dto.LikesDTO;
import com.my.relo.dto.ReplyDTO;
import com.my.relo.dto.StyleDTO;
import com.my.relo.dto.StyleTagDTO;
import com.my.relo.entity.Member;
import com.my.relo.entity.Style;
import com.my.relo.entity.StyleTagEmbedded;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.LikesRepository;
import com.my.relo.repository.MemberRepository;
import com.my.relo.repository.StyleRepository;
@SpringBootTest
class StyleTest {

	@Autowired
	private StyleService ss;
	
	@Autowired
	private LikesRepository lr;
	
	@Autowired
	private MemberRepository mr;
	
	@Autowired
	private StyleRepository sr;
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@DisplayName("게시판 추가")
	@Test
	void writeTest() throws AddException {
		StyleDTO style = new StyleDTO();
		Optional<Member> optM = mr.findById(1L);
		Member m = optM.get();
		List<StyleTagDTO> tagList = new ArrayList<>();
		for(int i = 1; i < 3; i++) {
			StyleTagDTO tag = new StyleTagDTO();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName("테스트 !"+i);
			tag.setSte(ste);
			tagList.add(tag);
		}
		style.setTagList(tagList);
		style.setMember(m);
		ss.write(style);
	}
	
	@DisplayName("게시판 삭제")
	@Test
	void DeleteTest() throws RemoveException {
		ss.deleteByStyleNum(11L);
	}
	
	@DisplayName("게시판 전체 목록 출력(최신순)")
	@Test
	void FindAllListtest() throws FindException {
		List<StyleDTO> list = ss.listByStyleNum();
		for(StyleDTO s : list) {
			int likeCnt = lr.ListByStyleNum(s.getStyleNum()).size();
			log.info("글번호 : "+s.getStyleNum()+" 멤버ID : "+s.getMember().getId()+" 작성일: "+ s.getDate());
			log.info("좋아요 개수 :"+likeCnt);
			List <StyleTagDTO> tagList =s.getTagList();
			for(StyleTagDTO t : tagList) {
				log.info("StyleTag : "+ t.getSte().getHashName());
			}
		}
	}
	
	@DisplayName("게시판 전체 목록 출력(조회수순)")
	@Test
	void FindAllListByCntTest() throws FindException {
		List<StyleDTO> list = ss.listBystyleCnt();
		for(StyleDTO s : list) {
			int likeCnt = lr.ListByStyleNum(s.getStyleNum()).size();
			log.info("글번호 : "+s.getStyleNum()+" 멤버ID : "+s.getMember().getId()+" 작성일: "+s.getDate());
			log.info("좋아요 개수 :"+likeCnt+" 조회수 : "+s.getStyleCnt());
			List <StyleTagDTO> tagList =s.getTagList();
			for(StyleTagDTO t : tagList) {
				log.info("StyleTag : "+ t.getSte().getHashName());
			}
		}
	}
	
	@DisplayName("내가 쓴 게시판 리스트 출력 테스트")
	@Test
	void styleFindMNumTest() throws FindException {
		List<StyleDTO> list = ss.listByMNum(2L);
		for(StyleDTO s : list) {
			int likeCnt = lr.ListByStyleNum(s.getStyleNum()).size();
			log.info("글번호 : "+s.getStyleNum()+" 멤버ID : "+s.getMember().getId()+" 작성일: "+s.getDate());
			log.info("좋아요 개수 :"+likeCnt);
			List <StyleTagDTO> tagList =s.getTagList();
			for(StyleTagDTO t : tagList) {
				log.info("StyleTag : "+ t.getSte().getHashName());
			}
		}
	}
	
	@DisplayName("게시판 해시태그별 리스트 출력 리스트")
	@Test
	void styleFindHashNameTest() throws FindException {
		String hashName = "수정태그>>1";
		List<StyleDTO> list = ss.listByHashName(hashName);
		for(StyleDTO s : list) {
			int likeCnt = lr.ListByStyleNum(s.getStyleNum()).size();
			log.info("글번호 : "+s.getStyleNum()+" 멤버ID : "+s.getMember().getId()+" 작성일: "+s.getDate());
			log.info("좋아요 개수 :"+likeCnt);
			List <StyleTagDTO> tagList =s.getTagList();
			for(StyleTagDTO t : tagList) {
				log.info("StyleTag : "+ t.getSte().getHashName());
			}
		}
	}
	
	@DisplayName("게시판 상세보기 출력")
	@Test
	void styleFindDetailTest() throws FindException{
		Long styleNum = 19L;
		StyleDTO s = ss.styleDetail(styleNum);
		log.info("글번호 : "+s.getStyleNum()+" 멤버ID : "+s.getMember().getId());
		log.info("좋아요수 : "+s.getLikesList().size()+"작성일 : "+s.getDate());
		List<StyleTagDTO> tags= s.getTagList();
		for(StyleTagDTO t : tags) {
			log.info("스타일태그 : " + t.getSte().getHashName());
		}
		List<ReplyDTO> replys= s.getReplyList();
		for(ReplyDTO r : replys) {
			log.info("댓글작성자 : "+r.getMember().getId()+" 댓글 : "+ r.getRepContent()+" 작성일 : "+ r.getDate());
		}
		List<LikesDTO> likes = s.getLikesList();
		for(LikesDTO l : likes) {
			log.info("좋아요 누른 사람 : "+l.getLe().getMNum());
		}
	}
	
	@DisplayName("게시판 업데이트")
	@Test
	void styleEditTest() throws AddException{
		Optional<Style> optS = sr.findById(3L);
		Style s = optS.get();
		List <StyleTagDTO> tagList = new ArrayList<>();
		for(int i = 1; i < 5; i++) {
			StyleTagDTO t = new StyleTagDTO();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName("수정태그>>"+i);
			ste.setStyleNum(s.getStyleNum());
			t.setSte(ste);
			tagList.add(t);
		}
		ss.edit(tagList);
	}
	
	@DisplayName("조회수 증가")
	@Test
	void pluaCntTest() throws AddException {
		Long styleNum = 12L;
		ss.plusCnt(styleNum);
	}

	}
