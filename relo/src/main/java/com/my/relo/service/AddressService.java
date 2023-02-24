package com.my.relo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.AddressDTO;
import com.my.relo.entity.Address;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.AddressRepository;

@Service
public class AddressService {
	@Autowired
	private AddressRepository ar;
	
	@Autowired
	ModelMapper modelMapper;
	
	//주소 추가 - 수정 
	public void write(AddressDTO addrDTO) throws AddException{
		Integer type = addrDTO.getAddrType();
		if(type == 0) {
			Address defaultaddr = ar.findByAddrType(addrDTO.getMNum());
			if(defaultaddr != null) {
				ar.updateAddrType(defaultaddr.getAddrNum());
			}
		}
		Address a = addrDTO.toEntity();
		ar.save(a);
	}
	
	//해당 주소 삭제 
	public void deleteByAddrNum(Long addrNum) throws RemoveException{
		ar.deleteById(addrNum);
	}
	
	//해당 회원 주소록 리스트 출력 
	public List<AddressDTO> findByMNum(Long mNum) throws FindException{
		List<Address> addrList = ar.findBymNum(mNum);
		List<AddressDTO> addrDTOList =
				addrList.stream().map(Address -> modelMapper.map(Address, AddressDTO.class)).collect(Collectors.toList());
		return addrDTOList;
	}
	
	//주소 번호로 해당 주소 출력
	public AddressDTO findByAddrNum(Long addrNum) throws FindException{
		Optional<Address> addr = ar.findById(addrNum);
		Address a = addr.get();
		AddressDTO addrDTO = modelMapper.map(a, AddressDTO.class);
		return addrDTO;
	}
	
}
