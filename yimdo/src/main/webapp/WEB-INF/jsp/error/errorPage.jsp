<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>

<style type="text/css">
.contentArea {
	padding: 10px;
}
</style>

<jsp:include page="../common/title.jsp" />

<%-- 
<script type="text/javascript">

document.addEventListener("DOMContentLoaded", function(){
	
	showAlert();
});


function showAlert() {
	
	let statusCode = "${requestScope['javax.servlet.error.status_code']}";
	
	switch (statusCode) {
	
		case '403':
			location.href = '/general/home.do';
			break;
	
		default:
			break;
	}
}
</script>
--%>

</head>
<body>
	<c:set var="statusCode" value="${requestScope['javax.servlet.error.status_code']}" />
	<c:set var="requestUri" value="${requestScope['javax.servlet.error.request_uri']}" />
	<c:set var="exceptionType" value="${requestScope['javax.servlet.error.exception_type']}" />
	<c:set var="exception" value="${requestScope['javax.servlet.error.exception']}" />

	<div class="contentArea">
		<h1>에러 페이지</h1>
		<p>에러코드: ${statusCode}</p>
		<p>요청경로: ${requestUri}</p>
		<c:choose>
			<c:when test="${statusCode eq 403}">
				<p>내용: 이 페이지에 접속 권한이 없습니다.</p>
				<c:redirect url="/general/home.do" />
			</c:when>
			<c:when test="${statusCode eq 404}">
				<p>내용: 잘못된 경로입니다. 올바른 방법으로 접근해주세요.</p>
			</c:when>
			<c:when test="${statusCode eq 500}">
				<p>내용: 서버에 요청이 전송되었으나 서버가 거부하였습니다. 올바른 방법으로 접근해주세요.</p>
				<%-- <c:redirect url="/general/home.do" /> --%>
			</c:when>
		</c:choose>
		<%-- 
		<c:if test="${not empty exceptionType}">
			<p>Exception Type: ${exceptionType}</p>
		</c:if>
		<c:if test="${not empty exception}">
			<p>Exception Message: ${exception.message}</p>
		</c:if>
		--%>
		<p>
			<a href="<c:url value='/general/home.do' />">기본화면으로 돌아가기</a>
		</p>
	</div>
</body>
</html>