package com.my.relo.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ReplyDTO {
	
	private Long repNum;
	
	private StyleDTO style;
	
	private MemberDTO member;
	
	private String repContent;
	
	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime repDate;
	
	@JsonIgnoreProperties(value="replyParentDTO")
	private ReplyDTO replyParentDTO;
	
	private String date;
	
	
	public void setRepDate(LocalDateTime LocalDateTime) {
		this.date = DateTimeFormat.timesAgo(LocalDateTime);
	}
	
	@Builder
	public ReplyDTO(Long repNum, StyleDTO style, MemberDTO member, String repContent, ReplyDTO replyParentDTO, LocalDateTime repDate) {
		this.repNum = repNum;
		this.style = style;
		this.member = member;
		this.repContent = repContent;
		this.replyParentDTO = replyParentDTO;
		this.repDate = repDate;
	}
	
}
