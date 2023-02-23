package com.my.relo.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString

@DynamicInsert @DynamicUpdate
@Entity
@Table(name="award")
public class Award implements Serializable{
	
	@Id
	@NotNull
	@Column(name="a_num")
	private Long aNum; 
	
	@Column(name = "a_time", nullable = false)
	@ColumnDefault(value = "SYSDATE")
	private LocalDate aTime;
	
	@OneToOne(optional=true) // OneToONe 은 Wrapper 나 기본 자료형은 안된다, mappedBy랑 변수명이랑 같아야 하나
	@MapsId //@MapsId 는 @id로 지정한 컬럼에 @OneToOne 이나 @ManyToOne 관계를 매핑시키는 역할
	@JoinColumn(name="a_num")
	private Auction auction;
}
