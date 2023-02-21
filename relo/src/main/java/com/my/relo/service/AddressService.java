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
	
	//주소 추가
	public void write(Address address) throws AddException{
		Integer type = address.getAddrType();
		if(type == 0) {
			Address defaultaddr = ar.findByAddrType(address.getMNum());
			if(defaultaddr != null) {
				defaultaddr.setAddrType(1);
				ar.save(defaultaddr);
			}
		}
		ar.save(address);
	}
	
	//해당 주소 삭제 
	public void deleteByAddrNum(Long addrNum) throws RemoveException{
		ar.deleteById(addrNum);
	}
	
	//주소 수정
	public void updateByAddrNum(Address address) throws FindException{
		Optional<Address> optA = ar.findById(address.getAddrNum());
		Address a = optA.get();
		
		a.setAddr(address.getAddr());
		a.setAddrDetail(address.getAddrDetail());
		a.setAddrName(address.getAddrName());
		a.setAddrPostNum(address.getAddrPostNum());
		a.setAddrRecipient(address.getAddrRecipient());
		a.setAddrTel(address.getAddrTel());
		a.setAddrType(address.getAddrType());
		Integer type = a.getAddrType();
		if(type == 0) {
			Address defaultaddr = ar.findByAddrType(a.getMNum());
			if(defaultaddr != null) {
				defaultaddr.setAddrType(1);
				ar.save(defaultaddr);
			}
		}
		ar.save(a);
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
		AddressDTO addrDTO = modelMapper.map(addr, AddressDTO.class);
		return addrDTO;
	}
	
}
