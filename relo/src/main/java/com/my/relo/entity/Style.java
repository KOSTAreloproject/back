package com.my.relo.entity;

import java.util.Date;
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
@Table(name = "style")
@SequenceGenerator(name = "style_sequence_generator",
					sequenceName = "style_seq",
					initialValue = 1,
					allocationSize = 1)
public class Style {
	/**
	 * 스타일 게시판 
	 */
	@Id
	@Column(name = "style_num", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "style_sequence_generator")
	private Long styleNum;
	
	@ManyToOne
	@JoinColumn(name = "m_num", nullable = false)
	private Member member;

	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "style_date")
	@CreationTimestamp
	private Date styleDate;
	
	@Column(name = "style_cnt")
	@ColumnDefault(value = "0")
	private int styleCnt;
	
	@OneToMany(mappedBy = "style",
				cascade = CascadeType.REMOVE
				)
	private List<Reply> repList;
	                
	@OneToMany(mappedBy = "style",
			cascade = CascadeType.REMOVE,
			fetch = FetchType.EAGER
			)
	private List<StyleTag> tagList;
	
//	@OneToMany(mappedBy = "style",
//			cascade = CascadeType.REMOVE
//				)
//	@JoinColumn(name = "style_num")
//	private List<Likes> likeList;
}
