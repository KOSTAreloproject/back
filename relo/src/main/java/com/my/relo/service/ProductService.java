package com.my.relo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.my.relo.dto.PInfoDTO;
import com.my.relo.entity.Member;
import com.my.relo.entity.PInfo;
import com.my.relo.entity.Product;
import com.my.relo.entity.Stock;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.repository.MemberRepository;
import com.my.relo.repository.ProductRepository;
import com.my.relo.repository.StockRepository;

@Service
public class ProductService {
	@Autowired
	private MemberRepository mr;

	@Autowired
	private StockRepository sr;

	@Autowired
	private ProductRepository pr;

	/**
	 * 관리자가 상품을 등록한다. 상품이 등록될때 DB에서 트리거로 Stock의 status를 3에서 4로 바꾼다.
	 * 
	 * @param product
	 * @param sNum
	 * @param mNum
	 * @throws AddException
	 */
	public void ProductAdd(PInfo product, Long sNum, Long mNum) throws AddException {

		Optional<Stock> optS1 = sr.findById(sNum);
		Stock s = optS1.get();

		Optional<Member> optM1 = mr.findById(mNum);
		Member m = optM1.get();

		if (m.getType() == 1) {
			optM1 = mr.findById(s.getMember().getMNum());
			m = optM1.get();
			Integer sHopeDays = s.getSHopeDays(); // 일 계산
			LocalDate pEndDate = LocalDate.now().plusDays(sHopeDays);

			Product p = Product.builder().pEndDate(pEndDate).pStatus(4).stock(s).mNum(m.getMNum()).build();

			pr.save(p);

		} else {
			throw new AddException("상품등록 권한이 없습니다.");
		}
	}

	/**
	 * 판매자 판매내역 진행중 페이지
	 * 
	 * @param mNum
	 * @return
	 * @throws AddException
	 */
	public Map<String,Object> selectByIdProduct(Long mNum,int currentPage) throws FindException {

		Optional<Member> optM1 = mr.findById(mNum);
		Member m1 = optM1.get();
		
		Pageable pageable = PageRequest.of(currentPage-1,10); 
		Page<Object[]> PagePList = pr.selectByIdProduct(m1.getMNum(),pageable);
		List<Object[]> pList = PagePList.getContent();
		if (!pList.isEmpty()) {
			List<PInfoDTO> list = new ArrayList<>();
			for (Object[] obj : pList) {
				PInfoDTO dto = PInfoDTO.builder().sName(String.valueOf(obj[0])).sizeCategoryName(String.valueOf(obj[1]))
						.pStatus(Integer.valueOf(String.valueOf(obj[2]))).pNum(Long.valueOf(String.valueOf(obj[3])))
						.sBrand(String.valueOf(obj[4])).build();

				list.add(dto);
			}
			Map<String,Object> resultMap = new HashMap<>();
			resultMap.put("list", list);
			resultMap.put("totalPageNum",PagePList.getTotalPages());
			
			return resultMap;
		} else {
			throw new FindException("등록된 상품이 없습니다.");
		}
	}

	/**
	 * 판매자 판매내역 진행중 상세페이지 (경매참여내역 x) -> selectByIdProductDetail 판매자 판매내역 진행중 상세페이지
	 * (경매참여내역 o) -> selectByIdProductDetail2
	 * 
	 * @param mNum
	 * @param pNum
	 * @return list
	 * @throws FindException
	 */
	public List<PInfoDTO> selectByIdProductDetail(Long mNum, Long pNum) throws FindException {

		Optional<Member> optM1 = mr.findById(mNum);
		Member m = optM1.get();

		Optional<Product> optP1 = pr.findById(pNum);
		if (!optP1.isEmpty()) {
			Product p = optP1.get();
			List<Object[]> optP2 = pr.selectByIdProductDetail2(mNum, pNum);
			if (optP2.isEmpty()) {
				List<Object[]> pinfo = pr.selectByIdProductDetail(m.getMNum(), p.getPNum());
				List<PInfoDTO> list = new ArrayList<>();
				for (Object[] obj : pinfo) {
					PInfoDTO dto = PInfoDTO.builder().sName(String.valueOf(obj[0]))
							.sizeCategoryName(String.valueOf(obj[1])).pStatus(Integer.valueOf(String.valueOf(obj[2])))
							.pEndDate((Date) obj[3]).sHopePrice(Integer.valueOf(String.valueOf(obj[4])))
							.sGrade(String.valueOf(obj[5])).sBrand(String.valueOf(obj[6])).build();

					list.add(dto);
				}
				return list;
			} else {
				List<Object[]> pinfo = pr.selectByIdProductDetail2(mNum, pNum);
				List<PInfoDTO> list = new ArrayList<>();
				for (Object[] obj : pinfo) {
					PInfoDTO dto = PInfoDTO.builder().sName(String.valueOf(obj[0]))
							.sizeCategoryName(String.valueOf(obj[1])).pStatus(Integer.valueOf(String.valueOf(obj[2])))
							.pEndDate((Date) obj[3]).maxPrice(Long.valueOf(String.valueOf(obj[4])))
							.sHopePrice(Integer.valueOf(String.valueOf(obj[5]))).sGrade(String.valueOf(obj[6]))
							.sBrand(String.valueOf(obj[7])).build();

					list.add(dto);
				}
				return list;
			}
		} else {
			throw new FindException("등록된 상품이 없습니다.");
		}
	}
	
	/**
	 * 판매자 판매내역 종료 페이지
	 * @param mNum
	 * @return
	 * @throws FindException
	 */
	public Map<String,Object> ProductEndListById(Long mNum,int currentPage) throws FindException {
		
		Pageable pageable = PageRequest.of(currentPage-1,10);
		Page<Object[]> pagePList = pr.selectByEndProduct(mNum,pageable);
		List<Object[]> pList = pagePList.getContent();
		
		if(pList.isEmpty()) {
			throw new FindException("등록된 상품이 없습니다.");
		}

		List<PInfoDTO> list = new ArrayList<>();
		for (Object[] obj : pList) {
			PInfoDTO dto = PInfoDTO.builder()
			.sName(String.valueOf(obj[0]))
			.sizeCategoryName(String.valueOf(obj[1]))
			.pStatus(Integer.valueOf(String.valueOf(obj[2])))
			.pNum(Long.valueOf(String.valueOf(obj[3])))
			.sBrand(String.valueOf(obj[4]))
			.build();
			
			list.add(dto);
		}
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("totalPageNum",pagePList.getTotalPages());
		
		return resultMap;
	}
	
	/**
	 * 판매자 판매내역 종료 상세 페이지 
	 * @param mNum
	 * @return
	 * @throws FindException
	 */
	public List<PInfoDTO> ProductEndDetailById(Long mNum) throws FindException {

		List<Object[]> pList = pr.selectByEndProductDetail(mNum);
		
		if(pList.isEmpty()) {
			throw new FindException("등록된 상품이 없습니다.");
		}
		
		List<PInfoDTO> list = new ArrayList<>();
		for (Object[] obj : pList) {
			PInfoDTO dto = PInfoDTO.builder()
			.sName(String.valueOf(obj[0]))
			.sizeCategoryName(String.valueOf(obj[1]))
			.pStatus(Integer.valueOf(String.valueOf(obj[2])))
			.pEndDate((Date)obj[3])
			.pNum(Long.valueOf(String.valueOf(obj[4])))
			.sBrand(String.valueOf(obj[5]))
			.maxPrice(Long.valueOf(String.valueOf(obj[6])))
			.build();
			
			list.add(dto);
		}
		return list;
	}

}
