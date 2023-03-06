package com.my.relo.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.dto.ReplyDTO;
import com.my.relo.entity.Member;
import com.my.relo.entity.Reply;
import com.my.relo.entity.Style;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.MemberRepository;
import com.my.relo.repository.ReplyRepository;
import com.my.relo.repository.StyleRepository;
@SpringBootTest
class ReplyTest {

	@Autowired
	private ReplyService rs;
	
	@Autowired
	private ReplyRepository rr;
	
	@Autowired
	private MemberRepository mr;
	
	@Autowired
	private StyleRepository sr;
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@DisplayName("댓글 작성")
	@Test
	void replyWriteTest() throws AddException {
		Optional<Member> optM =mr.findById(1L);
		Member m = optM.get();
		Optional<Style> optS = sr.findById(15L);
		Style s = optS.get();
		ReplyDTO reply = ReplyDTO.builder().member(m).style(s)
							.repContent("작성 ㅇ ").build();
		rs.writeReply(reply);
	}
	
	@DisplayName("대댓글 작성")
	@Test
	void childReplyWriteTest() throws AddException {
		Optional<Member> optM =mr.findById(2L);
		Member m = optM.get();
		Optional<Style> optS = sr.findById(15L);
		Style s = optS.get();
		Optional<Reply> optR = rr.findById(32L);
		Reply r = optR.get();
		
		ReplyDTO reply = ReplyDTO.builder().member(m).style(s).replyParent(r)
							.repContent("대댓").build();

		rs.writeReply(reply);
	}
	
	@DisplayName("댓글 수정")
	@Test
	void editReplyTest() throws FindException, AddException {
		Optional<Reply> optR = rr.findById(19L);
		Reply r = optR.get();
		ReplyDTO reply = 
				ReplyDTO.builder().member(r.getMember()).repContent("수정").repNum(r.getRepNum())
				.replyParent(r.getReplyParent()).style(r.getStyle()).repDate(r.getRepDate()).build();
		rs.writeReply(reply);
	}
	
	@DisplayName("대댓글 삭제")
	@Test
	void deleteReplyTest() throws RemoveException {
		rs.deleteReply(22L);
	}
	
	@DisplayName("부모댓글 삭제")
	@Test
	void deleteParentRepTest() throws RemoveException {
		rs.deleteReply(24L);
	}
	
	@DisplayName("댓글 리스트 출력")
	@Test
	void listByStyleNumTest() throws FindException{
		List<ReplyDTO> list= rs.listByStyleNum(19L);
		for(ReplyDTO r : list) {
			log.info("댓글 : "+ r.getRepContent());
		}
	}
	}
