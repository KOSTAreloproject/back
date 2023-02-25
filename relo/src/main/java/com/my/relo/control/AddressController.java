package com.my.relo.control;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.AddressDTO;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.service.AddressService;

@RestController
@RequestMapping("address/*")
public class AddressController {
	@Autowired
	private AddressService service;

	/**
	 * 리스트 출력
	 * @param mNum
	 * @return
	 * @throws FindException
	 */
	@GetMapping(value = "{mNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAddrList(@PathVariable("mNum") Long mNum) throws FindException{
		 List<AddressDTO> list = service.findByMNum(mNum);
		 return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	/**
	 * 주소 하나 출력 
	 * @param addrNum
	 * @return
	 * @throws FindException
	 */
	@GetMapping(value = "select/{addrNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAddrNum(@PathVariable("addrNum") Long addrNum) throws FindException{
		AddressDTO dto = service.findByAddrNum(addrNum);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	/**
	 * 주소 추가 
	 * @param session
	 * @param addressDTO
	 * @return
	 * @throws AddException
	 */
	@PostMapping(value="add",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addAddr(HttpSession session,@RequestBody AddressDTO addressDTO) throws AddException{

//		Long logined = (Long)session.getAttribute("logined");
//		if(logined == null) {//로그인 안한 경우
//			throw new AddException("로그인하세요");
//		}
		Long logined = 2L;
		addressDTO.setMNum(logined);
		
		service.write(addressDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	/**
	 * 주소 수정
	 * @param addrNum
	 * @param addressDTO
	 * @param session
	 * @return
	 * @throws AddException
	 * @throws FindException
	 */
	@PutMapping(value = "{addrNum}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateAddr(@PathVariable("addrNum") Long addrNum,
			@RequestBody AddressDTO addressDTO,HttpSession session) 
												throws AddException, FindException{

//		Long logined = (Long)session.getAttribute("logined");
//		if(logined == null) {//로그인 안한 경우
//			throw new AddException("로그인하세요");
//		}
		Long logined = 2L;
		addressDTO.setMNum(logined);
		
		addressDTO.setAddrNum(addrNum);
		service.write(addressDTO);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	/**
	 * 주소 삭제 
	 * @param addrNum
	 * @return
	 * @throws RemoveException
	 * @throws FindException 
	 */
	@DeleteMapping(value = "{addrNum}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteAddr(@PathVariable("addrNum")Long addrNum) throws RemoveException, FindException{
		service.deleteByAddrNum(addrNum);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
}
