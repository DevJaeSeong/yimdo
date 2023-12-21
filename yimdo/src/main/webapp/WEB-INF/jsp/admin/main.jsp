<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html >
<html lang="ko">
<head>
<jsp:include page="../common/adminTitle.jsp" />
<link href="/css/imdo/admin/css/reset.css" media="all" rel="stylesheet" type="text/css">
<link href="/css/imdo/admin/css/common.css" media="all" rel="stylesheet" type="text/css">
<link href="/css/imdo/admin/css/content.css" media="all" rel="stylesheet" type="text/css">
<link href="/css/imdo/admin/css/fonts.css" media="all" rel="stylesheet" type="text/css">
<link href="/css/imdo/admin/css/jquery-ui.css" media="all" rel="stylesheet" type="text/css">

<script type="text/javascript" src="/js/imdo/admin/js/jquery.js"></script>
<script type="text/javascript" src="/js/imdo/admin/js/jquery-ui.js"></script>
<script type="text/javascript" src="/js/imdo/admin/js/jquery.bpopup.min.js"></script>

<script type="text/javascript"> 
$(function() {
	
})
</script>
</head>

<body class="admin_company">
<!-- S : wrap -->              
<div class="wrap">

<jsp:include page="./common/adminPageHeader.jsp" />

<!-- S : admi_section -->              
<div class="admi_section">
    <!-- S : sec_container -->              
	<div class="sec_container">    

        <!-- S : side_content -->              
		<div class="side_content" tabindex="-1">
            <!-- S : content_doc -->              
            <div class="content_doc">

                <div class="Dashbd">
                    <ul class="main_desk_area col-3">
                        <li>
                            <div class="unit">
                            <span class="sTit">차단기상태현황</span>
                            <span class="link_tab"><a href="<c:url value = '/admin/breakerStatus/breakerStatusPage.do' />" class="btn_a_help_desk" title="바로기가"><span>바로가기</span></a></span>
                            </div>
                        </li>
                        <!--
                        <li>
                            <div class="unit">
                            <span class="sTit">차단기관리</span>
                            <span class="link_tab"><a href="<c:url value = '/admin/breakerManagement/breakerManagementPage.do' />" class="btn_a_help_desk" title="바로기가"><span>바로가기</span></a></span>
                            </div>
                        </li>
                        -->
                        <li>
                            <div class="unit">
                            <span class="sTit">차단기관리이력</span>
                            <span class="link_tab"><a href="<c:url value = '/admin/managementHistory/breakerHistoryPage.do' />" class="btn_a_help_desk" title="바로기가"><span>바로가기</span></a></span>
                            </div>
                        </li>
                        <li>
                            <div class="unit">
                            <span class="sTit">임도출입이력</span>
                            <span class="link_tab"><a href="<c:url value = '/admin/imdoAccessHistory/imdoAccessHistoryPage.do' />" class="btn_a_help_desk" title="바로기가"><span>바로가기</span></a></span>
                            </div>
                        </li>
                        <li class="mt15"> 
                            <div class="unit">
                            <span class="sTit">출입제한 관리정책</span>
                            <span class="link_tab"><a href="<c:url value = '/admin/elementList/elementListPage.do' />" class="btn_a_help_desk" title="바로기가"><span>바로가기</span></a></span>
                            </div>
                        </li>
                        <li class="mt15">
                            <div class="unit">
                            <span class="sTit">출입제한 인자관리</span>
                            <span class="link_tab"><a href="<c:url value = '/admin/elementInfo/elementInfoPage.do' />" class="btn_a_help_desk" title="바로기가"><span>바로가기</span></a></span>
                            </div>
                        </li>
                        <li class="mt15">
                            <div class="unit">
                            <span class="sTit">기상관측기 기상정보</span>
                            <span class="link_tab"><a href="<c:url value = '/admin/weatherData/weatherDataPage.do' />" class="btn_a_help_desk" title="바로기가"><span>바로가기</span></a></span>
                            </div>
                        </li>
                    </ul>
                </div>

            </div>
            <!-- E : content_doc -->              
        </div>
        <!-- E : side_content -->              

    </div>
    <!-- E : sec_container -->              
</div>
<!-- E : admi_section -->     
         
<jsp:include page="./common/adminPageFooter.jsp" />    
        
</div>
<!-- E : wrap -->              
</body>
</html>
