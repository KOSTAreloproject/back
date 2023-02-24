package com.my.relo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.my.relo.dto.LikesDTO;
import com.my.relo.dto.ReplyDTO;
import com.my.relo.dto.StyleDTO;
import com.my.relo.dto.StyleTagDTO;
import com.my.relo.entity.Style;
import com.my.relo.entity.StyleTag;
import com.my.relo.entity.StyleTagEmbedded;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.LikesRepository;
import com.my.relo.repository.StyleRepository;
import com.my.relo.repository.StyleTagRepository;

@Service
public class StyleService {
	@Autowired
	private StyleRepository sr;
	
	@Autowired
	private StyleTagRepository str;
	
	@Autowired
	private LikesRepository lr;
	
	@Autowired
	private ReplyService rs;
	
	@Autowired
	private LikesService ls;
	
	@Autowired
	ModelMapper modelMapper;
	
	//게시판 추가 
	public void write(StyleDTO styleDTO) throws AddException{
		List<StyleTagDTO> tagList = styleDTO.getTagList();
		Style style = styleDTO.toEntity();
		sr.save(style);
		Optional<Style> optS1 = sr.findById(style.getStyleNum());
		Style s = optS1.get();
		for(StyleTagDTO t : tagList) {
			String hashName = t.getSte().getHashName();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName(hashName);
			StyleTag tag= new StyleTag(ste,s);
			str.save(tag);
		}
	}
	
	//게시판 삭제
	public void deleteByStyleNum(Long styleNum) throws RemoveException{
		lr.deleteByStyleNumList(styleNum);
		sr.deleteById(styleNum);
	}
	
	//게시판 최신순 리스트 출력
	public List<StyleDTO> listByStyleNum() throws FindException{
		List<Style> styleList = sr.findAll(Sort.by(Sort.Direction.DESC,"styleNum"));
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for(StyleDTO dto : styleDTOList) {
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			list.add(dto);
		}
		return list;
	}
	
	//게시판 해시태그별 리스트 출력
	public List<StyleDTO> listByHashName(String hashName) throws FindException{
		List<Style> styleList = sr.listByHashName(hashName);
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for(StyleDTO dto : styleDTOList) {
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			list.add(dto);
		}
		return list;
	}
	
	//게시판 좋아요순 리스트 출력
	public List<StyleDTO> listByLikes() throws FindException{
		List<Style> styleList = sr.listByLikes();
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for(StyleDTO dto : styleDTOList) {
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			list.add(dto);
		}
		return list;
	}
	
	//게시판 조회수순 리스트 출력
	public List<StyleDTO> listBystyleCnt() throws FindException{
		List<Style> styleList = sr.findAll(Sort.by(Sort.Direction.DESC,"styleCnt","styleNum"));
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for(StyleDTO dto : styleDTOList) {
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			list.add(dto);
		}
		return list;
	}
	
	//내가 쓴 게시판 리스트 출력
	public List<StyleDTO> listByMNum(Long mNum) throws FindException{
		List<Style> styleList = sr.findBymNum(mNum);
		List<StyleDTO> styleDTOList =
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for(StyleDTO dto : styleDTOList) {
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			list.add(dto);
		}
		return list;
	}
	
	//게시판 상세보기 출력
	public StyleDTO styleDetail(Long styleNum) throws FindException{
		Optional<Style> optS = sr.findById(styleNum);
		List <LikesDTO> likes = ls.listByStyleNum(styleNum);
		List <ReplyDTO> repList = rs.listByStyleNum(styleNum);
		Style style = optS.get();
		StyleDTO styleDTO = 
				modelMapper.map(style, StyleDTO.class);
		styleDTO.setLikesList(likes);
		styleDTO.setReplyList(repList);
		
		return styleDTO;
	}
	
	//게시판 수정 
	public void edit(List<StyleTagDTO> list) throws AddException{
		StyleTagDTO st= list.get(0);
		Long styleNum = st.getSte().getStyleNum();
		str.deleteByStyleNum(styleNum);
		Optional<Style> optS= sr.findById(styleNum);
		Style s = optS.get();
		for(StyleTagDTO t : list) {
			String hashName = t.getSte().getHashName();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName(hashName);
			StyleTag tag= new StyleTag(ste,s);
			str.save(tag);
		}
	}
	
	//조회수 증가
	public void plusCnt(Long styleNum) throws AddException{
		sr.updateCnt(styleNum);
	}
}