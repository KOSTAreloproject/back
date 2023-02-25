package com.my.relo.control;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.MemberDTO;
import com.my.relo.dto.ReplyDTO;
import com.my.relo.dto.StyleDTO;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.service.ReplyService;

@RestController
@RequestMapping("style/reply/*")
public class ReplyController {

	@Autowired
	private ReplyService service;
	
	/**
	 * 댓글, 대댓글 작성 
	 * @param styleNum
	 * @param session
	 * @param repContent
	 * @param repNum -> 부모댓글 번호 있으면 대댓글 
	 * @return
	 * @throws AddException
	 */
	@PostMapping(value="{styleNum}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postRep(@PathVariable("styleNum")Long styleNum,
										HttpSession session, String repContent,Long repNum) throws AddException{
		
//		Long logined = (Long) session.getAttribute("logined");
//		if(logined == null) {
//			throw new AddException("로그인하세요");
//		}
		Long logined = 1L;
		
		MemberDTO m = new MemberDTO();
		m.setMNum(logined);
		ReplyDTO r = new ReplyDTO();
		
		StyleDTO s = new StyleDTO();
		s.setStyleNum(styleNum);
		
		if(repNum != null) {
			ReplyDTO parentR = new ReplyDTO();
			parentR.setRepNum(repNum);
			r.setReplyParentDTO(parentR);
		}
		r.setMember(m);
		r.setStyle(s);
		r.setRepContent(repContent);

		service.writeReply(r);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	/**
	 * 댓글 수정 
	 * @param repNum
	 * @param repContent
	 * @return
	 * @throws AddException
	 */
	@PutMapping(value="{repNum}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateRep(@PathVariable("repNum")Long repNum,
												String repContent) throws AddException{
		
		ReplyDTO r = new ReplyDTO();
		r.setRepNum(repNum);
		r.setRepContent(repContent);
		
		service.writeReply(r);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	/**
	 * 댓글 삭제 
	 * @param repNum
	 * @return
	 * @throws RemoveException
	 * @throws FindException 
	 */
	@DeleteMapping(value="{repNum}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteRep(@PathVariable("repNum")Long repNum) throws RemoveException, FindException{
		
		service.deleteReply(repNum);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
