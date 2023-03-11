package com.my.relo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.my.relo.dto.NoticeDTO;
import com.my.relo.entity.Notice;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.NoticeRepository;
 
@Service
public class NoticeService {

	@Autowired
	private NoticeRepository nr;

	// 추가
	public Long addNotice(NoticeDTO dto) throws AddException {
		Notice n = dto.toEntity();
		nr.save(n);
		return n.getNNum();
	}

	// 제목으로 검색
	public List<NoticeDTO> searchByTitle(String title) throws FindException {
		List<Object[]> list = nr.findBynTitleLike(title);
		List<NoticeDTO> collect = new ArrayList<>();

		for (Object[] objs : list) {
			NoticeDTO dto = NoticeDTO.builder().nNum((Long) objs[0]).title(String.valueOf(objs[1]))
					.category((Integer) objs[2]).build();
			collect.add(dto);
		}

		return collect;
	}

	// 카테고리로 검색
	public List<NoticeDTO> searchByCategory(int category) throws FindException {
		List<Object[]> list = nr.findBynCategory(category);
		List<NoticeDTO> collect = new ArrayList<>();

		for (Object[] objs : list) {
			NoticeDTO dto = NoticeDTO.builder().nNum((Long) objs[0]).title(String.valueOf(objs[1]))
					.category((Integer) objs[2]).build();

			collect.add(dto);
		}

		return collect;
	}

	// 페이징
	public Map<String, Object> pagingNotice(int currentPage) throws FindException {
		Pageable sortedByUserIdDesc = PageRequest.of(currentPage - 1, 5, Sort.by("nNum").descending());

		Page<Notice> p = nr.findAll(sortedByUserIdDesc);
		List<Notice> list = p.getContent();
		int totalPage = p.getTotalPages();

		List<NoticeDTO> collect = new ArrayList<>();
		for (Notice n : list) {
			NoticeDTO dto = NoticeDTO.builder().category(n.getNCategory()).title(n.getNTitle()).nNum(n.getNNum())
					.build();
			collect.add(dto);
		}

		Map<String, Object> result = new HashMap<>();
		result.put("totalPage", totalPage);
		result.put("collect", collect);

		return result;
	}

	// 세부 공지사항 조회
	public NoticeDTO detailNotice(Long nNum) throws FindException {
		Optional<Notice> optN = nr.findById(nNum);
		if (!optN.isPresent())
			throw new FindException();
		else {
			Notice n = optN.get();
			NoticeDTO dto = NoticeDTO.builder().title(n.getNTitle()).content(n.getNContent()).category(n.getNCategory())
					.date(n.getNDate()).build();
			return dto;
		}
	}

	// 이전 글 검색
	public NoticeDTO searchPre(Long nNum) {
		List<Object[]> n = nr.findPrevNotice(nNum);

		if (n.size() == 0) {
			return null;
		} else {
			Long preNNum = Long.valueOf(String.valueOf(n.get(0)[0]));
			String preTitle = String.valueOf(n.get(0)[1]);

			NoticeDTO dto = NoticeDTO.builder().nNum(preNNum).title(preTitle).build();
			return dto;
		}
	}

	// 다음 글 검색
	public NoticeDTO searchNext(Long nNum) {
		List<Object[]> n = nr.findNextNotice(nNum);

		if (n.size() == 0) {
			return null;
		} else {
			Long nextNNum = Long.valueOf(String.valueOf(n.get(0)[0]));
			String nextTitle = String.valueOf(n.get(0)[1]);

			NoticeDTO dto = NoticeDTO.builder().nNum(nextNNum).title(nextTitle).build();
			return dto;
		}
	}

	// 수정
	public void updateNotice(NoticeDTO notice) throws FindException {

		Optional<Notice> optN = nr.findById(notice.getNNum());
		if (!optN.isPresent())
			throw new FindException();
		else {
			Notice n = optN.get();

			if (notice.getNCategory() == null) {
				notice = NoticeDTO.builder().nNum(notice.getNNum()).title(notice.getNTitle())
						.content(notice.getNContent()).category(n.getNCategory()).build();
			}

			if (notice.getNTitle() == null) {
				notice = NoticeDTO.builder().nNum(notice.getNNum()).title(n.getNTitle()).content(notice.getNContent())
						.category(notice.getNCategory()).build();
			}

			if (notice.getNContent() == null) {
				notice = NoticeDTO.builder().nNum(notice.getNNum()).title(notice.getNTitle()).content(n.getNContent())
						.category(notice.getNCategory()).build();
			}

			n.updateNotice(notice);

			nr.save(n);
		}
	}

	// 삭제
	public void deleteNotice(Long nNum) throws RemoveException, FindException {
		Optional<Notice> optN = nr.findById(nNum);

		if (!optN.isPresent())
			throw new FindException();
		else {
			Notice n = optN.get();
			nr.deleteById(n.getNNum());
		}
	}
}
