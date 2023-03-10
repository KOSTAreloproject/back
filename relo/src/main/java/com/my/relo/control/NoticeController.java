package com.my.relo.control;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.NoticeDTO;
import com.my.relo.entity.Member;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.MemberRepository;
import com.my.relo.service.NoticeService;

@RestController
@RequestMapping("notice/*")
public class NoticeController {
	@Autowired
	private NoticeService ns;

	@Autowired
	private MemberRepository mr;

	private final String saveDirectory = "C:\\storage\\notice";

	// 공지사항 작성
	@PostMapping(value = "write")
	public ResponseEntity<?> write(@RequestPart(value = "param") Map<String, Object> param, HttpSession session)
			throws AddException, IllegalStateException, IOException, FindException {

		String title = (String) param.get("title");
		String rawCategory = (String) param.get("category");
		Integer category = Integer.valueOf(rawCategory);
		String content = (String) param.get("content");

		Long mNum = (Long) session.getAttribute("logined");

		Optional<Member> optM = mr.findById(mNum);
		if (optM.isPresent()) {
			Member m = optM.get();

			NoticeDTO dto = NoticeDTO.builder().member(m).title(title).date(LocalDate.now()).category(category).build();

			Long nNum = ns.addNotice(dto);

			String fileName = "n_" + nNum + ".html";

			FileWriter writer = new FileWriter(saveDirectory + "/" + fileName);
			writer.write(content);
			writer.close();
			
			dto = NoticeDTO.builder().nNum(nNum).content(fileName).build();

			ns.updateNotice(dto);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new FindException();
		}
	}

	// 제목으로 검색
	@GetMapping(value = "title/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchByTitle(@PathVariable String title) {
		try {
			List<NoticeDTO> results = ns.searchByTitle(title);
			return new ResponseEntity<>(results, HttpStatus.OK);
		} catch (FindException e) {
			e.printStackTrace();
			Map<String, Object> results = new HashMap<>();
			results.put("msg", e.getStackTrace());
			return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
		}
	}

	// 카테고리로 검색
	@GetMapping(value = "category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchByCategory(@PathVariable Integer category) {
		try {
			List<NoticeDTO> results = ns.searchByCategory(category);
			return new ResponseEntity<>(results, HttpStatus.OK);
		} catch (FindException e) {
			e.printStackTrace();
			Map<String, Object> results = new HashMap<>();
			results.put("msg", e.getStackTrace());
			return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
		}
	}

	// 전체 목록
	@GetMapping(value = "{currentPage}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> list(@PathVariable Integer currentPage) {
		try {
			Map<String, Object> results = ns.pagingNotice(currentPage);
			return new ResponseEntity<>(results, HttpStatus.OK);
		} catch (FindException e) {
			e.printStackTrace();
			Map<String, Object> results = new HashMap<>();
			results.put("msg", e.getStackTrace());
			return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
		}
	}

	// 공지사항 상세
	@GetMapping(value = "detail/{nNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> detail(@PathVariable Long nNum) {
		try {
			NoticeDTO dto = ns.detailNotice(nNum);
			NoticeDTO pre = ns.searchPre(nNum);
			NoticeDTO next = ns.searchNext(nNum);

			Map<String, Object> results = new HashMap<>();

			if (pre == null) {
				results.put("msg", "이전글이 없습니다.");
			} else {
				results.put("pre", pre);
			}

			if (next == null) {
				results.put("msg", "다음글이 없습니다.");
			} else {
				results.put("next", next);
			}

			results.put("dto", dto);

			return new ResponseEntity<>(results, HttpStatus.OK);
		} catch (FindException e) {
			e.printStackTrace();
			Map<String, Object> results = new HashMap<>();
			results.put("msg", e.getStackTrace());
			return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
		}
	}

	// 공지사항 content 파일 전송
	@GetMapping("contentfile/{nNum}")
	public ResponseEntity<?> contentFiles(@PathVariable Long nNum) throws IOException {
		File saveDirFile = new File(saveDirectory);

		File[] files = saveDirFile.listFiles();
		HttpHeaders responseHeaders = new HttpHeaders();
		for (File f : files) {
			// .을 기준으로, 확장자명 앞에 있는 파일명 얻음
			StringTokenizer stk = new StringTokenizer(f.getName(), ".");
			String fileName = stk.nextToken();

			// 해당 파일들 중에서 원하는 파일 이름과 일치하는 파일명이 존재할 경우
			if (fileName.equals("n_" + nNum)) {

				f = new File(saveDirectory + "/" + f.getName());

				byte[] bArr = FileCopyUtils.copyToByteArray(f);

				// 파일 크기 설정
				responseHeaders.set(HttpHeaders.CONTENT_LENGTH, f.length() + "");
				// 파일 타입 설정
				responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(f.toPath()));
				// 파일 인코딩 설정
				responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
						"inline; filename=" + URLEncoder.encode("a", "UTF-8"));

				return new ResponseEntity<>(bArr, responseHeaders, HttpStatus.OK);
			}
		}
		return null;
	}

	// 공지사항 수정
	@PostMapping("{nNum}")
	public ResponseEntity<?> modify(HttpSession session, @PathVariable Long nNum,
			@RequestPart(value = "param") Map<String, Object> param) throws FindException, IOException {
		Long mNum = (Long) session.getAttribute("logined");
		Optional<Member> optM = mr.findById(mNum);
		
		if(!optM.isPresent())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		String title = (String) param.get("title");
		String rawCategory = (String) param.get("category");
		Integer category = Integer.valueOf(rawCategory);
		String content = (String) param.get("content");

		File saveDirFile = new File(saveDirectory);
		File[] files = saveDirFile.listFiles();
		String fileFullName = null;
		for (File f : files) {
			fileFullName = f.getName();
			if (fileFullName.equals("n_" + nNum + ".html")) {
				File exitFile = new File(saveDirectory, fileFullName);
				exitFile.delete();
				FileWriter writer = new FileWriter(saveDirectory + "/" + fileFullName);
				writer.write(content);
				writer.close();
				break;
			}
		}

		NoticeDTO dto = NoticeDTO.builder().nNum(nNum).category(category).title(title).content(fileFullName).build();
		ns.updateNotice(dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 공지사항 삭제
	@DeleteMapping("{nNum}")
	public ResponseEntity<?> remove(@PathVariable Long nNum) throws RemoveException, FindException {
		ns.deleteNotice(nNum);

		File saveFile = new File(saveDirectory, "n_"+nNum+".html");


		if(saveFile.exists()) {
			saveFile.delete();
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
