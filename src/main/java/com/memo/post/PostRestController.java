package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	// 글쓰기 API
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
}
