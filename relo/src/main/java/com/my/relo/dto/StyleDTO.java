package com.my.relo.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.my.relo.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class StyleDTO {
	
	private Long styleNum;
	
	private Member member;
	
	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm")
	private Date styleDate;
	
	private int styleCnt;
	
	private List<ReplyDTO> replyList;
	
	private List<StyleTagDTO> tagList;
	
	private List<LikesDTO> likesList;
}
