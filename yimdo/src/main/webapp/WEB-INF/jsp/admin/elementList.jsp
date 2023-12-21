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
	$(document).ready(function(){
  
	  // 버튼 클릭 시 색 변경
		$('table tr').mouseover(function(){ 
			$(this).css("backgroundColor","#faf8f1"); 
		}); 

		$('table tr').mouseout(function(){ 
			$(this).css("backgroundColor","#fff"); 
		}); 
	});

// 레이어팝업
;(function($) {

         // DOM Ready
        $(function() {
            // Binding a click event
            // From jQuery v.1.7.0 use .on() instead of .bind()
            $('#popbtn1').bind('click', function(e) {
                // Prevents the default action to be triggered. 
                e.preventDefault();
                // Triggering bPopup when click event is fired
                $('#popup_1').bPopup();
            });

            $('#popbtn2').bind('click', function(e) {
                // Prevents the default action to be triggered. 
                e.preventDefault();
                // Triggering bPopup when click event is fired
                $('#popup_2').bPopup();
            });

            $('#popbtn3').bind('click', function(e) {
                // Prevents the default action to be triggered. 
                e.preventDefault();
                // Triggering bPopup when click event is fired
                $('#popup_3').bPopup();
            });

        });

    })(jQuery);
</script>

<script type="text/javascript">
$(function() {
	
	// 해더안 해당기능 관련 영역 색 변경
	$('ul.gnb_menu li:nth-child(4) a').removeClass('bul').addClass('bul bulon');
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

                <!-- S : sub_head -->              
                <div class="sub_head">
                    <div class="loacation_area">
                    <ol>
                    <li><a href="/" class="home"><span class="screen_out">Home</span></a></li>
                    <li><span><strong>출입제한인자구분</strong></span></li>
                    </ol>
                    </div>
                    <h2 class="titlesb">출입제한인자구분</h2>
                </div>
                <!-- E : sub_head -->              

                <!-- S : content_area -->    
				<div class="content_area mt15">    
                    <!-- S : table_typA -->    
                    <div class="table_typA">
                    <table class="contTbl txtC" summary="이 표는 목록입니다." >
                    <colgroup>
                        <col style="width:30%">
                        <col style="width:30%">
                        <col style="width:30%">
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="col">차단기ID</th>
                            <th scope="col">임도명</th>
                            <th scope="col">차단기상태</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- 산림경영시작 -->
                        <tr>
                            <td rowspan="4">산림경영</td>
                            <td rowspan="2">경제적 경영목적</td>
                            <td>산림사업</td>
                        </tr>
                        <tr>
                            <td>임목수확작업</td>
                        </tr>
                         <tr>
                            <td rowspan="2">사회적 경영목적</td>
                            <td>여가활동</td>
                         </tr>
                         <tr>
                            <td>지역축제</td>
                         </tr>
                         <!-- 산림경영 끝 -->

                         <!-- 임도관리시작 -->
                         <tr>
                            <td rowspan="10">임도관리</td>
                            <td rowspan="4">유지관리</td>
                            <td>사면관리</td>
                        </tr>
                        <tr>
                            <td>노면관리</td>
                        </tr>
                        <tr>
                            <td>배수시설관리</td>
                        </tr>
                        <tr>
                            <td>장애물관리</td>
                        </tr>
                         <tr>
                            <td rowspan="3">재해관리</td>
                            <td>응금상황발생</td>
                         </tr>
                         <tr>
                            <td>산림재해조심·예방기간</td>
                         </tr>
                         <tr>
                            <td>산림병해충관리</td>
                         </tr>
                         <tr>
                            <td rowspan="3">운영관리</td>
                            <td>임도조사 모니터링</td>
                         </tr>
                         <tr>
                            <td>출입인원·차량관리</td>
                         </tr>
                         <tr>
                            <td>출입시간제한</td>
                         </tr>
                         <!-- 임도관리 끝 -->

                         <!-- 자연환경시작 -->
                         <tr>
                            <td rowspan="14">자연환경</td>
                            <td rowspan="4">기후</td>
                            <td>강우</td>
                        </tr>
                        <tr>
                            <td>풍속</td>
                        </tr>
                        <tr>
                            <td>건조</td>
                        </tr>
                        <tr>
                            <td>지진</td>
                        </tr>
                         <tr>
                            <td rowspan="3">지형</td>
                            <td>수계</td>
                         </tr>
                         <tr>
                            <td>모암</td>
                         </tr>
                         <tr>
                            <td>임상</td>
                         </tr>
                         <tr>
                            <td rowspan="4">생태</td>
                            <td>식생</td>
                         </tr>
                         <tr>
                            <td>곤충</td>
                         </tr>
                         <tr>
                            <td>조류</td>
                         </tr>
                         <tr>
                            <td>해양생물</td>
                         </tr>
                         <tr>
                            <td rowspan="3">위치</td>
                            <td>지리적위치</td>
                         </tr>
                         <tr>
                            <td>역사적</td>
                         </tr>
                         <tr>
                            <td>문화적</td>
                         </tr>
                         <!-- 자연환경 끝 -->
                    </tbody>
                    </table>
                    </div>			                
                    <!-- E : table_typA -->    
				</div>
                <!-- E : content_area -->              

            </div>
            <!-- E : content_doc -->              
        </div>
        <!-- E : side_content -->              

    </div>
    <!-- E : sec_container -->              
</div>
<!-- E : admi_section -->              
<!-- S : 레이어팝업 -->    
<div id="popup_1" class="popup_wrap">
    <div class="title_pop">
        <h2>차단기 상세정보</h2>
        <button type="button" class="btn-close " tabindex="0" title="창닫기"><span class="blind">창닫기</span></button>
    </div>
    <div class="pop_doc">
        <div class="pop_doc_box">
        상세내용 출력됨
        </div>
        <!-- small버튼 A테그 -->
        <div class="btn_popgroup_wrap mt10">
        <div class="btn_g_right">
        <a href="#none" class="btn_a_small_03"><span>취소</span></a>
        <a href="#none" class="btn_a_small_01"><span>확인</span></a>
        </div>
        </div>
        <!-- small버튼 A -->
    </div>
</div>
<!-- E : 레이어팝업 -->    

<jsp:include page="./common/adminPageFooter.jsp" />
             
</div>
<!-- E : wrap -->              

<script>	
// 달력
$(function() {
	$('input[id^="dateStar"]').datepicker();
	$('input[id^="dateEnd"]').datepicker();
	$('input[id^="datepickers"]').datepicker();
	$.datepicker.setDefaults({
		dateFormat: 'yy-mm-dd',
		prevText: '이전 달',
		nextText: '다음 달',
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		showMonthAfterYear: true,
		changeYear:true, //콤보박스에서 년 선택 가능  
		changeMonth: true, //콤보박스에서 월 선택 가능  
		//yearSuffix: '년',
	});

});
</script>
</body>
</html>
