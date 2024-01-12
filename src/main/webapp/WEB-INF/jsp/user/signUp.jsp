<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 회원가입 section --%>
<div class="d-flex justify-content-center">
	<div class="col-6">
		<!-- 회원 가입 form -->
		<form id="signUpForm" method="post" action="/user/sign-up">
			<div class="border-box border">
				<div class="m-2">
					<label>ID</label>
					<div class="d-flex align-items-center">
						<input id="loginId" type="text" name="loginId" placeholder="ID를 입력해주세요."
							class="form-control col-7">
						<!-- 중복확인 버튼 -->
						<small><button type="button" id="loginId-check-btn" class="btn btn-primary btn-sm ml-3">중복확인</button></small>
					</div>
					<small id="idCheckOk" class="text-success d-none">
						사용 가능한 아이디 입니다.
					</small>
					<small id="idCheckDupl" class="text-danger d-none">
						중복된 아이디 입니다.
					</small>
					<small id="idCheck4Word" class="text-danger d-none">
						ID는 4자 이상이어야 합니다.
					</small>
				</div>
				<div class="m-2">
					<label>password</label> 
					<input type="password" id="password" name="password" placeholder="****" class="form-control col-6">
				</div>
				<div class="m-2">
					<label>confirm password</label> 
					<input type="password" id="confirm-password" 
						placeholder="****" class="form-control col-6">
				</div>
				<div class="m-2">
					<label>이름</label> 
					<input type="text" id="name" name="name"
						placeholder="이름을 입력해주세요" class="form-control col-7">
				</div>
				<div class="m-2">
					<label>이메일</label> 
					<input type="text" id="email" name="email"
						placeholder="이메일을 입력해주세요" class="form-control col-7">
				</div>
				<div class="m-2 d-flex justify-content-center">
					<input type="submit" value="가입하기" class="btn btn-primary">
				</div>
			</div>
		</form>
	</div>
</div>

<script>
	$(document).ready(function() {
		
		// 아이디 중복확인
		$('#loginId-check-btn').on('click', function() {
			//alert("중복");
			
			// 1. 일단 버튼을 누르면 경고문구 초기화
			$("#idCheckOk").addClass("d-none");
			$("#idCheckDupl").addClass("d-none");
			$("#idCheck4Word").addClass("d-none");
			
			// 2. 아이디 4자 이상 문구 띄우기
			let loginId = $('#loginId').val().trim();
			if (loginId.length < 4) {
				$("#idCheck4Word").removeClass("d-none");
				return; // 얘는 그냥 버튼이라 return
			}
			
			// 3. AJAX - 중복확인
			$.get("/user/is-duplicated-id", {"loginId":loginId}) // request
			.done(function(data) { // response
				if (data.code == 200) {
					if (data.is_duplicated_id) { // 중복
						$("#idCheckDupl").removeClass("d-none");
					} else { // 중복X
						$("#idCheckOk").removeClass("d-none");
					}
				} else {
					alert(data.error_message);
				}
			});
			
			
		}); // 중복확인 버튼 클릭이벤트 끝
		
		
		
		// 회원가입
		$('#signUpForm').on('submit', function(e) {
			e.preventDefault(); // submit 기능 끄기.
			
			//alert("회원가입");
			
			// validation check
			let loginId = $('#loginId').val().trim();
			let password = $('#password').val();
			let confirmPassword = $('#confirm-password').val();
			let name = $('#name').val().trim();
			let email = $('#email').val().trim();
			
			if (!loginId) {
				alert("아이디를 입력하세요.");
				return false; // submit 이벤트일때는 false 꼭 넣어주기
			}
			if (!password || !confirmPassword) {
				alert("비밀번호를 입력하세요.");
				return false;
			}
			if (password != confirmPassword) {
				alert("비밀번호가 일치하지 않습니다.");
				return false;
			}
			if (!name) {
				alert("이름을 입력하세요");
				return false;
			}
			if (!email) {
				alert("이메일을 입력하세요");
				return false;
			}
			
			// 중복확인 후 사용 가능한 아이디인지 확인
			// => idCheckOk 클래스 d-none이 없을 때
			if ($("#idCheckOk").hasClass('d-none')) {
				alert("아이디 중복 확인을 다시 해주세요.");
				return false;
			}
			
			// <데이터 전송하기>
			// 1) 서버 전송: submit을 js에서 동작시킴
			//$(this)[0].submit(); // 화면 이동이 된다.
			
			// 2) AJAX: 화면 이동 되지 않음(callback 함수에서 이동) 응답값->JSON
			let url = $(this).attr("action"); // this(form태그)의 action 속성값을 url에 담는다.
			//alert(url);
			let params = $(this).serialize(); // form태그에 있는 name 속성과 값으로 파라미터를 구성.
			//alert(params); // loginId=11111&password=2222&name=33333&email=44444444
			
			$.post(url, params) // request
			.done(function(data) { // response
				// {"code":200, "result":"성공"}
				if (data.code == 200) {
					alert("가입을 환영합니다. 로그인 해주세요.");
					location.href = "/user/sign-in-view"; // 로그인 페이지로 이동
				} else {
					// 로직 실패
					alert(data.error_message);
				}
			
			}); // $.post 끝
			
		}); // submit 이벤트 끝
	}); // 레디이벤트 끝
</script>