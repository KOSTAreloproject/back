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
	
	/**
	 * 게시판별 좋아요 리스트 출력 
	 * @param styleNum : 스타일 번호 
	 * @return List<LikesDTO> 
	 * @throws FindException
	 */
	public List<LikesDTO> listByStyleNum(Long styleNum) throws FindException{
		List<Likes> likesList = lr.ListByStyleNum(styleNum);
		
		if(likesList == null) {
			throw new FindException(" 존재하지 않습니다.");
		}
		
		List<LikesDTO> likesDTOList = 
				likesList.stream().map(likes -> modelMapper.map(likes, LikesDTO.class)).collect(Collectors.toList());
		return likesDTOList;
	}
	
	/**
	 * 좋아요 +1 추가 
	 * @param mNum : 회원 번호 
	 * @param styleNum : 스타일 번호 
	 * @throws AddException
	 */
	public void plusLikes(Long mNum, Long styleNum) throws AddException{
		LikesDTO likeDTO = new LikesDTO();
		LikesEmbedded le = new LikesEmbedded();
		le.setMNum(mNum);
		le.setStyleNum(styleNum);
		likeDTO.setLe(le);
		Likes like = likeDTO.toEntity();
		lr.save(like);
	}
	
	/**
	 * 좋아요 삭제 
	 * @param mNum : 회원 번호 
	 * @param styleNum : 스타일 번호 
	 * @throws FindException
	 */
	public void minusLikes(Long mNum, Long styleNum) throws RemoveException, FindException{
		
		Likes l= lr.findBymNumAndStyleNum(mNum, styleNum);
		if(l == null) {
			throw new FindException("존재하지 않습니다.");
		}
		
		lr.deleteLikes(styleNum, mNum);
	}
}
