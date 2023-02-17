package com.my.relo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
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
	@Column(name = "rep_num")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_sequence_generator")
	private Long repNum;
	
	@ManyToOne
	@JoinColumn(name = "style_num")
	private Style styleRep;
	
	@ManyToOne
	@JoinColumn(name = "m_num")
	private Member memberRep;
	
	@Column(name = "rep_content")
	private String repContent;
	
	@Column(name = "rep_date")
	private Date repDate;
	
	@ManyToOne
	@JoinColumn(name = "rep_parent")
	private Reply replyParent;
	
}
