package com.my.relo.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.my.relo.dto.NoticeDTO;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "m_num")
	private Member member;

	@NotNull
	@Column(name = "n_title")
	private String nTitle;

//	@NotNull
	@Column(name = "n_content")
	private String nContent;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@NotNull
	@Column(name = "n_date")
	private LocalDate nDate;

	/**
	 * 서비스(0) / 작업(1) / 업데이트(2) / 이벤트(3)
	 */
	@NotNull
	@Column(name = "n_category")
	private Integer nCategory;

	@Builder
	public Notice(Integer category, String title, String content, LocalDate date, Member member) {
		this.nCategory = category;
		this.nTitle = title;
		this.nContent = content;
		this.nDate = date;
		this.member = member;
	}

	public void updateNotice(NoticeDTO dto) {
		this.nCategory = dto.getNCategory();
		this.nTitle = dto.getNTitle();
		this.nContent = dto.getNContent();
	}

}
