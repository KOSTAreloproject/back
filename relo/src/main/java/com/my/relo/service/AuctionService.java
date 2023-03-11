package com.my.relo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.my.relo.dto.AddressDTO;
import com.my.relo.dto.AuctionDTO;
import com.my.relo.dto.OrderInfoDTO;
import com.my.relo.entity.Auction;
import com.my.relo.entity.Product;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.repository.AuctionRepository;
import com.my.relo.repository.ProductRepository;

@Service
public class AuctionService {
	@Autowired
	private AuctionRepository ar;
	@Autowired
	private ProductRepository pr;

	@Autowired
	ModelMapper modelMapper;

	// 경매 번호에 따른 입찰가
	public Integer priceByaNum(Long aNum) throws FindException {
		Optional<Auction> otpA = ar.findByaNum(aNum);
		Auction a = null;
		if (otpA.isPresent()) {
			a = otpA.get();
		}
		Integer price = a.getAPrice();
		return price;
	}
	
	// 상품의 최고 입찰가 조회
	public Integer maxPriceByPNum(Long pNum) throws FindException {
		Integer max = ar.findMaxPriceByPNum(pNum);
		return max;
	}

	// 회원이 경매 참여할 경우
	public void addAuction(Long pNum, Long mNum, int aPrice) throws AddException {
		try {
			// 상품에 경매 참여 이력 확인
			Optional<Product> otpP = pr.findById(pNum);
			Product p = otpP.get();

			Optional<Auction> otpA = ar.findBymNumAndProduct(mNum, p);
			Auction a = null;
			if (otpA.isPresent()) { // 존재하면 auction값 aPrice 수정
				LocalDate d = LocalDate.now();
				a = Auction.builder().aNum(otpA.get().getANum()).aPrice(aPrice).mNum(mNum).product(p).aDate(d).build();
			} else { // 해당 상품에 경매 참여이력 없을 경우 새로 값 insert
				a = Auction.builder().aPrice(aPrice).mNum(mNum).product(p).build();
			}
			ar.save(a);
		} catch (Exception e) {
			throw new AddException(e.getMessage());
		}
	}

