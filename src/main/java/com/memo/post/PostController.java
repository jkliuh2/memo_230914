package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.domain.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;
	
	/**
	 * 글 list 화면
	 * http://localhost/post/post-list-view
	 * @param model
	 * @param session
	 * @return
	 */
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
	
	
	// 글 상세 화면
	@GetMapping("/post-detail-view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			Model model,
			HttpSession session
			) {
		
		// 로그인 검사 => 로그인 안되면 애초에 여기 못들어오기 때문에 생략
		
		// 세션에서 userId 꺼내기 => 여기까지 왔으면 로그인은 무조건 되어있음
		int userId = (int)session.getAttribute("userId");
		
		// DB 조회 - postId로 select
		// +) 로그인된 유저의 정보까지 조회 -> userId까지 검증
		Post post = postBO.getPostByPostIdUserId(postId, userId);
		
		// 응답값
		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/postDetail");
		
		return "template/layout";
	}
}
