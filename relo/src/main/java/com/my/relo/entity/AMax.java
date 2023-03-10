package com.my.relo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Immutable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Immutable
@Table(name = "a_max")
@Getter
@ToString
@NoArgsConstructor @AllArgsConstructor
public class AMax {
	@Id
	@Column(name = "p_num")
	private Long pNum;
	
	@Column(name = "max_price")
	private Long MaxPrice;
}
