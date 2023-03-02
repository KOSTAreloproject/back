package com.my.relo.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@DynamicInsert @DynamicUpdate
@Entity
@Table(name = "auction")
@SequenceGenerator(name = "AUCTION_SEQ_GENERATOR", // 사용할 sequence 이름
		sequenceName = "auction_seq", // 실제 데이터베이스 sequence 이름
		initialValue = 1, allocationSize = 1)
public class Auction {
	@Id
	@Column(name = "a_num")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUCTION_SEQ_GENERATOR")
	private Long aNum;
	
	@JoinColumn(name = "m_num", nullable = false, referencedColumnName = "m_num")
	private Long mNum;

	@ManyToOne
	@JoinColumn(name = "p_num", nullable = false)
	private Product product;

	@Column(name = "a_price", nullable = false)
	private int aPrice;

	
	@Column(name = "a_date", nullable = false)
	@ColumnDefault(value = "SYSDATE")
	private LocalDate aDate;

	@OneToOne(mappedBy = "auction")
	private Award award;

}
