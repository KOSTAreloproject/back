package com.my.relo.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.my.relo.entity.Member;
import com.my.relo.entity.Reply;
import com.my.relo.entity.Style;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ReplyDTO {
	
	private Long repNum;
	
	private Style style;
	
	private Member member;
	
	private String repContent;
	
	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date repDate;
	
	private Reply replyParent;

}
