<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
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
	
	$('ul.gnb_menu li:nth-child(5) a').removeClass('bul').addClass('bul bulon');
	
	fetch_getElementList();
})

function fetch_getElementList(pageNo) {
	
	if (pageNo <= 0 || pageNo == null) pageNo = 1;
	
	let searchKeyword = $('#elementSearch').val();
	let queryString = "pageIndex=" + pageNo + 
                      "&searchKeyword=" + searchKeyword +
                      "&searchUseYn=" + "y";
	
	fetch("/common/getElementList.do?" + queryString)
	.then(response => {
		if (response.ok) return response.json();
		else throw new Error("네트워크 에러");
	})
	.then(data => {
		
		let modifyElementVos = data.modifyElementVos;
		let paginationInfo = data.paginationInfo;
		let prev = (paginationInfo.firstPageNoOnPageList == 1 ? 1 : (paginationInfo.currentPageNo - paginationInfo.pageSize < 1 ? 1 : paginationInfo.currentPageNo - paginationInfo.pageSize));
		let next = (paginationInfo.lastPageNoOnPageList == paginationInfo.lastPageNo ? paginationInfo.lastPageNo : (paginationInfo.currentPageNo + paginationInfo.pageSize > paginationInfo.lastPageNo ? paginationInfo.lastPageNo : paginationInfo.currentPageNo + paginationInfo.pageSize));
		
		$('#element_totalCnt').html("<span class='data_num'>총 <strong>" + paginationInfo.totalRecordCount + "</strong>건의 게시물이 있습니다. <em>(" + paginationInfo.totalPageCount + "페이지)</em></span>");
		
		$("#pop_elementList").empty();
		modifyElementVos.forEach(modifyElementVo => {
			
			$("#pop_elementList").append('<tr><td>' + modifyElementVo.elementCode + '</td><td>' + modifyElementVo.elementName + '</td><td>' + modifyElementVo.elementValue + modifyElementVo.elementUnit + '</td><td><input type="number" id="' + modifyElementVo.elementCode + '" class="form wp70"><a href="#none" class="btn_list_src" onclick="fetch_updateElement(' + modifyElementVo.elementCode + '); return false;"><span>적용</span></a></td></tr>');
		})
		
		$("#pop_pageBtn").empty();
		$("#pop_pageBtn").append('<button type=\"button\" class=\"btn_page_first\" title=\"맨앞 페이지\" onclick=\"fetch_getElementList(1); return false;\">맨 처음</button>');
		$("#pop_pageBtn").append('<button type=\"button\" class=\"btn_page_prev\" title=\"이전 페이지\" onclick=\"fetch_getElementList(' + prev + '); return false;\">이전</button>');
		
		for (let i = paginationInfo.firstPageNoOnPageList; i <= paginationInfo.lastPageNoOnPageList; i++) {
			
		  if (i == paginationInfo.currentPageNo) {
			  
			 $("#pop_pageBtn").append('<a class="ml2 mr2 current" href=\"#\" id="currentPage" data-currentPage="' + i + '" title=\"현재 ' + i + '페이지\" >' + i + '</a>');
			 
		  } else {
			  
			 $("#pop_pageBtn").append('<a class="ml2 mr2" href=\"#\" title=\"' + i + '페이지\" onclick=\"fetch_getElementList(' + i + ');\ return false;">' + i + '</a>');
		  }
		}
		$("#pop_pageBtn").append('<button type=\"button\" class=\"btn_page_next\" title=\"다음 페이지\" onclick=\"fetch_getElementList(' + next + '); return false;\">다음</button>');
		$("#pop_pageBtn").append('<button type=\"button\" class=\"btn_page_last\" title=\"맨 마지막\" onclick=\"fetch_getElementList(' + paginationInfo.lastPageNo + '); return false;\">맨 마지막</button>');
	})
	.catch(error => {
		
		console.error("fetch에러: ", error);
	})
}

function fetch_updateElement(elementCode) {
	
	let msg = {
		"elementCode": elementCode,
		"elementValue": $("#" + elementCode).val()
	};
	
	fetch("/admin/elementInfo/updateElement.do", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
            "${_csrf.headerName}": "${_csrf.token}"
		},
		body: JSON.stringify(msg)
	})
	.then(response => {
		if (response.ok) return response.json();
		else throw new Error("네트워크 에러");
	})
	.then(data => {
		
		if (data.result == "success") {
			
			alert("정상적으로 적용되었습니다.");
			
		} else {
			
			alert("적용에 실패하였습니다. 잠시후 다시 시도해주세요.");
		}
		
		fetch_getElementList($("#currentPage").data("currentPage"))
	})
	.catch(error => {
		
		console.error("fetch에러: ", error);
	})
}
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
                    <li><span><strong>출입정책인자정보</strong></span></li>
                    </ol>
                    </div>
                    <h2 class="titlesb">출입정책인자정보</h2>
                </div>
                <!-- E : sub_head -->              


                <!-- S : content_area -->    
				<div class="content_area mt15">
                    <!-- S : sarchTxpa_box -->    
                    <div class="sarchTxpa_box">
                        <div id="element_totalCnt" class="pacount">
                        <span class="data_num">총 <strong> 2145</strong>건의 게시물이 있습니다. <em>(9페이지)</em></span>
                        </div>
                        <div class="srcR_box ">
                            <input  type="text" id="elementSearch" class="form wp65 " title="입력하세요">
                            <a href="#none" class="btn_list_src" onclick="fetch_getElementList(); return false;"><span>조회</span></a>
                        </div>
                    </div>
                    <!-- E : sarchTxpa_box -->    
                    <!-- S : table_typA -->    
                    <div class="table_typA">
                    <table class="contTbl txtC" summary="이 표는 목록입니다." >
                    <colgroup>
                        <col style="width:25%">
                        <col style="width:30%">
                        <col style="width:25%">
                        <col style="width:20%">
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="col">인자코드</th>
                            <th scope="col">인자명</th>
                            <th scope="col">정상범위 기준값</th>
                            <th scope="col">기준값 변경</th>
                        </tr>
                    </thead>
                    <tbody id="pop_elementList">
                    </tbody>
                    </table>
                    </div>			                
                    <!-- E : table_typA -->    

                    <!-- S : paging_wrap -->
                    <div id="pop_pageBtn" class="paging_wrap mt15 mb15">
                    </div>
                    <!-- E : paging_wrap -->
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

<!-- S : footer_wrap -->              
<jsp:include page="./common/adminPageFooter.jsp" />
<!-- E : footer_wrap -->              
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