	// 회원의 경매참여 내역 중 경매 진행 중인 항목 개수세기
	public List<AuctionDTO> getIngListBymNum(Long mNum) throws FindException {
		try {
			List<Auction> listA = ar.findAuctionIngByMNum(mNum);
			List<AuctionDTO> list = new ArrayList<>();
			for (Auction a : listA) {
				AuctionDTO dto = AuctionDTO.builder().aNum(a.getANum()).aDate(a.getADate()).aPrice(a.getAPrice())
						.pNum(a.getProduct().getPNum()).sNum(a.getProduct().getStock().getSNum())
						.sGrade(a.getProduct().getStock().getSGrade()).sBrand(a.getProduct().getStock().getSBrand())
						.sColor(a.getProduct().getStock().getSColor()).sName(a.getProduct().getStock().getSName())
						.sizeCategoryName(a.getProduct().getStock().getSizes().getSizeCategoryName())
						.pEndDate(a.getProduct().getPEndDate()).build();
				list.add(dto);
			}
			return list;
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
	}

	// 회원의 경매참여 내역 중 마감일 지난 항목 개수세기로 변경하기
	public List<AuctionDTO> getEndListBymNum(Long mNum) throws FindException {
		try {
			List<Auction> listA = ar.findAuctionEndByMNum(mNum);
			List<AuctionDTO> list = new ArrayList<>();
			for (Auction a : listA) {
				AuctionDTO dto = AuctionDTO.builder().aNum(a.getANum()).mNum(a.getMNum()).pNum(a.getProduct().getPNum())
						.pEndDate(a.getProduct().getPEndDate())
						.aTime(a.getAward() != null ? a.getAward().getATime() : null)
						.sNum(a.getProduct().getStock().getSNum()).sGrade(a.getProduct().getStock().getSGrade())
						.sColor(a.getProduct().getStock().getSColor()).sBrand(a.getProduct().getStock().getSBrand())
						.sName(a.getProduct().getStock().getSName())
						.sizeCategoryName(a.getProduct().getStock().getSizes().getSizeCategoryName())
						.aPrice(a.getAPrice()).pStatus(a.getProduct().getPStatus()).aDate(a.getADate())
						.awNum(a.getAward() != null ? a.getAward().getANum() : null).build();

				list.add(dto);
			}
			return list;
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
	}
	
	// 회원의 경매참여 내역 중 경매 진행 중인 항목 페이징
	public Map<String, Object> getIngBymNum(Long mNum, int currentPage) throws FindException {
		Pageable sortedByaDateDesc = PageRequest.of(currentPage - 1, 5, Sort.by("p_end_date").descending());

		Page<Auction> p = ar.findIngByMNum(mNum, sortedByaDateDesc);
		List<Auction> listA = p.getContent();
		int totalPage = p.getTotalPages();
		Long length = p.getTotalElements();

		List<AuctionDTO> list = new ArrayList<>();
		for (Auction a : listA) {
			AuctionDTO dto = AuctionDTO.builder().aNum(a.getANum()).aDate(a.getADate()).aPrice(a.getAPrice())
					.pNum(a.getProduct().getPNum()).sNum(a.getProduct().getStock().getSNum())
					.sGrade(a.getProduct().getStock().getSGrade()).sBrand(a.getProduct().getStock().getSBrand())
					.sColor(a.getProduct().getStock().getSColor()).sName(a.getProduct().getStock().getSName())
					.sizeCategoryName(a.getProduct().getStock().getSizes().getSizeCategoryName())
					.pEndDate(a.getProduct().getPEndDate()).build();
			list.add(dto);
		}
		Map<String, Object> res = new HashMap<>();
		res.put("totalPage", totalPage);
		res.put("list", list);
		res.put("totalLength", length);
		return res;
	}

	// 회원의 경매참여 내역 중 마감일 지난 항목 페이징
	public Map<String, Object> getEndBymNum(Long mNum, int currentPage) throws FindException {
		Pageable sortedByaDateDesc = PageRequest.of(currentPage - 1, 5, Sort.by("p_end_date").descending());

		Page<Auction> p = ar.findEndByMNum(mNum, sortedByaDateDesc);
		List<Auction> listA = p.getContent();
		int totalPage = p.getTotalPages();
		Long length = p.getTotalElements();
		List<AuctionDTO> list = new ArrayList<>();
		for (Auction a : listA) {
			AuctionDTO dto = AuctionDTO.builder().aNum(a.getANum()).mNum(a.getMNum()).pNum(a.getProduct().getPNum())
					.pEndDate(a.getProduct().getPEndDate())
					.aTime(a.getAward() != null ? a.getAward().getATime() : null)
					.sNum(a.getProduct().getStock().getSNum()).sGrade(a.getProduct().getStock().getSGrade())
					.sColor(a.getProduct().getStock().getSColor()).sBrand(a.getProduct().getStock().getSBrand())
					.sName(a.getProduct().getStock().getSName())
					.sizeCategoryName(a.getProduct().getStock().getSizes().getSizeCategoryName())
					.aPrice(a.getAPrice()).pStatus(a.getProduct().getPStatus()).aDate(a.getADate())
					.awNum(a.getAward() != null ? a.getAward().getANum() : null).build();

			list.add(dto);
		}
		Map<String, Object> res = new HashMap<>();
		res.put("totalPage", totalPage);
		res.put("list", list);
		res.put("totalLength", length);
		return res;

	}
	
	//결제 버튼 클릭시 띄워줄 주문 정보 GET
	public OrderInfoDTO getOrderInfo(Long mNum, Long pNum, Long aNum) throws FindException {
		try {
			List<Object[]> list = new ArrayList<>();
			list = ar.findOrderDetailByPNum(mNum, pNum, aNum);
			OrderInfoDTO oiDto = null;
			List<AddressDTO> addrList = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				AddressDTO adDto = null;
				if (list.get(i)[10] != null) {
					adDto = AddressDTO.builder()
							.addrNum(Long.parseLong(list.get(i)[10].toString()))
							.addrRecipient(list.get(i)[11].toString())
							.addrPostNum(list.get(i)[12].toString()).addrTel(list.get(i)[13].toString())
							.addr(list.get(i)[14].toString()).addrDetail(list.get(i)[15].toString())
							.addrType(Integer.parseInt(list.get(i)[16].toString())).build();
					addrList.add(adDto);
				}

				if (i == list.size() - 1) {
					oiDto = OrderInfoDTO.builder().sNum(Long.parseLong(list.get(i)[0].toString()))
							.sName(list.get(i)[1].toString()).sBrand(list.get(i)[2].toString())
							.sizeCategoryName(list.get(i)[3].toString()).sColor(list.get(i)[4].toString())
							.sGrade(list.get(i)[5].toString()).name(list.get(i)[6].toString())
							.email(list.get(i)[7].toString()).tel(list.get(i)[8].toString())
							.aPrice(Integer.parseInt(list.get(i)[9].toString())).addrList(addrList).build();
				}
			}
			return oiDto;
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
	}
}
