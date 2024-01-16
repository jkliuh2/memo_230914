package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.memo.post.bo.PostBO;
import com.memo.post.domain.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;
	
	
	// 글 내용 있는 부분
	// url: http://localhost/post/post-list-view
	@GetMapping("/post-list-view")
	public String postListView(Model model, HttpSession session) {
		// 로그인 정보 조회(권한 검사) - userId null 검사
		Integer userId = (Integer)session.getAttribute("userId"); // int or null => Integer로 저장+캐스팅
		if (userId == null) {
			// 비-로그인 이면 로그인 페이지로 이동. 끝.
			return "redirect:/user/sign-in-view";
		}
		// 여기까지 왔다면, 로그인이 된 상태임을 뜻한다.
		
		// DB select - 글 목록 조회 => Mybatis
		// 해당 게시판은 "로그인 된 사람이 쓴 글만" 가져오게끔 한다.
		List<Post> postList = postBO.getPostListByUserId(userId);
		
		// 응답값
		model.addAttribute("viewName", "post/postList"); // jsp 경로 모델에 담기
		model.addAttribute("postList", postList);
		
		// 리턴
		return "template/layout";
	}
	
	
	/**
	 * 글쓰기 화면
	 * http://localhost/post/post-create-view
	 * @param model
	 * @return
	 */
	@GetMapping("/post-create-view")
	public String postCreateView(Model model) {
		// 로그인 권한 검사 -> 나중에 한번에 할 것임
		
		// 응답값
		model.addAttribute("viewName", "/post/postCreate");
		
		return "template/layout";
	}
}
