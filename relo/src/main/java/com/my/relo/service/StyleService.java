package com.my.relo.service;

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
	public void write(Style style) throws AddException{
		List<StyleTag> tagList = style.getTagList();
		sr.save(style);
		for(StyleTag t : tagList) {
			String hashName = t.getSte().getHashName();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName(hashName);
			Optional<Style> optS1 = sr.findById(style.getStyleNum());
			Style s = optS1.get();
			t.setStyle(s);
			t.setSte(ste);
			str.save(t);
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
		return styleDTOList;
	}
	
	//게시판 해시태그별 리스트 출력
	public List<StyleDTO> listByHashName(String hashName) throws FindException{
		List<Style> styleList = sr.listByHashName(hashName);
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		return styleDTOList;
	}
	
	//게시판 좋아요순 리스트 출력
	public List<StyleDTO> listByLikes() throws FindException{
		List<Style> styleList = sr.listByLikes();
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		return styleDTOList;
	}
	
	//게시판 조회수순 리스트 출력
	public List<StyleDTO> listBystyleCnt() throws FindException{
		List<Style> styleList = sr.findAll(Sort.by(Sort.Direction.DESC,"styleCnt","styleNum"));
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		return styleDTOList;
	}
	
	//내가 쓴 게시판 리스트 출력
	public List<StyleDTO> listByMNum(Long mNum) throws FindException{
		List<Style> styleList = sr.findBymNum(mNum);
		List<StyleDTO> styleDTOList =
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		return styleDTOList;
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
	public void edit(Style style) throws AddException{
		Style editStyle = style;
		Long styleNum = editStyle.getStyleNum();
		List<StyleTag> editTagList = editStyle.getTagList();
		Optional<Style> optS = sr.findById(styleNum);
		Style originStyle = optS.get();
		str.deleteByStyleNum(styleNum);
		StyleTag editTag = new StyleTag();
		for(StyleTag t : editTagList) {
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName(t.getSte().getHashName());
			editTag.setSte(ste);
			editTag.setStyle(originStyle);
			str.save(editTag);
		}
	}
	
	//조회수 증가
	public void plusCnt(Long styleNum) throws AddException{
		sr.updateCnt(styleNum);
	}
}
