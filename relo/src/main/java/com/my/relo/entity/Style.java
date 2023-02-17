package com.my.relo.entity;

import java.util.Date;
import java.util.List;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
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
	@Column(name = "style_num")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "style_sequence_generator")
	private Long styleNum;
	
	@ManyToOne
	@JoinColumn(name = "m_num")
	private Member member;

	@Column(name = "style_date")
	private Date styleDate;
	
	@Column(name = "style_likes")
	private int styleLikes;
	
	@Column(name = "style_cnt")
	private int styleCnt;
	
	@OneToMany(mappedBy = "styleRep")
	private List<Reply> repList;
	
	@OneToMany(mappedBy = "style")
	private List<StyleTag> tagList;
	
	@OneToMany(mappedBy = "styleLike")
	private List<Likes> likeList;
}
