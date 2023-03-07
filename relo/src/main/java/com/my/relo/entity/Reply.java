package com.my.relo.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
@DynamicUpdate
@Entity 
@Table(name = "reply")
@SequenceGenerator(name = "reply_sequence_generator",
					sequenceName = "reply_seq",
					initialValue = 1,
					allocationSize = 1)
public class Reply {
	/**
	 *	댓글 
	 */
	@Id
	@Column(name = "rep_num", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_sequence_generator")
	private Long repNum;
	
	@ManyToOne
	@JoinColumn(name = "style_num", nullable = false)
	private Style style;
	
	@ManyToOne
	@JoinColumn(name = "m_num", nullable = false)
	private Member member;
	
	@Column(name = "rep_content", nullable = false)
	private String repContent;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@CreationTimestamp
	@Column(name = "rep_date")
	private LocalDateTime repDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rep_parent")
	@ColumnDefault(value = "0")
	private Reply replyParent;
	
	@OneToMany(mappedBy = "replyParent",
				cascade = CascadeType.REMOVE
				)
	private List<Reply> children;
	
	public void updateReply(Long repNum, String repContent) {
		this.repNum = repNum;
		this.repContent = repContent;
	}
	
	public Reply(Style style, Member member, String repContent) {
		this.style = style;
		this.member = member;
		this.repContent = repContent;
	}
	
	public Reply(Style style, Member member, Reply replyParent, String repContent) {
		this.style = style;
		this.member = member;
		this.replyParent = replyParent;
		this.repContent = repContent;
	}
}