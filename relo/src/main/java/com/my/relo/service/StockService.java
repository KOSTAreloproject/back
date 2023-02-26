package com.my.relo.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.my.relo.dto.StockDTO;

import com.my.relo.entity.Member;
import com.my.relo.entity.Sizes;
import com.my.relo.entity.Stock;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.repository.MemberRepository;
import com.my.relo.repository.SizesRepository;
import com.my.relo.repository.StockRepository;

@Service
public class StockService {
	@Autowired
	private MemberRepository mr;
	
	@Autowired
	private StockRepository sr;
	
	@Autowired
	private SizesRepository sir;
	
	/**
	 * 판매자가 처음 상품을 등록한다.
	 * 
	 * @param stock
	 * @throws AddException
	 */
	public void StockAdd(StockDTO stock) throws AddException{
		
		Optional<Member> optM1 = mr.findById(stock.getMNum());
		Member m = optM1.get();
		
		Optional<Sizes> optS1 = sir.findById(stock.getSizeCategoryNum());
		Sizes size = optS1.get();
		
		Stock s = Stock.builder()
				.member(m)
				.sizes(size)
				.sBrand(stock.getSBrand())
				.sName(stock.getSName())
				.sOriginPrice(stock.getSOriginPrice())
				.sColor(stock.getSColor())
				.sType(stock.getSType())
				.sHopeDays(stock.getSHopeDays())
				.sellerComment(stock.getSellerComment())
				.build();
		sr.save(s);
	}
	
	
	/**
	 * 관리자가 판매자가 등록한 상품에 등급과 comment를 등록한다.
	 * 판매자가 hopePrice를 등록한다.
	 * 관리자가 판매자가 등록한 상품에 등급과 comment를 등록할때 등급이 "불"을 받는다.
	 * @param stock
	 * @throws AddException
	 */
	public void updateSetSStatus(StockDTO stock) throws AddException{
		
//		Member m = stock.getMNum();

		Optional<Member> optM1 = mr.findById(stock.getMNum());

		Optional<Stock> optS1 = sr.findById(stock.getSNum());
			
			if(optM1.get().getType() == 1) { //관리자
				
				if(optS1.isPresent()) {
			
					Stock s = optS1.get();
					
					if(!stock.getSGrade().isBlank() && !stock.getManagerComment().isBlank() && !stock.getSGrade().equals("불")) {
						
						s .updateStockByAdmin(stock.getManagerComment(), stock.getSGrade(), 2);
						
						sr.save(s);
					
					}else if(stock.getSGrade().equals("불")) {
						
						s.updateStockByAdmin(stock.getManagerComment(), stock.getSGrade(), 5);
						
						sr.save(s);
					}
				}else {
					throw new AddException("상품이 없습니다");
				}
			}else if(optM1.get().getType() == 2) {  //판매자
				
				if(optS1.isPresent()) {
					
					Stock s = optS1.get();
					
					if(stock.getSHopePrice() != null) {
										
						s.updateStockByMember(stock.getSHopePrice(), 3);
						
						sr.save(s);
					}
				}else {
					throw new AddException("상품이 없습니다");
				}
			}
	}
	
	/**
	 * 판매자가 등급을 받고 상품등록을 취소한다.
	 * 
	 * @param stock
	 * @throws AddException
	 */
	public void updateByCancleSStatus5(Long sNum) throws AddException{
		
		Optional<Stock> optS1 = sr.findById(sNum);
		
		Stock s = optS1.get();
		s.updateByCancleSStatus5(5);
		sr.save(s);
	}
	
	/**
	 * 판매자 마이페이지-> 판매내역 -> 판매대기
	 * @param mNum
	 * @throws AddException
	 */
	public List<StockDTO> selectById(Long mNum) throws FindException{
		Optional<Member> optM1 = mr.findById(mNum);
		Member m1 = optM1.get();
		
		List<Object[]> sList = sr.selectById(m1.getMNum());
		
		List<StockDTO> list = new ArrayList<>();
		for (Object[] obj : sList) {
			StockDTO dto = StockDTO.builder()
			.sNum(Long.valueOf(String.valueOf(obj[0])))
			.sName(String.valueOf(obj[1]))
			.sizeCategoryName(String.valueOf(obj[2]))
			.sStatus(Integer.valueOf(String.valueOf(obj[3])))
			.sGrade(String.valueOf(obj[4]))
			.sBrand(String.valueOf(obj[5]))
			.build();
				
			list.add(dto);
		}
		return list;
	}
	
	/**
	 * 판매자 마이페이지-> 판매내역 -> 판매대기 상세
	 * @param sNum
	 * @param mNum
	 * @return list
	 * @throws AddException
	 */
	public List<StockDTO> detailById(Long sNum,Long mNum) throws FindException{
		Optional<Member> optM1 = mr.findById(mNum);
		Member m1 = optM1.get();
		
		List<Object[]> sList = sr.selectByIdDeatil(sNum,m1.getMNum());
		
		List<StockDTO> list = new ArrayList<>();
		for (Object[] obj : sList) {
			StockDTO dto = StockDTO.builder()
					.sNum(Long.valueOf(String.valueOf(obj[0])))
					.mNum(Long.valueOf(String.valueOf(obj[1])))
					.sName(String.valueOf(obj[2]))
					.sType(String.valueOf(obj[3]))
					.sizeCategoryName(String.valueOf(obj[4]))
					.sColor(String.valueOf(obj[5]))
					.managerComment(String.valueOf(obj[6]))
					.sHopeDays(Integer.valueOf(String.valueOf(obj[7])))
					.sOriginPrice(Integer.valueOf(String.valueOf(obj[8])))
					.sBrand(String.valueOf(obj[9]))
					.sStatus(Integer.valueOf(String.valueOf(obj[10])))
					.sGrade(String.valueOf(obj[11]))
					.build();
			
			list.add(dto);
		}
		return list;
	}
	
	/**
	 * 관리자 상품등록 승인요청 목록 sStatus =1 AND 관리자 상품 최종 등록 목록  sStatus =3
	 * @return list
	 * @throws AddException
	 */
	public List<StockDTO> selectBySstatus(Integer sStatus) throws FindException{
		
			Pageable pageable = PageRequest.of(0,5,Sort.by("s_num"));  //5개씩 페이징
			List<Object[]> sList = sr.selectBySReturn(sStatus,pageable);
			List<StockDTO> list = new ArrayList<>();
			for (Object[] obj : sList) {
				StockDTO dto = StockDTO.builder()
				.sNum(Long.valueOf(String.valueOf(obj[0])))
				.sName(String.valueOf(obj[1]))
				.sizeCategoryName(String.valueOf(obj[2]))
				.sColor(String.valueOf(obj[3]))
				.mNum(Long.valueOf(String.valueOf(obj[4])))
				.build();
					
				list.add(dto);
			}

		return list;
	}
	
	/**
	 * 관리자 상품등록 승인요청 목록 상세 AND 관리자 상품 최종 등록 목록 상세
	 * @param sNum
	 * @return s
	 * @throws AddException
	 */
	public Stock detailBySNum(Long sNum) throws FindException{
		
		Optional<Stock> optS1 = sr.findById(sNum);
		
		Stock s = optS1.get();

		return s;
	}
	
}
