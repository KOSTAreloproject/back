package com.my.relo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.ReplyDTO;
import com.my.relo.entity.Reply;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.ReplyRepository;

@Service
public class ReplyService {
	@Autowired
	private ReplyRepository rr;
	
	@Autowired
	ModelMapper modelMapper;
	
	//댓글리스트 출력
	public List<ReplyDTO> listByStyleNum(Long styleNum) throws FindException{
		List<Reply> repList = rr.findByStyleNum(styleNum);
		List<ReplyDTO> repDTOList = 
				repList.stream().map(reply -> modelMapper.map(reply, ReplyDTO.class)).collect(Collectors.toList());
		return repDTOList;	
	}
	
	//댓글 등록, 수정 
	public void writeReply(ReplyDTO replyDTO) throws AddException{
		Reply r = replyDTO.toEntity();
		rr.save(r);
	}

	//댓글 삭제
	public void deleteReply(Long repNum) throws RemoveException {
		rr.deleteById(repNum);
	}
 }
