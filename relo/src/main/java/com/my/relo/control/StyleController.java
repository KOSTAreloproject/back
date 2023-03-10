package com.my.relo.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.my.relo.dto.MemberDTO;
import com.my.relo.dto.StyleDTO;
import com.my.relo.dto.StyleTagDTO;
import com.my.relo.entity.StyleTagEmbedded;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.service.StyleService;

import net.coobird.thumbnailator.Thumbnailator;

@RestController
@RequestMapping("style/*")
public class StyleController {

	@Autowired
	private StyleService service;

	private final String saveDirectory = "C:\\storage\\style";
//	private final String saveDirectory = "/Users/skyleeb95/Downloads/files/style";

	/**
	 * 리스트 출력
	 * 
	 * @param type 1: 최신순 2: 좋아요순 3: 조회수순
	 * @return
	 */
	@GetMapping(value = "list/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getList(@PathVariable("type") Long type, HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (type == 1) {
				map = service.listByStyleNum();
			} else if (type == 2) {
				map = service.listByLikes();
			} else if (type == 3) {
				map = service.listBystyleCnt();
			}
			if (!map.isEmpty()) {
				Long loginId = (Long) session.getAttribute("logined");
				map.put("loginId", loginId);
				return new ResponseEntity<>(map, HttpStatus.OK);
			}
		} catch (FindException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
	}

	/**
	 * 리스트 출력 - 이미지 출력
	 * 
	 * @param styleNum
	 * @return
	 * @throws FindException
	 * @throws IOException
	 */
	@GetMapping(value = "list/img/{styleNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getImgList(@PathVariable("styleNum") Long styleNum) throws FindException, IOException {

		File saveDirFile = new File(saveDirectory);
		File[] files = saveDirFile.listFiles();
		File file = null;
		String fileName;
		Resource img = null;
		for (File thumbF : files) {
			StringTokenizer stk = new StringTokenizer(thumbF.getName(), ".");
			fileName = stk.nextToken();
			if (fileName.equals("t_s_" + styleNum)) {
				img = new FileSystemResource(thumbF);
				file = thumbF;
			}
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set(HttpHeaders.CONTENT_LENGTH, "" + file.length());
		responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file.toPath()));
		responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + URLEncoder.encode("a", "UTF-8"));

		return new ResponseEntity<>(img, responseHeaders, HttpStatus.OK);
	}

	/**
	 * 상세보기 출력 (상세보기 조회시 조회수 +1 증가)
	 * 
	 * @param styleNum
	 * @return
	 * @throws FindException
	 * @throws AddException
	 */
	@GetMapping(value = "detail/{styleNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDetail(@PathVariable("styleNum") Long styleNum, HttpSession session)
			throws FindException, AddException {
		Long mNum = (Long) session.getAttribute("logined");
		String loginId = (String) session.getAttribute("loginId");
//		if(mNum == null) {
//			throw new FindException("로그인하세요");
//		}
		service.plusCnt(styleNum);
		StyleDTO style = service.styleDetail(styleNum);
		Map<String, Object> map = new HashMap<>();
		map.put("style", style);
		map.put("loginNum", mNum);
		map.put("loginId", loginId);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 상세보기 - 해당 이미지 출력
	 * 
	 * @param styleNum
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value = "detail/img/{styleNum}")
	public ResponseEntity<?> getDetailFile(@PathVariable("styleNum") Long styleNum) throws IOException {
		File saveDirFile = new File(saveDirectory);
		File[] files = saveDirFile.listFiles();
		Resource img = null;
		for (File f : files) {
			StringTokenizer stk = new StringTokenizer(f.getName(), ".");
			String fileName = stk.nextToken();
			if (fileName.equals("s_" + styleNum)) {
				img = new FileSystemResource(f);
				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.set(HttpHeaders.CONTENT_LENGTH, f.length() + "");
				responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(f.toPath()));
				responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
						"inline; filename=" + URLEncoder.encode("a", "UTF-8"));

				return new ResponseEntity<>(img, responseHeaders, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 내가 쓴 게시판 리스트 출력
	 * 
	 * @param mNum
	 * @return
	 * @throws FindException
	 */
	@GetMapping(value = "myList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyList(HttpSession session) throws FindException {
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}
		Map<String, Object> map = service.listByMNum(mNum);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 해시태그 별 리스트 출력
	 * 
	 * @param hashName : 해시태그
	 * @return
	 * @throws FindException
	 */
	@GetMapping(value = "hashList/{hashName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHashName(@PathVariable("hashName") String hashName) throws FindException {
		Map<String, Object> map = service.listByHashName(hashName);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	/**
	 * 게시판 작성
	 * 
	 * @param session
	 * @param styleContent : 해시태그 리스트
	 * @param f            : 파일
	 * @return
	 * @throws AddException
	 * @throws IOException
	 * @throws FindException
	 */
	@PostMapping(value = "write")
	public ResponseEntity<?> write(HttpSession session, String styleContent,
			@RequestPart(value = "f", required = false) MultipartFile f)
			throws AddException, IOException, FindException {

		Long mNum = (Long) session.getAttribute("logined");

		if (mNum == null) {// 로그인 안한 경우
			throw new FindException("로그인하세요");
		}

		if (f.getSize() == 0) {
			throw new FindException("파일이 없습니다.");
		}
		MemberDTO mDTO = MemberDTO.builder().mnum(mNum).build();

		StyleDTO s = new StyleDTO();
		s.setMember(mDTO);

		List<StyleTagDTO> tagList = new ArrayList<>();
		StringTokenizer stk = new StringTokenizer(styleContent, "#");
		while (stk.hasMoreTokens()) {
			StyleTagDTO tag = new StyleTagDTO();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName(stk.nextToken().trim());
			tag.setSte(ste);
			tagList.add(tag);
		}
		s.setTagList(tagList);

		Long styleNum = service.write(s);
		String originFileName = f.getOriginalFilename();
		String fileExtension = originFileName.substring(originFileName.lastIndexOf(".") + 1);
		String fileName = "s_" + styleNum + "." + fileExtension;

		File file = new File(saveDirectory, fileName);

		int width = 500;
		int height = 500;
		String thumbFileName = "t_" + fileName;
		File thumbFile = new File(saveDirectory, thumbFileName);
		FileOutputStream thumbOs = new FileOutputStream(thumbFile);
		InputStream thumbIs = f.getInputStream();
		Thumbnailator.createThumbnail(thumbIs, thumbOs, width, height);

		f.transferTo(file);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 게시물 업데이트(스타일 태그만 수정)
	 * 
	 * @param styleNum
	 * @param styleContent
	 * @param f
	 * @return
	 * @throws AddException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws FindException
	 */
	@PostMapping(value = "update/{styleNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@PathVariable("styleNum") Long styleNum, String styleContent,
			@RequestPart(value = "f", required = false) MultipartFile f, HttpSession session)
			throws AddException, IllegalStateException, IOException, FindException {

		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {// 로그인 안한 경우
			throw new AddException("로그인하세요");
		}
		StyleDTO s = new StyleDTO();
		List<StyleTagDTO> tagList = new ArrayList<>();
		StringTokenizer stk = new StringTokenizer(styleContent, "#");
		while (stk.hasMoreTokens()) {
			StyleTagDTO tag = new StyleTagDTO();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName(stk.nextToken().trim());
			tag.setSte(ste);
			tagList.add(tag);
		}
		s.setTagList(tagList);
		s.setStyleNum(styleNum);
		service.edit(s);

		if (f.getSize() != 0) {

			File saveDirFile = new File(saveDirectory);

			File[] files = saveDirFile.listFiles();
			for (File originF : files) {
				StringTokenizer stk2 = new StringTokenizer(originF.getName(), ".");
				String fileName = stk2.nextToken();
				if (fileName.equals("s_" + styleNum)) {
					originF.delete();
				}
			}
			for (File thumbF : files) {
				StringTokenizer stk2 = new StringTokenizer(thumbF.getName(), ".");
				String fileName2 = stk2.nextToken();
				if (fileName2.equals("t_s_" + styleNum)) {
					thumbF.delete();
				}
			}

			String originFileName = f.getOriginalFilename();
			String fileExtension = originFileName.substring(originFileName.lastIndexOf(".") + 1);
			String fileName = "s_" + styleNum + "." + fileExtension;

			File file = new File(saveDirectory, fileName);

			int width = 500;
			int height = 500;
			String thumbFileName = "t_" + fileName;
			File thumbFile = new File(saveDirectory, thumbFileName);
			FileOutputStream thumbOs = new FileOutputStream(thumbFile);
			InputStream thumbIs = f.getInputStream();
			Thumbnailator.createThumbnail(thumbIs, thumbOs, width, height);

			f.transferTo(file);

			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	/**
	 * 게시판 삭제(좋아요 + 해시태그 + 댓글)
	 * 
	 * @param styleNum
	 * @return
	 * @throws RemoveException
	 * @throws FindException
	 */
	@DeleteMapping(value = "{styleNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable("styleNum") Long styleNum, HttpSession session)
			throws RemoveException, FindException {

		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {// 로그인 안한 경우
			throw new FindException("로그인하세요");
		}

		File saveDirFile = new File(saveDirectory);
		File[] files = saveDirFile.listFiles();

		for (File originF : files) {
			StringTokenizer stk2 = new StringTokenizer(originF.getName(), ".");
			String fileName = stk2.nextToken();
			if (fileName.equals("s_" + styleNum)) {
				originF.delete();
			}
		}
		for (File thumbF : files) {
			StringTokenizer stk2 = new StringTokenizer(thumbF.getName(), ".");
			String fileName2 = stk2.nextToken();
			if (fileName2.equals("t_s_" + styleNum)) {
				thumbF.delete();
			}
		}

		service.deleteByStyleNum(styleNum);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}