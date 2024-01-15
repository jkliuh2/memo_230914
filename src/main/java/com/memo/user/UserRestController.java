package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {
	// API용 컨트롤러
	
	@Autowired 
	private UserBO userBO;
	
	/**
	 * 중복 확인 API
	 * 
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is-duplicated-id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId) {
		
		// DB select
		UserEntity user = userBO.getUserEntityByLoginId(loginId);
		
		// 응답값
		Map<String, Object> result = new HashMap<>();
		
		if (user != null) { // user 존재 -> 중복
			result.put("code", 200);
			result.put("is_duplicated_id", true);
		} else { // user 없음 -> 중복X
			result.put("code", 200);
			result.put("is_duplicated_id", false);
		}
		
		return result;
	}
	
	/**
	 * 회원가입 API
	 * 
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email) {
		
		// md5 알고리즘(비밀번호 암호화)(보안 약한 편)
		// => password hashing
		// aaaa => 74b8733745420d4d33f80c4663dc5e5
		String hashedPassword = EncryptUtils.md5(password);
		
		// DB insert -> pk값 리턴
		Integer userId = userBO.addUser(loginId, hashedPassword, name, email);
		
		// 응답
		Map<String, Object> result = new HashMap<>();
		if (userId != null) { // 성공
			result.put("code", 200);
			result.put("result", "성공");
		} else { // insert 실패
			result.put("code", 500);
			result.put("error_message", "회원가입에 실패했습니다.");
		}
		
		return result;
	}
	
	
	// 로그인 API
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request) {
		
		// 비밀번호 hashing - md5방식
		String hashingPassword = EncryptUtils.md5(password);
		
		// DB select(loginId, 해싱된 비밀번호) - 여기서는 UserEntity를 리턴해볼거임
		// (id만 리턴해도 되긴 하지만, 이걸 또 다른곳에 저장할 필요가 있어서 Entity로 리턴)
		UserEntity user = userBO.getUserEntityByLoginIdPassword(loginId, hashingPassword);
		
		// 응답
		Map<String, Object> result = new HashMap<>();
		if (user != null) { // 성공
			// 로그인 처리
			// 로그인 정보를 세션에 담는다.(사용자 마다)
			HttpSession session = request.getSession();
			session.setAttribute("userId", user.getId());
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());
			
			result.put("code", 200);
			result.put("result", "성공");
		} else { // 로그인 불가
			result.put("code", 300);
			result.put("error_message", "존재하지 않는 사용자입니다.");
		}
		
		return result;
	}
}
