
package com.my.relo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.my.relo.dto.AuctionDTO;
import com.my.relo.dto.PInfoDTO;
import com.my.relo.dto.ZPResponseDTO;
import com.my.relo.entity.Member;
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

	@PersistenceContext
	private EntityManager em;

	/**
	 * 관리자가 상품을 등록한다. 상품이 등록될때 DB에서 트리거로 Stock의 status를 3에서 4로 바꾼다.
	 * 
	 * @param product
	 * @param sNum
	 * @param mNum
	 * @throws AddException
	 */
	public void ProductAdd(Long sNum, Long mNum) throws AddException {

		Optional<Stock> optS1 = sr.findById(sNum);
		Stock s = optS1.get();

		Optional<Member> optM1 = mr.findById(mNum);
		Member m = optM1.get();

		if (m.getType() == 1) {
			optM1 = mr.findById(s.getMember().getMNum());
			m = optM1.get();
			Integer sHopeDays = s.getSHopeDays(); // 일 계산
			LocalDate pEndDate = LocalDate.now().plusDays(sHopeDays);

			Product p = Product.builder().pEndDate(pEndDate).pStatus(4).stock(s).mNum(m.getMNum())
					.pStartDate(LocalDate.now()).build();

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
	public Map<String, Object> selectByIdProduct(Long mNum, int currentPage) throws FindException {

		Optional<Member> optM1 = mr.findById(mNum);
		Member m1 = optM1.get();

		Pageable pageable = PageRequest.of(currentPage - 1, 10);
		Page<Object[]> PagePList = pr.selectByIdProduct(m1.getMNum(), pageable);
		List<Object[]> pList = PagePList.getContent();
		if (!pList.isEmpty()) {
			List<PInfoDTO> list = new ArrayList<>();
			for (Object[] obj : pList) {
				PInfoDTO dto = PInfoDTO.builder().sName(String.valueOf(obj[0])).sizeCategoryName(String.valueOf(obj[1]))
						.pStatus(Integer.valueOf(String.valueOf(obj[2]))).pNum(Long.valueOf(String.valueOf(obj[3])))
						.sBrand(String.valueOf(obj[4])).sNum(Long.valueOf(String.valueOf(obj[5]))).build();

				list.add(dto);
			}

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("list", list);
			resultMap.put("totalPageNum", PagePList.getTotalPages());

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
							.sGrade(String.valueOf(obj[5])).sBrand(String.valueOf(obj[6]))
							.sNum(Long.valueOf(String.valueOf(obj[7]))).build();

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
							.sBrand(String.valueOf(obj[7])).sNum(Long.valueOf(String.valueOf(obj[8]))).build();

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
	 * 
	 * @param mNum
	 * @return
	 * @throws FindException
	 */
	public Map<String, Object> ProductEndListById(Long mNum, int currentPage) throws FindException {

		Pageable pageable = PageRequest.of(currentPage - 1, 10);
		Page<Object[]> pagePList = pr.selectByEndProduct(mNum, pageable);
		List<Object[]> pList = pagePList.getContent();

		if (pList.isEmpty()) {
			throw new FindException("등록된 상품이 없습니다.");
		}

		List<PInfoDTO> list = new ArrayList<>();
		for (Object[] obj : pList) {
			PInfoDTO dto = PInfoDTO.builder().sName(String.valueOf(obj[0])).sizeCategoryName(String.valueOf(obj[1]))
					.pStatus(Integer.valueOf(String.valueOf(obj[2]))).pNum(Long.valueOf(String.valueOf(obj[3])))
					.sBrand(String.valueOf(obj[4])).sNum(Long.valueOf(String.valueOf(obj[5]))).pEndDate((Date) obj[6])
					.build();

			list.add(dto);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("totalPageNum", pagePList.getTotalPages());

		return resultMap;
	}

	/**
	 * 판매자 판매내역 종료 상세 페이지
	 * 
	 * @param mNum
	 * @return
	 * @throws FindException
	 */
	public List<PInfoDTO> ProductEndDetailById(Long mNum) throws FindException {

		List<Object[]> pList = pr.selectByEndProductDetail(mNum);

		if (pList.isEmpty()) {
			throw new FindException("등록된 상품이 없습니다.");
		}

		List<PInfoDTO> list = new ArrayList<>();
		for (Object[] obj : pList) {
			PInfoDTO dto = PInfoDTO.builder().sName(String.valueOf(obj[0])).sizeCategoryName(String.valueOf(obj[1]))
					.pStatus(Integer.valueOf(String.valueOf(obj[2]))).pEndDate((Date) obj[3])
					.pNum(Long.valueOf(String.valueOf(obj[4]))).sBrand(String.valueOf(obj[5]))
					.maxPrice(Long.valueOf(String.valueOf(obj[6]))).sNum(Long.valueOf(String.valueOf(obj[7]))).build();

			list.add(dto);
		}
		return list;
	}

	public void updateProductStatus8(Long pNum) throws AddException {
		Optional<Product> optP = pr.findById(pNum);
		Product p = optP.get();
		p.updatePStatus8(8);
		pr.save(p);
	}

	/**
	 * SHOP 상품리스트를본다
	 * 
	 * @param start      상품목록 가져올 시작 위치
	 * @param condition1 totality : 전체, top :상의, bottom : 하의 sheos :신발
	 *                   default=totality
	 * @param condition2 notender : 입찰안된상품, "" : 전체
	 * @param condition3 pnum : 최신순, penddate: 마감순, zzim : 찜하기순
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ZPResponseDTO> shopProductList(int start, String prodCate, String tender, String sort)
			throws FindException {
		String sql = "SELECT p.p_num, p.s_num, s_name, s_hope_price, nvl(a.max_price,0) as max_price, p_end_date, s_type, z.zcount\r\n"
				+ "FROM product p\r\n" + "JOIN stock s ON p.s_num = s.s_num AND s_type IN(" + prodCate + ")\r\n"
				+ "LEFT OUTER JOIN (SELECT p_num, MAX(a_price) AS max_price FROM auction GROUP BY p_num) a ON p.p_num = a.p_num\r\n"
				+ "LEFT OUTER JOIN (SELECT p_num, COUNT(*) zcount FROM zzim GROUP BY p_num) z ON p.p_num = z.p_num\r\n"
				+ "WHERE p_status = 4 " + tender + "ORDER BY " + sort;

		List<Object[]> resultList = em.createNativeQuery(sql).setFirstResult(start).setMaxResults(15).getResultList();
		List<ZPResponseDTO> dtos = new ArrayList<>();
		for (Object[] objs : resultList) {
			ZPResponseDTO dto = ZPResponseDTO.builder().pNum(Long.valueOf(String.valueOf(objs[0])))
					.sNum(Long.valueOf(String.valueOf(objs[1]))).sName((String) objs[2])
					.sHopePrice(Integer.parseInt(String.valueOf(objs[3])))
					.maxPrice(Integer.parseInt(String.valueOf(objs[4]))).pendDate(LocalDateTime
							.parse(String.valueOf(objs[5]), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")))
					.sType((String) objs[6]).build();
			dtos.add(dto);
		}
		return dtos;
	}

	/**
	 * SHOP > 상품 상세 페이지를 본다(상품 정보)
	 * 
	 * @param pNum : 상품번호
	 * @return 상품 상세 정보
	 * @throws FindException
	 */
	public ZPResponseDTO ShopProductDetail(Long pNum) throws FindException {
		List<Object[]> resultList = pr.selectShopProductDetail(pNum);
		ZPResponseDTO dto = new ZPResponseDTO();
		for (Object[] objs : resultList) {
			dto = ZPResponseDTO.builder().pNum(Long.valueOf(String.valueOf(objs[0])))
					.sNum(Long.valueOf(String.valueOf(objs[1]))).mnum(Long.valueOf(String.valueOf(objs[2])))
					.sBrand((String) objs[3]).sName((String) objs[4]).sType((String) objs[5]).sColor((String) objs[6])
					.sGrade((String) objs[7]).sOriginPrice(Integer.parseInt(String.valueOf(objs[8])))
					.sHopePrice(Integer.parseInt(String.valueOf(objs[9])))
					.maxPrice(Integer.parseInt(String.valueOf(objs[10]))).sizeCategoryName((String) objs[11])
					.managerComment((String) objs[12]).pendDate(LocalDateTime.parse(String.valueOf(objs[13]),
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")))
					.build();

		}
		return dto;
	}

	/**
	 * SHOP > 상품 상세 페이지를 본다(최근 입찰내역)
	 * 
	 * @param pNum : 상품번호
	 * @return 최근 입찰 내역
	 * @throws FindException
	 */

	public List<AuctionDTO> ShopRecentTender(Long pNum) throws FindException {
		List<Object[]> resultlist = pr.recentTenderList(pNum);
		List<AuctionDTO> dtos = new ArrayList<>();
		for (Object[] objs : resultlist) {
			AuctionDTO dto = AuctionDTO.builder().pNum(Long.valueOf(String.valueOf(objs[1])))
					.mNum(Long.valueOf(String.valueOf(objs[2]))).id((String) objs[3])
					.aPrice(Integer.parseInt(String.valueOf(objs[4]))).tenDate(LocalDateTime
							.parse(String.valueOf(objs[5]), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")))
					.build();

			dtos.add(dto);
		}

		return dtos;

	}

	/**
	 * 검색모달창에서 상품검색시 일치하는 상품목록을 표시한다.
	 * 
	 * @param keyword(검색어)
	 * @return 검색결과(상품목록)
	 * @throws FindException
	 */
	public Map<String, Object> searchProductList(String keyword, int currentpage) {
		Pageable pb = PageRequest.of((currentpage - 1), 10);
		Page<Object[]> resultList = pr.selectProductListByName(keyword, pb);
		List<Object[]> list = resultList.getContent();
		int totalpage = resultList.getTotalPages();
		List<ZPResponseDTO> dtos = new ArrayList<>();
		for (Object[] objs : resultList) {
			ZPResponseDTO dto = ZPResponseDTO.builder().pNum(Long.valueOf(String.valueOf(objs[0])))
					.sNum(Long.valueOf(String.valueOf(objs[1]))).sName((String) objs[2])
					.sHopePrice(Integer.parseInt(String.valueOf(objs[3])))
					.maxPrice(Integer.parseInt(String.valueOf(objs[4]))).pendDate(LocalDateTime
							.parse(String.valueOf(objs[5]), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")))
					.sType((String) objs[6]).build();
			dtos.add(dto);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("totalcnt", dtos.size());
		resultMap.put("list", dtos);
		resultMap.put("totalpage", totalpage);
		return resultMap;
	}

}
