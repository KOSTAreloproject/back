package com.my.relo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.relo.dto.LikesDTO;
import com.my.relo.dto.MemberDTO;
import com.my.relo.dto.ReplyDTO;
import com.my.relo.dto.StyleDTO;
import com.my.relo.dto.StyleTagDTO;
import com.my.relo.entity.Likes;
import com.my.relo.entity.Member;
import com.my.relo.entity.Reply;
import com.my.relo.entity.Style;
import com.my.relo.entity.StyleTag;
import com.my.relo.entity.StyleTagEmbedded;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.LikesRepository;
import com.my.relo.repository.ReplyRepository;
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
	private ReplyRepository rr;
	
	@Autowired
	private LikesService ls;
	
	@Autowired
	ModelMapper modelMapper;
	
	/**
	 * 글 작성 
	 * @param styleDTO
	 * @throws AddException
	 */
	public Long write(StyleDTO styleDTO) throws AddException{
		List<StyleTagDTO> tagList = styleDTO.getTagList();
		MemberDTO m = styleDTO.getMember();
		System.out.println("멤버 >>" +m.getMNum());
		Style style = styleDTO.toEntity(m);
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
		return s.getStyleNum();
	}
	/**
	 * 게시판 삭제 후 나머지 댓글, 좋아요, 해시태그 삭제 
	 * @param styleNum
	 * @throws RemoveException
	 * @throws FindException 
	 */
	public void deleteByStyleNum(Long styleNum) throws RemoveException, FindException{
		
		Optional<Style> optA= sr.findById(styleNum);
		if(!optA.isPresent()) {
			throw new FindException("존재하지 않는 게시물입니다.");
		}
		lr.deleteByStyleNumList(styleNum);
		sr.deleteById(styleNum);
	}
	/**
	 * 최신순 리스트 출력 
	 * @return
	 * @throws FindException
	 */
	public List<StyleDTO> listByStyleNum() throws FindException{
		List<Style> styleList = sr.findAll(Sort.by(Sort.Direction.DESC,"styleNum"));
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for(StyleDTO dto : styleDTOList) {
			Long styleNum = dto.getStyleNum();
			Optional<Style> optS = sr.findById(styleNum);
			Style s = optS.get();
			MemberDTO mDTO = MemberDTO.builder().id(s.getMember().getId()).build();
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			dto.setMember(mDTO);
			list.add(dto);
		}
		return list;
	}
	/**
	 * 해시태그 별 리스트 출력 
	 * @param hashName
	 * @return
	 * @throws FindException
	 */
	public List<StyleDTO> listByHashName(String hashName) throws FindException{
		List<Style> styleList = sr.listByHashName(hashName);
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for(StyleDTO dto : styleDTOList) {
			Long styleNum = dto.getStyleNum();
			Optional<Style> optS = sr.findById(styleNum);
			Style s = optS.get();
			MemberDTO mDTO = MemberDTO.builder().id(s.getMember().getId()).build();
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			dto.setMember(mDTO);
			list.add(dto);
		}
		return list;
	}
	
	/**
	 * 좋아요 리스트 출력 
	 * @return List<StyleDTO>
	 * @throws FindException
	 */
	public List<StyleDTO> listByLikes() throws FindException{
		List<Style> styleList = sr.listByLikes();
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for(StyleDTO dto : styleDTOList) {
			Long styleNum = dto.getStyleNum();
			Optional<Style> optS = sr.findById(styleNum);
			Style s = optS.get();
			MemberDTO mDTO = MemberDTO.builder().id(s.getMember().getId()).build();
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			dto.setMember(mDTO);
			list.add(dto);
		}
		return list;
	}
	
	/**
	 * 조회수순 리스트 출력 
	 * @return List<StyleDTO>
	 * @throws FindException
	 */
	public List<StyleDTO> listBystyleCnt() throws FindException{
		List<Style> styleList = sr.findAll(Sort.by(Sort.Direction.DESC,"styleCnt","styleNum"));
		List<StyleDTO> styleDTOList = 
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for(StyleDTO dto : styleDTOList) {
			Long styleNum = dto.getStyleNum();
			Optional<Style> optS = sr.findById(styleNum);
			Style s = optS.get();
			MemberDTO mDTO = MemberDTO.builder().id(s.getMember().getId()).build();
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			dto.setMember(mDTO);
			list.add(dto);
		}
		return list;
	}
	
	/**
	 * 내가 쓴 리스트 출력 
	 * @param mNum 
	 * @return
	 * @throws FindException
	 */
	public List<StyleDTO> listByMNum(Long mNum) throws FindException{
		List<Style> styleList = sr.findBymNum(mNum);
		List<StyleDTO> styleDTOList =
				styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class)).collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for(StyleDTO dto : styleDTOList) {
			Long styleNum = dto.getStyleNum();
			Optional<Style> optS = sr.findById(styleNum);
			Style s = optS.get();
			MemberDTO mDTO = MemberDTO.builder().id(s.getMember().getId()).build();
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			dto.setMember(mDTO);
			list.add(dto);
		}
		return list;
	}
	
	/**
	 * 게시판 상세보기 출력 
	 * @param styleNum
	 * @return
	 * @throws FindException
	 */
	@Transactional
	public StyleDTO styleDetail(Long styleNum) throws FindException{
		
		Optional<Style> optS = sr.findById(styleNum);
		Style style = optS.get();
		StyleDTO sDTO = new StyleDTO();
		MemberDTO mDTO1 = 
				MemberDTO.builder().mNum(style.getMember().getMNum())
						.id(style.getMember().getId()).build();
		
		sDTO.setMember(mDTO1);
		sDTO.setStyleNum(style.getStyleNum());
		sDTO.setStyleDate(style.getStyleDate());
		
		List<Reply> list= rr.findByStyleNum(styleNum);
		List<ReplyDTO> listDTO = new ArrayList<>();
		for(Reply r: list) {
			ReplyDTO rDTO = new ReplyDTO();
			
			Member m = r.getMember();
			MemberDTO mDTO =
					MemberDTO.builder().id(m.getId()).build();
			
			rDTO.setRepNum(r.getRepNum());
			rDTO.setRepContent(r.getRepContent());
			rDTO.setRepDate(r.getRepDate());
			rDTO.setMember(mDTO);
			System.out.println("아이디 -> " + mDTO.getId());
			
			listDTO.add(rDTO);
		}
		
		sDTO.setReplyList(listDTO);
		sDTO.setStyleCnt(style.getStyleCnt());

		List<Likes> likeEntityList = style.getLikeList();
		List<LikesDTO> likesDTOList = 
				likeEntityList.stream().map(likes -> modelMapper.map(likes, LikesDTO.class)).collect(Collectors.toList());
		
		sDTO.setLikesList(likesDTOList);
		
		List<StyleTag> tagEntityList = style.getTagList();
		List<StyleTagDTO> tagDTOList = 
				tagEntityList.stream().map(tags -> modelMapper.map(tags, StyleTagDTO.class)).collect((Collectors.toList()));
		
		sDTO.setTagList(tagDTOList);

		return sDTO;
	}
	
	/**
	 * 게시판 수정
	 * @param StyleDTO
	 * @throws AddException
	 * @throws FindException 
	 */
	public void edit(StyleDTO styleDTO) throws AddException, FindException{
		List <StyleTagDTO> list = styleDTO.getTagList();
		if(list == null) {
			throw new FindException("존재하지 않는 게시물입니다.");
		}
		Long styleNum = styleDTO.getStyleNum();
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
	
	/**
	 * 상세조회 클릭시 조회수 증가 
	 * @param styleNum
	 * @throws AddException
	 */
	public void plusCnt(Long styleNum) throws AddException{
		sr.updateCnt(styleNum);
	}
	
	/**
	 * 이미지 리스트 출력 
	 * @param list
	 * @return List<Long>
	 */
	public List<Long> imgStyleNum(List<StyleDTO> list) throws FindException{
		List<Long> imgList = new ArrayList<>();
		for(int i = 0; i < list.size(); i++) {
			Long styleNum = list.get(i).getStyleNum();
			imgList.add(styleNum);
		}
		return imgList;
	}
}