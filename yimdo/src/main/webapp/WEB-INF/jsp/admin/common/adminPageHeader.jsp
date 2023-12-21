<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- S : header_wrap -->              
<div class="header_wrap">
	<div class="head_in">
    <!-- S : logo_box -->              
     <div class="logo_box">
         <a href="<c:url value='/admin/main.do' />" class="link_kesco" title="서비스바로가기"><span class="blind">서비스바로가기</span></a>
         <div class="link_logo" title="스마트임도관리시스템"><span>스마트임도관리시스템</span></div>
     </div>
    <!-- E : logo_box -->              
    <!-- S : utility -->              
     <ul class="utility">
        <li class="total_menu"><a href="#none" class="btn_menu btn_total" ><span class="blind">전체메뉴 보기</span></a></li>
        <li class="total_menu"><a href="<c:url value='/general/logout.do' />" class="btn_menu btn_total02" ><span class="blind">로그아웃</span></a></li>
     </ul>
    <!-- E : utility --> 
    </div>
</div>
<!-- E : header_wrap -->

 <!-- S : gnb -->              
<div class="gnb">
	<div class="blind">관리시스템 주요서비스</div>
	<ul class="gnb_menu">
		<li class="depth1">
			<a href="<c:url value = '/admin/breakerStatus/breakerStatusPage.do' />" class="bul" title="차단기상태현황">차단기상태현황</a>
		</li>
		<!--
		<li class="depth1">
			<a href="<c:url value = '/admin/breakerManagement/breakerManagementPage.do' />" class="bul" title="차단기 관리">차단기 관리</a>
		</li>
		-->
		<li class="depth1">
			<a href="<c:url value = '/admin/managementHistory/breakerHistoryPage.do' />" class="bul" title="차단기관리이력">차단기관리이력</a>
		</li>
		<li class="depth1">
			<a href="<c:url value = '/admin/imdoAccessHistory/imdoAccessHistoryPage.do' />" class="bul" title="임도출입이력">임도출입이력</a>
		</li>
		<li class="depth1">
			<a href="<c:url value = '/admin/elementList/elementListPage.do' />" class="bul" title="출입제한 인자구분">출입제한 인자구분</a>
		</li>
		<li class="depth1">
			<a href="<c:url value = '/admin/elementInfo/elementInfoPage.do' />" class="bul" title="출입제한 인자정보">출입제한 인자정보</a>
		</li>
		<li class="depth1">
			<a href="<c:url value = '/admin/weatherData/weatherDataPage.do' />" class="bul" title="기상관측기 기상정보">기상관측기 기상정보</a>
		</li>
	</ul>
</div>
<!-- E : gnb -->