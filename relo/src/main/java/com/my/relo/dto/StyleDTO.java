package com.my.relo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.relo.entity.Member;
import com.my.relo.entity.Style;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class StyleDTO {
	
	private Long styleNum;
	
	private MemberDTO member;
	
	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime styleDate;
	
	private int styleCnt;
	
	@JsonIgnoreProperties(value = "styleDTO")
	private List<ReplyDTO> replyList;
	
	private List<StyleTagDTO> tagList;
	
	private List<LikesDTO> likesList;
	
	private String date;
	
	public void setStyleDate(LocalDateTime LocalDateTime) {
		this.date = DateTimeFormat.timesAgo(LocalDateTime);
	} 
	
	public Style toEntity(MemberDTO member) {
		Member m = Member.builder().id(member.getId())
				.pwd(member.getPwd())
				.birth(member.getBirth())
				.email(member.getEmail())
				.name(member.getName())
				.tel(member.getTel())
				.type(member.getType())
				.mNum(member.getMNum())
				.build();
		Style entity = Style.builder().member(m).build();
		return entity;
	}
}