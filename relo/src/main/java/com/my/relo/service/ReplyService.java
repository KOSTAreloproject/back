package com.my.relo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.MemberDTO;
import com.my.relo.dto.ReplyDTO;
import com.my.relo.dto.StyleDTO;
import com.my.relo.entity.Member;
import com.my.relo.entity.Reply;
import com.my.relo.entity.Style;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.MemberRepository;
import com.my.relo.repository.ReplyRepository;
import com.my.relo.repository.StyleRepository;

@Service
public class ReplyService {
	@Autowired
	private ReplyRepository rr;

	@Autowired
	private StyleRepository sr;

	@Autowired
	private MemberRepository mr;

	@Autowired
	ModelMapper modelMapper;

	/**
	 * 댓글 리스트 출력
	 * 
	 * @param styleNum : 스타일 번호
	 * @return List<ReplyDTO>
	 * @throws FindException
	 */
	public List<ReplyDTO> listByStyleNum(Long styleNum) throws FindException {
		List<Reply> repList = rr.findByStyleNum(styleNum);
		List<ReplyDTO> repDTOList = new ArrayList<>();
		for (Reply r : repList) {
			ReplyDTO pDTO = new ReplyDTO();
			pDTO.setRepNum(r.getReplyParent().getRepNum());
			ReplyDTO rDTO = new ReplyDTO();
			rDTO.setRepNum(r.getRepNum());
			rDTO.setRepDate(r.getRepDate());
			rDTO.setRepContent(r.getRepContent());
			Member m = r.getMember();
			MemberDTO mDTO = new MemberDTO();
			mDTO.builder().id(m.getId()).build();
			rDTO.setMember(mDTO);
			rDTO.setReplyParentDTO(pDTO);
			repDTOList.add(rDTO);
		}
		return repDTOList;
	}

	/**
	 * 댓글 등록 OR 수정
	 * 
	 * @param replyDTO
	 * @throws AddException
	 */
	public void writeReply(ReplyDTO replyDTO) throws AddException {

		String repContent = replyDTO.getRepContent();
		if (repContent == "") {
			throw new AddException("댓글 내용을 입력해주세요...");
		}
		StyleDTO sDTO = replyDTO.getStyle();

		if (sDTO != null) {
			Optional<Style> optS = sr.findById(sDTO.getStyleNum());
			Style s = optS.get();

			MemberDTO mDTO = replyDTO.getMember();
			Optional<Member> optM = mr.findById(mDTO.getMnum());
			Member m = optM.get();

			ReplyDTO rDTO = replyDTO.getReplyParentDTO();

			if (rDTO != null) {
				Optional<Reply> optR = rr.findById(rDTO.getRepNum());
				Reply parentR = optR.get();
				Reply R = new Reply(s, m, parentR, repContent);
				rr.save(R);
			} else {
				Reply R = new Reply(s, m, repContent);
				rr.save(R);
			}
		} else {
			Long repNum = replyDTO.getRepNum();
			Optional<Reply> optR = rr.findById(repNum);
			Reply r = optR.get();
			r.updateReply(repNum, repContent);
			rr.save(r);
		}
	}

	/**
	 * 댓글 삭제
	 * 
	 * @param repNum : 댓글 번호
	 * @throws RemoveException
	 * @throws FindException
	 */
	public void deleteReply(Long repNum) throws RemoveException, FindException {
		Optional<Reply> optR = rr.findById(repNum);
		if (!optR.isPresent()) {
			throw new FindException("없는 댓글 입니다.");
		}
		rr.deleteById(repNum);
	}
}