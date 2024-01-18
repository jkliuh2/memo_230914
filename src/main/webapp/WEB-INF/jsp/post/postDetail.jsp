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
			<button type="button" id="deleteBtn" class="btn btn-secondary">삭제</button>
			
			<div>
				<%-- 목록으로 / 수정 버튼 --%>
				<a href="/post/post-list-view" class="btn btn-dark">목록</a>
				<button type="button" id="updateBtn" class="btn btn-info">수정</button>
			</div>
		</div>
	</div>
</div>

<script>

</script>