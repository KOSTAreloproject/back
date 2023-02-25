package com.my.relo.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.my.relo.repository.StyleTagRepository;
import com.my.relo.service.StyleService;

@RestController
@RequestMapping("style/*")
public class StyleController {
	@Autowired
	private StyleService service;
	@Autowired
	private StyleTagRepository str;
	/**
	 * 리스트 출력 
	 * @param type 1: 최신순 2: 좋아요순 3: 조회수순 
	 * @return
	 */
	@GetMapping(value = "list/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getList(@PathVariable("type") Long type) {
		List<StyleDTO> list = new ArrayList<>();
		List<String> tagList = str.ListByCnt();
		Map map = new HashMap<>();
		try {
			if(type == 1) {
				list = service.listByStyleNum();
			}else if(type == 2) {
				list = service.listByLikes();
			}else if(type == 3) {
				list = service.listBystyleCnt();
			}
			if(!list.isEmpty()) {
				map.put("list", list);
				map.put("tagList", tagList);
				return new ResponseEntity<>(map, HttpStatus.OK);
			}
		} catch (FindException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
	}

	/**
	 * 상세보기 출력 (상세보기 조회시 조회수 +1 증가)
	 * @param styleNum
	 * @return
	 * @throws FindException
	 * @throws AddException 
	 */
	@GetMapping(value = "detail/{styleNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDetail(@PathVariable("styleNum")Long styleNum) throws FindException, AddException {
		service.plusCnt(styleNum);
		StyleDTO style= service.styleDetail(styleNum);
		return new ResponseEntity<>(style, HttpStatus.OK);
	}
	
	/**
	 * 내가 쓴 게시판 리스트 출력 
	 * @param mNum
	 * @return
	 * @throws FindException
	 */
	@GetMapping(value = "myList/{mNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMyList(@PathVariable("mNum")Long mNum) throws FindException{
		Map map = new HashMap<>();
		List<StyleDTO> list = service.listByMNum(mNum);
		List<String> tagList = str.ListByCnt();
		map.put("list", list);
		map.put("tagList", tagList);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	/**
	 * 해시태그 별 리스트 출력 
	 * @param hashName : 해시태그 
	 * @return
	 * @throws FindException
	 */
	@GetMapping(value="hashList/{hashName}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHashName(@PathVariable("hashName")String hashName) throws FindException{
		Map map = new HashMap<>();
		List<StyleDTO> list = service.listByHashName(hashName);
		List<String> tagList = str.ListByCnt();
		map.put("list", list);
		map.put("tagList", tagList);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	/**
	 * 게시판 작성 
	 * @param session
	 * @param styleContent : 해시태그 리스트 
	 * @param f : 파일 
	 * @return
	 * @throws AddException
	 * @throws IOException 
	 */
	@PostMapping(value = "write")
	public ResponseEntity<?> write(HttpSession session,
									String styleContent, 
									@RequestPart MultipartFile f) throws AddException, IOException {
		
//		Long logined = (Long)session.getAttribute("logined");
//		if(logined == null) {//로그인 안한 경우
//			throw new AddException("로그인하세요");
//		}
		MemberDTO m = new MemberDTO();
		Long logined = 2L;
		m.setMNum(logined);
		
		StyleDTO s = new StyleDTO();
		s.setMember(m);
		
		String saveDirectory = "/Users/skyleeb95/Downloads/files";
		String fileName = "s_"+logined+".jpeg";
		
		File file = new File(saveDirectory, fileName);
		
		f.transferTo(file);
		
		List<StyleTagDTO> tagList = new ArrayList<>();
		StringTokenizer stk = new StringTokenizer(styleContent,"#");
		while(stk.hasMoreTokens()) {
			StyleTagDTO tag = new StyleTagDTO();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName(stk.nextToken().trim());
			tag.setSte(ste);
			tagList.add(tag);
			}
		s.setTagList(tagList);
		service.write(s);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	/**
	 * 게시물 업데이트(스타일 태그만 수정)
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
	public ResponseEntity<?> update(@PathVariable("styleNum")Long styleNum,
															String styleContent,
															@RequestPart(value = "f",required = false)MultipartFile f) throws AddException, IllegalStateException, IOException, FindException{
		Long logined = 2L;
		
		String saveDirectory = "/Users/skyleeb95/Downloads/files";
		String fileName = "s_"+logined+".jpeg";
		File file = new File(saveDirectory, fileName);
		
		if(file.exists()) {
			file.delete();
		}
		File nFile = new File(saveDirectory, fileName);
//		f.transferTo(nFile);
		
		StyleDTO s = new StyleDTO();
		List<StyleTagDTO> tagList = new ArrayList<>();
		StringTokenizer stk = new StringTokenizer(styleContent,"#");
		while(stk.hasMoreTokens()) {
			StyleTagDTO tag = new StyleTagDTO();
			StyleTagEmbedded ste = new StyleTagEmbedded();
			ste.setHashName(stk.nextToken().trim());
			tag.setSte(ste);
			tagList.add(tag);
			}
		s.setTagList(tagList);
		s.setStyleNum(styleNum);
		service.edit(s);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 게시판 삭제(좋아요 + 해시태그 + 댓글)
	 * @param styleNum
	 * @return
	 * @throws RemoveException
	 * @throws FindException 
	 */
	@DeleteMapping(value = "{styleNum}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable("styleNum")Long styleNum) throws RemoveException, FindException{
		service.deleteByStyleNum(styleNum);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
