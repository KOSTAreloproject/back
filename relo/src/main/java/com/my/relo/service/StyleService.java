package com.my.relo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	 * 
	 * @param styleDTO
	 * @throws AddException
	 */
	public Long write(StyleDTO styleDTO) throws AddException {
		List<StyleTagDTO> tagList = styleDTO.getTagList();
		MemberDTO m = styleDTO.getMember();
		Style style = styleDTO.toEntity(m);
		sr.save(style);
		Optional<Style> optS1 = sr.findById(style.getStyleNum());
		Style s = optS1.get();
		for (StyleTagDTO t : tagList) {
			String hashName = t.getSte().getHashName();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName(hashName);
			StyleTag tag = new StyleTag(ste, s);
			str.save(tag);
		}
		return s.getStyleNum();
	}

	/**
	 * 게시판 삭제 후 나머지 댓글, 좋아요, 해시태그 삭제
	 * 
	 * @param styleNum
	 * @throws RemoveException
	 * @throws FindException
	 */
	public void deleteByStyleNum(Long styleNum) throws RemoveException, FindException {

		Optional<Style> optA = sr.findById(styleNum);
		if (!optA.isPresent()) {
			throw new FindException("존재하지 않는 게시물입니다.");
		}
		lr.deleteByStyleNumList(styleNum);
		sr.deleteById(styleNum);
	}

	/**
	 * 최신순 리스트 출력
	 * 
	 * @return map
	 * @throws FindException
	 */
	public Map<String, Object> listByStyleNum() throws FindException {
		Map<String, Object> map = new HashMap<>();
		List<Style> styleList = sr.findAll(Sort.by(Sort.Direction.DESC, "styleNum"));
		List<StyleDTO> styleDTOList = styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class))
				.collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for (StyleDTO dto : styleDTOList) {
			Long styleNum = dto.getStyleNum();
			Optional<Style> optS = sr.findById(styleNum);
			Style s = optS.get();
			MemberDTO mDTO = MemberDTO.builder().id(s.getMember().getId()).mnum(s.getMember().getMNum()).build();
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			dto.setMember(mDTO);
			list.add(dto);
		}
		List<String> tagList = str.ListByCnt();
		map.put("list", list);
		map.put("tagList", tagList);

		return map;
	}

	/**
	 * 해시태그 별 리스트 출력
	 * 
	 * @param hashName
	 * @return map
	 * @throws FindException
	 */
	public Map<String, Object> listByHashName(String hashName) throws FindException {
		Map<String, Object> map = new HashMap<>();
		List<Style> styleList = sr.listByHashName(hashName);
		List<StyleDTO> styleDTOList = styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class))
				.collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for (StyleDTO dto : styleDTOList) {
			Long styleNum = dto.getStyleNum();
			Optional<Style> optS = sr.findById(styleNum);
			Style s = optS.get();
			MemberDTO mDTO = MemberDTO.builder().id(s.getMember().getId()).mnum(s.getMember().getMNum()).build();
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			dto.setMember(mDTO);
			list.add(dto);
		}
		List<String> tagList = str.ListByCnt();
		map.put("list", list);
		map.put("tagList", tagList);

		return map;
	}

	/**
	 * 좋아요 리스트 출력
	 * 
	 * @return map
	 * @throws FindException
	 */
	public Map<String, Object> listByLikes() throws FindException {
		Map<String, Object> map = new HashMap<>();
		List<Style> styleList = sr.listByLikes();
		List<StyleDTO> styleDTOList = styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class))
				.collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for (StyleDTO dto : styleDTOList) {
			Long styleNum = dto.getStyleNum();
			Optional<Style> optS = sr.findById(styleNum);
			Style s = optS.get();
			MemberDTO mDTO = MemberDTO.builder().id(s.getMember().getId()).mnum(s.getMember().getMNum()).build();
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			dto.setMember(mDTO);
			list.add(dto);
		}
		List<String> tagList = str.ListByCnt();
		map.put("list", list);
		map.put("tagList", tagList);

		return map;
	}

	/**
	 * 조회수순 리스트 출력
	 * 
	 * @return map
	 * @throws FindException
	 */
	public Map<String, Object> listBystyleCnt() throws FindException {
		Map<String, Object> map = new HashMap<>();
		List<Style> styleList = sr.findAll(Sort.by(Sort.Direction.DESC, "styleCnt", "styleNum"));
		List<StyleDTO> styleDTOList = styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class))
				.collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for (StyleDTO dto : styleDTOList) {
			Long styleNum = dto.getStyleNum();
			Optional<Style> optS = sr.findById(styleNum);
			Style s = optS.get();
			MemberDTO mDTO = MemberDTO.builder().id(s.getMember().getId()).mnum(s.getMember().getMNum()).build();
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			dto.setMember(mDTO);
			list.add(dto);
		}
		List<String> tagList = str.ListByCnt();
		map.put("list", list);
		map.put("tagList", tagList);

		return map;
	}

	/**
	 * 내가 쓴 리스트 출력
	 * 
	 * @param mNum
	 * @return map
	 * @throws FindException
	 */
	public Map<String, Object> listByMNum(Long mNum) throws FindException {
		Map<String, Object> map = new HashMap<>();
		List<Style> styleList = sr.findBymNum(mNum);
		List<StyleDTO> styleDTOList = styleList.stream().map(style -> modelMapper.map(style, StyleDTO.class))
				.collect(Collectors.toList());
		List<StyleDTO> list = new ArrayList<>();
		for (StyleDTO dto : styleDTOList) {
			Long styleNum = dto.getStyleNum();
			Optional<Style> optS = sr.findById(styleNum);
			Style s = optS.get();
			MemberDTO mDTO = MemberDTO.builder().id(s.getMember().getId()).mnum(s.getMember().getMNum()).build();
			List<LikesDTO> likeList = ls.listByStyleNum(dto.getStyleNum());
			dto.setLikesList(likeList);
			dto.setMember(mDTO);
			list.add(dto);
		}
		List<String> tagList = str.ListByCnt();
		map.put("list", list);
		map.put("tagList", tagList);

		return map;
	}

	/**
	 * 게시판 상세보기 출력
	 * 
	 * @param styleNum
	 * @return StyleDTO
	 * @throws FindException
	 */
	@Transactional
	public StyleDTO styleDetail(Long styleNum) throws FindException {

		Optional<Style> optS = sr.findById(styleNum);
		Style style = optS.get();
		if (style == null) {
			throw new FindException("존재하지 않는 게시물입니다.");
		}
		StyleDTO sDTO = new StyleDTO();
		MemberDTO mDTO1 = MemberDTO.builder().mnum(style.getMember().getMNum()).id(style.getMember().getId()).build();

		sDTO.setMember(mDTO1);
		sDTO.setStyleNum(style.getStyleNum());
		sDTO.setStyleDate(style.getStyleDate());

		List<Reply> list = rr.findByStyleNum(styleNum);

		List<ReplyDTO> listDTO = new ArrayList<>();
		for (Reply r : list) {
			ReplyDTO rDTO = new ReplyDTO();
			Reply p = r.getReplyParent();
			if (p != null) {

				Long pRepNum = p.getRepNum();
				Optional<Reply> optP = rr.findById(pRepNum);
				Reply pRep = optP.get();
				ReplyDTO pDTO = modelMapper.map(pRep, ReplyDTO.class);
				rDTO.setReplyParentDTO(pDTO);
			}

			Member m = r.getMember();
			MemberDTO mDTO = MemberDTO.builder().id(m.getId()).mnum(m.getMNum()).build();

			rDTO.setRepNum(r.getRepNum());
			rDTO.setRepContent(r.getRepContent());
			rDTO.setRepDate(r.getRepDate());
			rDTO.setMember(mDTO);

			listDTO.add(rDTO);
		}

		sDTO.setReplyList(listDTO);
		sDTO.setStyleCnt(style.getStyleCnt());

		List<Likes> likeEntityList = style.getLikeList();
		List<LikesDTO> likesDTOList = likeEntityList.stream().map(likes -> modelMapper.map(likes, LikesDTO.class))
				.collect(Collectors.toList());

		sDTO.setLikesList(likesDTOList);

		List<StyleTag> tagEntityList = style.getTagList();
		List<StyleTagDTO> tagDTOList = tagEntityList.stream().map(tags -> modelMapper.map(tags, StyleTagDTO.class))
				.collect((Collectors.toList()));

		sDTO.setTagList(tagDTOList);

		return sDTO;
	}

	/**
	 * 게시판 수정
	 * 
	 * @param StyleDTO
	 * @throws AddException
	 * @throws FindException
	 */
	public void edit(StyleDTO styleDTO) throws AddException, FindException {

		Long styleNum = styleDTO.getStyleNum();
		Optional<Style> optS = sr.findById(styleNum);
		Style s = optS.get();
		if (s == null) {
			throw new FindException("존재하지 않는 게시물입니다.");
		}
		str.deleteByStyleNum(styleNum);
		List<StyleTagDTO> list = styleDTO.getTagList();
		for (StyleTagDTO t : list) {
			String hashName = t.getSte().getHashName();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName(hashName);
			StyleTag tag = new StyleTag(ste, s);
			str.save(tag);
		}
	}

	/**
	 * 상세조회 클릭시 조회수 증가
	 * 
	 * @param styleNum
	 * @throws AddException
	 */
	public void plusCnt(Long styleNum) throws AddException {
		sr.updateCnt(styleNum);
	}

}