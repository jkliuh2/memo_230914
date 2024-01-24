<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글 상세</h1>
		
		<%-- 제목, 내용 input --%>
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요." value="${post.subject}">
		<textarea id="content" class="form-control" rows="10" placeholder="내용을 입력하세요.">${post.content}</textarea>
		
		<%-- 이미지가 있을 경우에만 영역 노출 --%>
		<c:if test="${not empty post.imagePath}">
		<div class="my-3">
			<img src="${post.imagePath}" alt="업로드 된 이미지" width="300">
		</div>
		</c:if>
		
		<%-- 그림 파일 input --%>
		<div class="d-flex justify-content-end my-3">
			<input type="file" id="file" accept=".jpg, .png, .gif, .jpeg">
		</div>
		
		<%-- 버튼 3종 --%>
		<div class="d-flex justify-content-between">
			<%-- 삭제 버튼 --%>
			<button type="button" id="deleteBtn" class="btn btn-secondary" data-post-id="${post.id}">삭제</button>
			
			<div>
				<%-- 목록으로 / 수정 버튼 --%>
				<a href="/post/post-list-view" class="btn btn-dark">목록</a>
				<button type="button" id="updateBtn" class="btn btn-info" data-post-id="${post.id}">수정</button>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function() {
		// 글 수정 버튼
		$('#updateBtn').on('click', function() {
			let postId = $(this).data("post-id");
			let subject = $('#subject').val().trim();
			let content = $('#content').val();
			let fileName = $('#file').val(); // 파일명은 그냥 val() // C:\fakepath\winter878423.jpg
			
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
				let extension = fileName.split(".").pop().toLowerCase(); // 확장자만 뽑아냄 
				
				if ($.inArray(extension, ['jpg', 'png', 'gif', 'jpeg']) == -1) {
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$('#file').val("");
					return;
				}
			}
			
			// 이미지를 업로드 할 때는 반드시 form태그로 구성한다.
			let formData = new FormData(); // 비어있는 form태그 만든 것과 같은 효과
			formData.append("postId", postId);
			formData.append("subject", subject);
			formData.append("content", content);
			formData.append("file", $('#file')[0].files[0]);  // file 타입은 이렇게 가져온다.
			
			// AJAX
			$.ajax({
				// request
				type:"PUT"  // 글 수정시의 type
				, url:"/post/update"
				, data:formData   // 이렇게 보내는 순간, 보내는 타입은 String이 아니다.
				, enctype:"multipart/form-data"    // 파일 업로드를 위한 필수 설정
				, processData:false  // 필수 설정2
				, contentType:false  // 필수 설정3
				
				// response
				, success:function(data) {
					if (data.code == 200) {
						// 성공시 -> alert 띄우고, 이 페이지 새로고침
						alert("메모가 수정되었습니다.");
						location.reload(true);  // true 안넣어도 무관
					} else {
						// 로직 실패
						alert(data.error_message);
					}
				}
				, error:function(request, status, error) {
					// AJAX 자체가 실패
					alert("글을 수정하는데 실패했습니다. 관리자에게 문의해주세요.");
				}
			
			}); // AJAX 끝
		}); // 글수정버튼 이벤트 끝
		
		
		// 글 삭제버튼
		$('#deleteBtn').on('click', function() {
			let postId = $(this).data("post-id");
			
			// AJAX - delete
			$.ajax({
				type:"DELETE"
				, url:"/post/delete"
				, data:{"postId":postId}
			
				, success:function(data) {
					if (data.code == 200) {
						// 삭제 성공
						alert("글을 삭제했습니다.");
						location.href = "/post/post-list-view";
					} else {
						// 로직 실패
						alert(data.error_message);
					}
				}
				, error:function(request, status, error) {
					alert("글을 삭제하는데 실패했습니다. 관리자에게 문의해주세요.");
				}
			}); // AJAX 끝
		}); // 글삭제버튼 끝
	}); // 레디이벤트 끝
</script>