package com.my.relo.dto;

import com.my.relo.entity.Likes;
import com.my.relo.entity.LikesEmbedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor@ToString
public class LikesDTO {

	private LikesEmbedded le = new LikesEmbedded();

	public Likes toEntity() {
		return Likes.builder().le(le).build();
	}
}
