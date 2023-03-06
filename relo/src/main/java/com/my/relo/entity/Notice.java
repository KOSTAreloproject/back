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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notice")
@SequenceGenerator(name = "notice_sequence_generator", // 제너레이터명
		sequenceName = "notice_seq", // 시퀀스명
		initialValue = 1, // 시작 값
		allocationSize = 1 // 할당할 범위 사이즈
)
public class Notice {
	@Id
	@Column(name = "n_num")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notice_sequence_generator")
	private Long nNum;

	@ManyToOne
	@JoinColumn(name = "m_num")
	private Long mNum;

	@Column(name = "n_title")
	private String nTitle;

	@Column(name = "n_content")
	private String nContent;

	@Temporal(TemporalType.DATE)
	@Column(name = "n_date")
	private Date nDate;

	@Column(name = "n_category")
	private int nCategory;
}
