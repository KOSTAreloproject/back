package com.my.relo.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
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
	
	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "rep_date")
	@CreationTimestamp
	private Date repDate;
	
	@ManyToOne
	@JoinColumn(name = "rep_parent")
	@ColumnDefault(value = "0")
	private Reply replyParent;
	
	@OneToMany(mappedBy = "replyParent",
				cascade = CascadeType.REMOVE
				)
	private List<Reply> children;
	
}
