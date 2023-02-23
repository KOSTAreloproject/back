package com.my.relo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.ZzimDTO;
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

	public List<ZzimDTO> readZzimList(Long mNum) throws FindException{
		List<Object[]> list = zr.findByMNum(mNum);
		if(list.isEmpty()) {
			throw new FindException("찜 목록이 없습니다");
		}
		List<ZzimDTO> dtos = new ArrayList<>();
		for (Object[] objs : list) {
			ZzimDTO dto = ZzimDTO.builder().mNum(Integer.parseInt(String.valueOf(objs[0])))
					.pNum(Integer.parseInt(String.valueOf(objs[1]))).sBrand((String) objs[2]).sName((String) objs[3])
					.sType((String) objs[4]).sizeCategoryName((String) objs[5]).sColor((String) objs[6])
					.hopePrice(Integer.parseInt(String.valueOf(objs[7]))).sGrade((String) objs[8])
					.pEndDate((Date) objs[9]).maxPrice(Integer.parseInt(String.valueOf(objs[10]))).build();
			dtos.add(dto);
		}
		return dtos;
	}

	public void createZzimList(Long mNum, Long pNum) throws AddException {
		ZzimEmbedded ze = ZzimEmbedded.builder().mNum(mNum).pNum(pNum).build();
		Optional<Zzim> optZ = zr.findById(ze);
		System.out.println(optZ);
		if (optZ.isPresent()) {
			throw new AddException("이미 찜하기에 존재합니다");
		} else {
			zr.insertZzim(mNum, pNum);
		}
	}

	public void deleteZzimList(Long mNum, Long pNum) throws RemoveException{
		zr.deleteZzim(mNum, pNum);
	}

	public void deleteZzimAll() throws RemoveException{
		zr.deleteAll();
	}
}
