<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="d-flex justify-content-between align-items-center h-100">
	<%-- logo 부분 --%>
	<div>
		<h1>MEMO 게시판</h1>
	</div>
	
	<%-- 로그인 정보 --%>
	<div class="d-flex">
		<%-- 로그인 시 --%>
		<c:if test="${not empty userName}">
			<span>${userName}님 안녕하세요</span>
			<a href="/user/sign-out">로그아웃</a>
		</c:if>
		<%-- 비 로그인시 --%>
		<c:if test="${empty userName}">
			<a href="/user/sign-in-view">로그인</a>
		</c:if>
	</div>
</div>