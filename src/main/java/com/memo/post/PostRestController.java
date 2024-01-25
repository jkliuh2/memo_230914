package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@RestController
public class PostRestController {
// API용 컨트롤러(Map 리턴)
	
	@Autowired
	private PostBO postBO;
	
	/**
	 * 글쓰기 API
	 * 
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PostMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session
			) {
		
		// 글쓴이 번호 - session에 있는 userId를 꺼낸다.
		int userId = (int)session.getAttribute("userId"); 
		// int로 설정하면, null일 경우 NPE -> 로그인 풀려있을 때.
		// => 권한 검사를 앞에서 해주면 문제 없어진다.
		
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		// DB insert (성공시 1)
		int rowCount = postBO.addPost(userId, userLoginId, subject, content, file);
		
		// 응답값
		Map<String, Object> result = new HashMap<>();
		if (rowCount > 0) {
			result.put("code", 200);		
			result.put("result", "성공");		
		} else {
			result.put("code", 500);
			result.put("error_message", "DB 저장에 실패했습니다. 다시 시도해주세요.");
		}
		
		return result;
	}
	
	
	/**
	 * 글 수정 API
	 * 
	 * @param postId
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PutMapping("/update")
	public Map<String, Object> update(
			@RequestParam("postId") int postId,
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session
			) {
		
		// 로그인 검사(권한검사는 한방에 할 것이므로 생략)
		
		// 세션에서 관련 정보 가져오기
		int userId = (int)session.getAttribute("userId"); // 비로그인 상태면 NULL이 처리X -> 오류날것임
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		// db update
		postBO.updatePostById(userId, userLoginId, postId, subject, content, file);
		
		// 응답
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);		
		result.put("result", "성공");		
		
		return result;
	}
	
	
	/**
	 * 글 삭제 API
	 * 
	 * @param postId
	 * @param session
	 * @return
	 */
	@DeleteMapping("/delete")
	public Map<String, Object> delete(
			@RequestParam("postId") int postId,
			HttpSession session) {
		
		// 로그인 된 사람의 userId 가져오기
		int userId = (int)session.getAttribute("userId"); // 권한검사는 한번에 해서, 무조건 값은 들어오게 됨
		
		// DB 
		postBO.deletePostByPostIdUserId(userId, postId);
		
		// 응답
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		
		return result;
	}
}
