package com.my.relo.control;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.my.relo.dto.MemberDTO;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.service.MemberService;

@RestController
@RequestMapping("member/*")
public class MemberController {
	@Autowired
	private MemberService ms;

	private final String saveDirectory = "C:\\storage\\member";

	// 회원 가입
	@PostMapping("join")
	public ResponseEntity<?> join(@RequestBody Map<String, Object> param) throws AddException {
		boolean check = (boolean) param.get("check");

		String id = (String) param.get("id");
		String pwd = (String) param.get("pwd");
		String tel = (String) param.get("tel");
		String birth = (String) param.get("birth");
		String email = (String) param.get("email");
		String name = (String) param.get("name");

		MemberDTO dto = MemberDTO.builder().id(id).pwd(pwd).birth(birth).email(email).name(name).tel(tel).type(1)
				.build();

		if (check) {
			ms.join(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// 로그인
	@PostMapping("login")
	public ResponseEntity<?> login(HttpServletRequest request, @RequestParam String id, @RequestParam String pwd) {

		MemberDTO dto = ms.idCheck(id);

		if (dto.getId().equals(id) && dto.getPwd().equals(pwd)) {

			HttpSession session = request.getSession();
			session.setAttribute("logined", dto.getMnum());

			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// 로그인 상태 확인
	@GetMapping("checklogined")
	public ResponseEntity<?> checkLogined(HttpSession session) throws FindException {
		Long mNum = (Long) session.getAttribute("logined");

		if (mNum != null) {
			MemberDTO dto = ms.detailMember(mNum);
			int type = dto.getType();
			return new ResponseEntity<>(type, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// 로그아웃
	@GetMapping("logout")
	public ResponseEntity<?> logout(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 아이디 및 비밀번호 찾기
	@PostMapping("findidandpwd")
	public ResponseEntity<?> findIdAndPwd(String tel) {
		String id = ms.findIdAndPwd(tel);

		if (id != null) {
			return new ResponseEntity<>(id, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// 아이디 중복 체크
	@GetMapping("idcheck")
	public ResponseEntity<?> idCheck(@RequestParam String id) {
		MemberDTO dto = ms.idCheck(id);

		if (dto == null) {
			return new ResponseEntity<>("ok", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("no", HttpStatus.OK);
		}
	}

	// 회원 프로필 이미지 출력(이미지 수정 후 출력)
	@PostMapping(value = "img")
	public ResponseEntity<?> showImage(HttpSession session,
			@RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

		Long mNum = (Long) session.getAttribute("logined");

		File saveDirFile = new File(saveDirectory);

		// 해당 경로의 정보 필요 -> listFiles로 해당 경로의 정보 얻어옴
		File[] files = saveDirFile.listFiles();
		Resource img = null;
		if (profile == null) {
			for (File f : files) {
				// .을 기준으로, 확장자명 앞에 있는 파일명 얻음
				StringTokenizer stk = new StringTokenizer(f.getName(), ".");
				String fileName = stk.nextToken();

				// 해당 파일들 중에서 원하는 파일 이름과 일치하는 파일명이 존재할 경우
				if (fileName.equals("m_" + mNum)) {
					img = new FileSystemResource(f);

					// response 헤더 설정을 따로 하지 않을 경우, 응답 헤더의 content-type이 application/json 타입으로 되므로
					// 설정 필요함
					HttpHeaders responseHeaders = new HttpHeaders();
					// 파일 크기 설정
					responseHeaders.set(HttpHeaders.CONTENT_LENGTH, f.length() + "");
					// 파일 타입 설정
					responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(f.toPath()));
					// 파일 인코딩 설정
					responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
							"inline; filename=" + URLEncoder.encode("a", "UTF-8"));
					return new ResponseEntity<>(img, responseHeaders, HttpStatus.OK);
				}
			}
		} else {

			for (File f : files) {
				StringTokenizer stk = new StringTokenizer(f.getName(), ".");
				String fileName = stk.nextToken();

				if (fileName.equals("m_" + mNum)) {
					File exitFile = new File(saveDirectory, f.getName());
					exitFile.delete();
				}
			}

			String orignFileName = profile.getOriginalFilename();
			String fileName = "m_" + mNum + "." + orignFileName.substring(orignFileName.lastIndexOf(".") + 1);
			File exitStorage = new File(saveDirectory, fileName);

			profile.transferTo(exitStorage);

			img = new FileSystemResource(exitStorage);
			HttpHeaders responseHeaders = new HttpHeaders();
			// 파일 크기 설정
			responseHeaders.set(HttpHeaders.CONTENT_LENGTH, exitStorage.length() + "");
			// 파일 타입 설정
			responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(exitStorage.toPath()));
			// 파일 인코딩 설정
			responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + URLEncoder.encode("a", "UTF-8"));
			return new ResponseEntity<>(img, responseHeaders, HttpStatus.OK);
		}
		return null;
	}

	// 스타일 게시판 목록에 회원 프로필 이미지 출력
	@PostMapping(value = "img/{mNum}")
	public ResponseEntity<?> showProfileList(@PathVariable Long mNum) throws IOException {

		File saveDirFile = new File(saveDirectory);

		// 해당 경로의 정보 필요 -> listFiles로 해당 경로의 정보 얻어옴
		File[] files = saveDirFile.listFiles();
		Resource img = null;
		for (File f : files) {
			// .을 기준으로, 확장자명 앞에 있는 파일명 얻음
			StringTokenizer stk = new StringTokenizer(f.getName(), ".");
			String fileName = stk.nextToken();

			// 해당 파일들 중에서 원하는 파일 이름과 일치하는 파일명이 존재할 경우
			if (fileName.equals("m_" + mNum)) {
				img = new FileSystemResource(f);

				// response 헤더 설정을 따로 하지 않을 경우, 응답 헤더의 content-type이 application/json 타입으로 되므로
				// 설정 필요함
				HttpHeaders responseHeaders = new HttpHeaders();
				// 파일 크기 설정
				responseHeaders.set(HttpHeaders.CONTENT_LENGTH, f.length() + "");
				// 파일 타입 설정
				responseHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(f.toPath()));
				// 파일 인코딩 설정
				responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
						"inline; filename=" + URLEncoder.encode("a", "UTF-8"));
				return new ResponseEntity<>(img, responseHeaders, HttpStatus.OK);
			}
		}

		return null;
	}

	// 회원 프로필 조회(개인 정보만 조회)
	@GetMapping(value = "detail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> detail(HttpSession session) throws FindException, IOException {
		Long mNum = (Long) session.getAttribute("logined");
		MemberDTO dto = ms.detailMember(mNum);

		return new ResponseEntity<>(dto, HttpStatus.OK);

	}

	// 회원 정보 수정
	@PutMapping(value = "edit")
	public ResponseEntity<?> edit(HttpSession session, @RequestBody MemberDTO dto) throws AddException, FindException {
		Long mNum = (Long) session.getAttribute("logined");

		if (mNum != null) {
			ms.updateProfile(mNum, dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// 회원 탈퇴시 탈퇴 가능 여부 확인 후, id null && outCk를 -1로 변경
	@PutMapping("out/{checkCnt}")
	public ResponseEntity<?> out(HttpSession session, @PathVariable Integer checkCnt) throws FindException {
		Long mNum = (Long) session.getAttribute("logined");

		Integer cnt = ms.checkOutTerms(mNum);

		if (checkCnt == 5 && cnt == 1) {
			ms.updateIdNull(mNum);
			session.invalidate();
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// 회원 정보 삭제
	@DeleteMapping("del")
	public ResponseEntity<?> delete(HttpSession session) throws RemoveException, FindException {
		Long mNum = (Long) session.getAttribute("logined");

		ms.deleteMember(mNum);

		delProfile(mNum);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("delprofile")
	public ResponseEntity<?> deleteProfile(HttpSession session) throws RemoveException, FindException {
		Long mNum = (Long) session.getAttribute("logined");
		delProfile(mNum);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 회원 프로필 삭제
	public void delProfile(Long mNum) {
		File saveDirFile = new File(saveDirectory);

		// 해당 경로의 정보 필요 -> listFiles로 해당 경로의 정보 얻어옴
		File[] files = saveDirFile.listFiles();
		for (File f : files) {
			// .을 기준으로, 확장자명 앞에 있는 파일명 얻음
			StringTokenizer stk = new StringTokenizer(f.getName(), ".");
			String fileName = stk.nextToken();

			// 해당 파일들 중에서 원하는 파일 이름과 일치하는 파일명이 존재할 경우
			if (fileName.equals("m_" + mNum)) {
				f.delete();
				break;
			}
		}
	}
}