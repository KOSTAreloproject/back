package com.my.relo.entity;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
public class LikesEmbedded implements Serializable{
	
	@Column(name = "style_num")
	private Long styleNum;
	
	@Column(name = "m_num")
	private Long mNum;
}
