package com.my.relo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.my.relo.dto.ZPResponseDTO;
import com.my.relo.entity.Zzim;
import com.my.relo.entity.ZzimEmbedded;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.ZzimRepository;

@Service
public class ZzimService {

	@Autowired
	private ZzimRepository zr;

	/**
	 * 회원의 찜목록을 본다
	 * 
	 * @param mNum : 회원번호
	 * @return 회원의 찜 목록
	 * @throws FindException
	 */
	public Map<String, Object> readZzimList(Long mNum, int currentPage) throws FindException {
		Pageable pb = PageRequest.of((currentPage - 1), 10);
		Page<Object[]> resultList = zr.findByMNum(mNum, pb);
		List<Object[]> list = resultList.getContent();
		int totalpage = resultList.getTotalPages();
		System.out.println();
		if (list.isEmpty()) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("msg", "찜 목록이 없습니다.");
			return resultMap;
		}
		List<ZPResponseDTO> dtos = new ArrayList<>();
		for (Object[] objs : list) {
			ZPResponseDTO dto = ZPResponseDTO.builder().mnum(Long.valueOf(String.valueOf(objs[0])))
					.pNum(Long.valueOf(String.valueOf(objs[1]))).sNum(Long.valueOf(String.valueOf(objs[2])))
					.sBrand((String) objs[3]).sName((String) objs[4]).sType((String) objs[5])
					.sizeCategoryName((String) objs[6]).sColor((String) objs[7])
					.sHopePrice(Integer.parseInt(String.valueOf(objs[8]))).sGrade((String) objs[9])
					.pendDate(LocalDateTime.parse(String.valueOf(objs[10]),
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")))
					.maxPrice(Integer.parseInt(String.valueOf(objs[11]))).build();
			dtos.add(dto);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", dtos);
		resultMap.put("totalpage", totalpage);
		resultMap.put("totalcnt", dtos.size());
		return resultMap;

	}

	/**
	 * 내 찜목록에 있는지 확인한다.
	 * 
	 * @param mNum : 회원번호
	 * @param pNum : 상품번호
	 * @throws FindException
	 */

	public int checkExistlist(Long mNum, Long pNum) throws FindException {
		ZzimEmbedded ze = ZzimEmbedded.builder().mnum(mNum).pNum(pNum).build();
		Optional<Zzim> optZ = zr.findById(ze);
		if (optZ.isPresent()) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 회원의 찜목록에 추가한다
	 * 
	 * @param mNum : 회원번호
	 * @param pNum : 상품번호
	 * @throws AddException
	 */
	public void createZzimList(Long mNum, Long pNum) throws AddException {
		ZzimEmbedded ze = ZzimEmbedded.builder().mnum(mNum).pNum(pNum).build();
		Optional<Zzim> optZ = zr.findById(ze);
		if (optZ.isPresent()) {
			throw new AddException("이미 찜하기에 존재합니다");
		} else {
			zr.insertZzim(mNum, pNum);
		}
	}

	/**
	 * 회원의 찜목록에서 삭제한다
	 * 
	 * @param mNum : 회원번호
	 * @param pNum : 상품번호
	 * @throws RemoveException
	 */
	public void deleteZzimList(Long mNum, Long pNum) throws RemoveException {
		zr.deleteZzim(mNum, pNum);
	}

	/**
	 * 상품이 삭제되었을 때 모든 회원에게서 찜목록을 삭제한다
	 * 
	 * @throws RemoveException
	 */
	public void deleteZzimAll(Long pNum) throws RemoveException {
		zr.deleteZzimProd(pNum);
	}
}