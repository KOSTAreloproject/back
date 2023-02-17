package com.my.relo.entity;

import java.io.Serializable;

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
@Table(name = "likes")
public class Likes implements Serializable{
	/**
	 * 좋아요 
	 */
	@Id
	@ManyToOne
	@JoinColumn(name = "style_num")
	private Style styleLike;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "m_num")
	private Member memberLike;
}
