<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 로그인 section 영역 --%>
<div class="d-flex justify-content-center align-items-center w-100 h-100">
	<div class="col-2">
		<%-- 로그인 form --%>
		<form id="loginForm" method="post" action="/user/sign-in">
			<div class="d-flex justify-content-center align-items-center w-100">
				<div id="login-input-box">
				
					<%-- loginId input --%>
					<input type="text" id="loginId" name="loginId" 
						placeholder="Username" class="form-control my-2"> 
						
					<%-- password input --%>
					<input type="password" id="password" name="password" 
						placeholder="password" class="form-control my-2">
					<div class="d-flex justify-content-between my-2">
						<%-- 회원가입 페이지 이동 버튼 --%>
						<a href="/user/sign-up-view">
							<button type="button" class="log-in-btn btn btn-secondary">회원가입</button>
						</a>
						
						<%-- submit 버튼 --%>
						<button type="submit" class="log-in-btn btn btn-primary">로그인</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<script>
	$(document).ready(function() {
		
		// 로그인 form 버튼 이벤트
		$('#loginForm').on('submit', function(e) {
			e.preventDefault(); // form 기능 막기(화면이동 방지)
			//alert("로그인");
			
			// validation check
			let loginId = $('input[name=loginId]').val().trim();
			let password = $('input[name=password]').val();
			if (!loginId) {
				alert("아이디를 입력하세요.");
				return false;
			}
			if (!password) {
				alert("비밀번호를 입력하세요.");
				return false;
			}
			
			// AJAX - select하고 로그인
			let url = $(this).attr("action");
			//console.log(url);
			let params = $(this).serialize();
			//console.log(params);
			
			$.post(url, params) // request
			.done(function(data) { // response
				if (data.result == "성공") {
					// 로그인 성공 시 글 목록으로 이동
					location.href = "/post/post-list-view";
				} else {
					alert(data.error_message);
				}
			}); // AJAX 끝
		}); // 로그인 form 이벤트
	}); // 레디이벤트
</script>