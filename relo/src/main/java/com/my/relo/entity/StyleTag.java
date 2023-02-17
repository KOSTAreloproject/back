package com.my.relo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString
@Entity 
@Table(name = "style_tag")
public class StyleTag {
	/**
	 * 스타일 태그 
	 */
	@Id
	@Column(name = "hash_name")
	private String hashName;
	
	@ManyToOne
	@JoinColumn(name = "style_num")
	private Style style;
}
