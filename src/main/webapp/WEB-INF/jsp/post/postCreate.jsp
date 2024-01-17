<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글쓰기</h1>
		
		<%-- 제목, 내용 input --%>
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요.">
		<textarea id="content" class="form-control" rows="10" placeholder="내용을 입력하세요."></textarea>
		
		<%-- 그림 파일 input --%>
		<div class="d-flex justify-content-end my-3">
			<input type="file" id="file" accept=".jpg, .png, .gif, .jpeg">
		</div>
		
		<%-- 버튼 3종 --%>
		<div class="d-flex justify-content-between">
			<%-- 목록으로 이동(AJAX) --%>
			<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
			
			<div>
				<%-- 모두 지우기 / 저장 버튼 --%>
				<button type="button" id="clearBtn" class="btn btn-secondary">모두 지우기</button>
				<button type="button" id="saveBtn" class="btn btn-info">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function() {
		
		// 목록 버튼 클릭 => 목록 화면 이동
		$('#postListBtn').on('click', function() {
			location.href = "/post/post-list-view";
		}); 
		
		// 모두 지우기 버튼 클릭
		$('#clearBtn').on('click', function() {
			//alert("모두 지우기");
			$('#subject').val(""); // subject의 값을 "" 으로 변경
			$('#content').val("");
		});
		
		
		// 저장 버튼 클릭
		$('#saveBtn').on('click', function() {
			//alert("저장");
			
			let subject = $('#subject').val().trim();
			let content = $('#content').val();
			let fileName = $('#file').val();
			//alert(fileName); C:\~~~
			
			// validation check
			if (!subject) {
				alert("제목을 입력하세요.");
				return;
			}
			if (!content) {
				alert("내용을 입력하세요.");
				return;
			}
			
			// 파일이 업로드 된 경우에만 확장자 체크
			if (fileName) {
				//alert("파일이 있다.");
				// c:\~~~~~\파일이름.확장자  => . 기준으로 잘라서 뒤를 확인
				// pop() -> 가장 뒤에 있는 값 뽑아낸다
				// toLowerCase() -> 모두 소문자로 변경
				let extension = fileName.split(".").pop().toLowerCase(); // 확장자만 뽑아냄 
				
				if ($.inArray(extension, ['jpg', 'png', 'gif', 'jpeg']) == -1) {
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$('#file').val("");
					return;
				}
			}
			
			// form 태그를 js에서 만든다.
			// 이미지를 업로드 할 때는 반드시 form 태그가 있어야 한다.
			let formData = new FormData();
			formData.append("subject", subject); // ("name값", value값) => request parameter
			formData.append("content", content);
			formData.append("file", $('#file')[0].files[0]);
			
			// AJAX
			$.ajax({
				// request
				type:"POST"
				, url:"/post/create"
				, data:formData
				, enctype:"multipart/form-data" // 파일 업로드를 위한 필수 설정
				, processData:false // 파일 업로드를 위한 필수 설정2
				, contentType:false // 파일 업로드를 위한 필수 설정3
				
				// response
				, success:function(data) {
					if (data.code == 200) {
						alert("메모가 저장되었습니다.");
						location.href = "/post/post-list-view"
					} else {
						alert(data.error_message);
					}
				}
				, error:function(request, status, error) {
					alert("글을 저장하는데 실패했습니다.");
				}
			}); // ajax 끝
		}); // 저장 이벤트 끝
	}); // 레디이벤트
	
</script>