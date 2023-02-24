package com.my.relo.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.my.relo.entity.Member;
import com.my.relo.entity.Reply;
import com.my.relo.entity.Style;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
	private LocalDateTime repDate;
	
	private Reply replyParent;
	
	private String date;
	
	public void setRepDate(LocalDateTime LocalDateTime) {
		this.date = DateTimeFormat.timesAgo(LocalDateTime);
	}
	
	@Builder
	public ReplyDTO(Long repNum, Style style, Member member, String repContent, Reply replyParent, LocalDateTime repDate) {
		this.repNum = repNum;
		this.style = style;
		this.member = member;
		this.repContent = repContent;
		this.replyParent = replyParent;
		this.repDate = repDate;
	}
	
	public Reply toEntity() {
		return Reply.builder()
				.repNum(repNum).style(style).member(member).repDate(repDate)
				.repContent(repContent).replyParent(replyParent).build();
	}
}
