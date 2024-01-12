<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 로그인 section 영역 --%>
<div class="d-flex justify-content-center align-items-center w-100 h-100">
	<div class="col-2">
		<%-- 로그인 form --%>
		<form id="signInForm" method="post" action="/user/sign-in">
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
		
	}); // 레디이벤트
</script>