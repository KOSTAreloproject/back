package com.my.relo.entity;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class StyleTagEmbedded implements Serializable{

	@Column(name = "hash_name", nullable = false)
	private String hashName;
	
	private Long styleNum;
}