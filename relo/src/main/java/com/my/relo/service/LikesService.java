package com.my.relo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.LikesDTO;
import com.my.relo.entity.Likes;
import com.my.relo.entity.LikesEmbedded;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.LikesRepository;

@Service
public class LikesService {
	@Autowired
	private LikesRepository lr;
	
	@Autowired
	ModelMapper modelMapper;
	
	//게시판별 좋아요 리스트 출력
	public List<LikesDTO> listByStyleNum(Long styleNum) throws FindException{
		List<Likes> likesList = lr.ListByStyleNum(styleNum);
		List<LikesDTO> likesDTOList = 
				likesList.stream().map(likes -> modelMapper.map(likes, LikesDTO.class)).collect(Collectors.toList());
		return likesDTOList;
	}
	
	//좋아요 +1 
	public void plusLikes(Long mNum, Long styleNum) throws AddException{
		Likes like = new Likes();
		LikesEmbedded le = new LikesEmbedded();
		le.setMNum(mNum);
		le.setStyleNum(styleNum);
		like.setLe(le);
		lr.save(like);
	}
	
	//좋아요 -1
	public void minusLikes(Long mNum, Long styleNum) throws RemoveException{
		lr.deleteLikes(styleNum, mNum);
	}
}
