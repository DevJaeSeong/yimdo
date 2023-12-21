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

            $('#searchPurpose').bind('click', function(e) {
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
$(function () {
	
	$('ul.gnb_menu li:nth-child(3) a').removeClass('bul').addClass('bul bulon');

    fetch_getSido();
    fetch_getAccessHistory();
})

function fetch_getSido() {
	
	fetch("/common/getSido.do")
	.then(response => {
		if (response.ok) return response.json();
		else throw new Error("네트워크 에러");
	})
	.then(sidoVos => {
		
		$('#sido').empty();
		$('#sido').append('<option value="">시/도 선택</option>');
		
		sidoVos.forEach(sidoVo => {
			
			$('#sido').append('<option value="' + sidoVo.sidoCode + '">' + sidoVo.sidoName + '</option>');
		})
	})
	.catch(error => {
		
		console.error("fetch에러: ", error);
	})
	
}

function fetch_getSigungu() {
	
	let sido = $('#sido').val();
	
	fetch("/common/getSigungu.do?sido=" + sido)
	.then(response => {
		if (response.ok) return response.json();
		else throw new Error("네트워크 에러");
	})
	.then(sigunguVos => {
		
		$('#sigungu').empty();
		$('#sigungu').append('<option value="">시/군/구 선택</option>');
		
		sigunguVos.forEach(sigunguVo => {
			
			$('#sigungu').append('<option value="' + sigunguVo.sigunguCode + '">' + sigunguVo.sigunguName + '</option>');
		})
	})
	.catch(error => {
		
		console.error("fetch에러: ", error);
	})
	
}

function fetch_getElementList(pageNo) {
	
	if (pageNo <= 0 || pageNo == null) pageNo = 1;
	
	$('#elementSearch').val("")
	let searchKeyword = $('#elementSearch').val();
	let queryString = "pageIndex=" + pageNo + "&searchKeyword=" + searchKeyword;
	
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
		
		$('#element_totalCnt').html("총 <strong>" + paginationInfo.totalRecordCount + "</strong>건의 게시물이 있습니다. <em>(" + paginationInfo.totalPageCount + "페이지)</em>");
		
		$("#pop_elementList").empty();
		modifyElementVos.forEach(modifyElementVo => {
			
			$("#pop_elementList").append('<tr><td style="text-align: center;">' + modifyElementVo.elementName + '</td><td class="ac"><input type="button" class="btn_addsq" onclick="selectElement(' + modifyElementVo.elementCode + ', \'' + modifyElementVo.elementName + '\'); return false;" style="width: 65px; cursor: pointer;" value="선택"></td></tr>');
		})
		
		$("#pop_pageBtn").empty();
		$("#pop_pageBtn").append('<button type=\"button\" class=\"btn_page_first\" title=\"맨앞 페이지\" onclick=\"fetch_getElementList(1); return false;\">맨 처음</button>');
		$("#pop_pageBtn").append('<button type=\"button\" class=\"btn_page_prev\" title=\"이전 페이지\" onclick=\"fetch_getElementList(' + prev + '); return false;\">이전</button>');
		
		for (let i = paginationInfo.firstPageNoOnPageList; i <= paginationInfo.lastPageNoOnPageList; i++) {
			
		  if (i == paginationInfo.currentPageNo) {
			  
			 $("#pop_pageBtn").append('<a class="ml2 mr2 current" href=\"#\" title=\"현재 ' + i + '페이지\" >' + i + '</a>');
			 
		  } else {
			  
			 $("#pop_pageBtn").append('<a class="ml2 mr2" href=\"#\" title=\"' + i + '페이지\" onclick=\"fetch_getElementList(' + i + '); return false;\">' + i + '</a>');
		  }
		}
		
		$("#pop_pageBtn").append('<button type=\"button\" class=\"btn_page_next\" title=\"다음 페이지\" onclick=\"fetch_getElementList(' + next + '); return false;\">다음</button>');
		$("#pop_pageBtn").append('<button type=\"button\" class=\"btn_page_last\" title=\"맨 마지막\" onclick=\"fetch_getElementList(' + paginationInfo.lastPageNo + '); return false;\">맨 마지막</button>');
	})
	.catch(error => {
		
		console.error("fetch에러: ", error);
	})
}

function selectElement(elementCode, elementName) {
	
	$('#selectedElement').val(elementCode);
	$('#selectedElementName').val(elementName);
	$('#popup_2').bPopup().close();
}

function reset() {

    $('#sido').val("");
    $('#sigungu').empty();
    $('#sigungu').append('<option value="">시/군/구 선택</option>');
	$('#breakerId').val("");
	$('#userNm').val("");
	$("#selectedElement").val("");
	$("#selectedElementName").val("");
    $("#dateStar").val("");
	$("#dateEnd").val("");
	
	fetch_getAccessHistory();
}

function fetch_getAccessHistory(pageNo) {

    if (pageNo <= 0 || pageNo == null) pageNo = 1;
	
    let sido = $('#sido').val();
    let sigungu = $('#sigungu').val();
	let breakerId = $('#breakerId').val();
	let userNm = $('#userNm').val();
	let purposeEntryCode = $("#selectedElement").val();
	let dateStar = $("#dateStar").val();
	let dateEnd = $("#dateEnd").val();
	
	
    if (sido == undefined) sido = "";
    if (sigungu == undefined) sigungu = "";
    if (dateEnd != "") {
        
        if (dateEnd < dateStar) {
    
            alert("마감일이 시작일보다 빠릅니다. 기간을 다시 설정해주세요.");
            return;
        }
    }
	
	let queryString = "pageIndex=" + pageNo + 
					  "&sido=" + sido + 
					  "&sigungu=" + sigungu + 
					  "&breakerId=" + breakerId + 
					  "&userNm=" + userNm + 
					  "&purposeEntryCode=" + purposeEntryCode + 
					  "&dateStar=" + dateStar + 
					  "&dateEnd=" + dateEnd;
	
	fetch("/admin/imdoAccessHistory/getAccessHistory.do?" + queryString)
	.then(response => {
		if (response.ok) return response.json();
		else throw new Error("네트워크 에러");
	})
	.then(data => {
		
		let entryYimdoVos = data.entryYimdoVos;
		let paginationInfo = data.paginationInfo;
		let prev = (paginationInfo.firstPageNoOnPageList == 1 ? 1 : (paginationInfo.currentPageNo - paginationInfo.pageSize < 1 ? 1 : paginationInfo.currentPageNo - paginationInfo.pageSize));
		let next = (paginationInfo.lastPageNoOnPageList == paginationInfo.lastPageNo ? paginationInfo.lastPageNo : (paginationInfo.currentPageNo + paginationInfo.pageSize > paginationInfo.lastPageNo ? paginationInfo.lastPageNo : paginationInfo.currentPageNo + paginationInfo.pageSize));
		
		$('#accessHistoryListTotalCount').html("<span class='data_num'>총 <strong>" + paginationInfo.totalRecordCount + "</strong>건의 게시물이 있습니다. <em>(" + paginationInfo.totalPageCount + "페이지)</em></span>");
		
		$('#accessHistoryList').empty();
		entryYimdoVos.forEach(entryYimdoVo => {

			$('#accessHistoryList').append('<tr><td>' + entryYimdoVo.yimdoName + '</td><td>' + entryYimdoVo.breakerId + '</td><td>' + entryYimdoVo.userNm + '</td><td>' + entryYimdoVo.purposeEntryName + '</td><td>' + entryYimdoVo.expectedEntryDate + '</td><td>' + entryYimdoVo.entryDate + '</td><td>' + entryYimdoVo.expectedExitDate + '</td><td>' + entryYimdoVo.exitDate + '</td></tr>');
		})
		
		$("#accessHistoryPaging").empty();
		
		$("#accessHistoryPaging").append('<button type=\"button\" class=\"btn_page_first\" title=\"맨앞 페이지\" onclick=\"fetch_getAccessHistory(1); return false;\">맨 처음</button>');
		$("#accessHistoryPaging").append('<button type=\"button\" class=\"btn_page_prev\" title=\"이전 페이지\" onclick=\"fetch_getAccessHistory(' + prev + '); return false;\">이전</button>');

		for (let i = paginationInfo.firstPageNoOnPageList; i <= paginationInfo.lastPageNoOnPageList; i++) {
			
		  if (i == paginationInfo.currentPageNo) {
			  
			 $("#accessHistoryPaging").append('<a class="ml2 mr2 current" href=\"#\" title=\"현재 ' + i + '페이지\" >' + i + '</a>');
			 
		  } else {
			  
			 $("#accessHistoryPaging").append('<a class="ml2 mr2" href=\"#\" title=\"' + i + '페이지\" onclick=\"fetch_getAccessHistory(' + i + '); return false;\">' + i + '</a>');
		  }
		}
		
		$("#accessHistoryPaging").append('<button type=\"button\" class=\"btn_page_next\" title=\"다음 페이지\" onclick=\"fetch_getAccessHistory(' + next + '); return false;\">다음</button>');
		$("#accessHistoryPaging").append('<button type=\"button\" class=\"btn_page_last\" title=\"맨 마지막\" onclick=\"fetch_getAccessHistory(' + paginationInfo.lastPageNo + '); return false;\">맨 마지막</button>');
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
                    <li><span><strong>임도출입이력</strong></span></li>
                    </ol>
                    </div>
                    <h2 class="titlesb">임도출입이력</h2>
                </div>
                <!-- E : sub_head -->              


                <!-- S : content_area -->    
				<div class="content_area mt15">
                    <!-- S : sarchTxpa_box -->    
                    <div class="sarchTxpa_box">
                        <div id="accessHistoryListTotalCount" class="pacount">
                        </div>
                        <!--
                        <div class="srcR_box ">
                            <input  type="text" class="form wp65 " title="입력하세요">
                            <a href="#none" class="btn_list_src"><span>조회</span></a>
                        </div>
                        -->
                    </div>
                    <!-- E : sarchTxpa_box -->    
                     <!-- S : table_typA -->
                     <div class="table_typA">
                        <table class="contTbl txtC" summary="이 표는 목록입니다.">
                            <colgroup>
                                <col style="width:15%">
                                <col style="width:35%">
                                <col style="width:15%">
                                <col style="width:35%">
                            </colgroup>
                            <tbody>
                                <tr>
                                    <th scope="col">임도선택</th>
                                    <td>
                                        <div class="tb_city" data-label="시/도">
                                            <select id="sido" title="시/도" onchange="fetch_getSigungu(); return false;">
                                                <option value="">시/도 선택</option>
                                            </select>
                                        </div>
                                        <div class="tb_city" data-label="구/군">
                                            <select id="sigungu" title="시/군/군">
                                                <option value="">시/군/구 선택</option>
                                            </select>
                                        </div>
                                    </td>
                                    <th scope="col">차단기ID</th>
                                    <td class="tbtdL txtC"><input type="text" id="breakerId" class="form wp85"></td>
                                </tr>
                                <tr>
                                    <th scope="col">신청자</th>
                                    <td class="tbtdL txtL"><input type="text" id="userNm" class="form wp50 ml37"></td>
                                    <th scope="col">출입목적</th>
                                    <td>
                                        <input type="hidden" id="selectedElement" value="">
                                        <input type="text" id="selectedElementName" class="form wp70 " disabled>
                                        <button type="button" id="searchPurpose" class="btn_addsq " onclick="fetch_getElementList();"><span>조회</span></button>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="col">이용기간</th>
                                    <td colspan="3" class="txtL pl40">
                                        <div class="tb_date">
                                            <!-- 달력 마감일 데이터값 -->
                                            <input type="text" id="dateStar" class="tb_inp inp_date"
                                                placeholder="시작일" title="시작년월일을 입력합니다. YYYYMMDD형식으로 입력하세요.">
                                            <input type="text" id="dateEnd" class="tb_inp inp_date"
                                                placeholder="마감일" title="마감년월일을 입력합니다. YYYYMMDD형식으로 입력하세요.">
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <!-- E : table_typA -->
                    <!-- S : btn_center -->
                    <div class="mt20 mb30 btn_center">
                        <a href="#none" class="btn_list_src mr10" onclick="reset(); return false;"><span>초기화</span></a>
                        <a href="#none" class="btn_list_src" onclick="fetch_getAccessHistory(); return false;"><span>조회</span></a>
                    </div>
                    <!-- E : btn_center -->
                    <!-- S : table_typA -->    
                    <div class="table_typA">
                    <table class="contTbl txtC" summary="이 표는 목록입니다." >
                    <colgroup>
                        <col style="width:8%">
                        <col style="width:10%">
                        <col style="width:8%">
                        <col style="width:10%">
                        <col style="width:10%">
                        <col style="width:14%">
                        <col style="width:14%">
                        <col style="width:14%">
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="col">임도명</th>
                            <th scope="col">차단기ID</th>
                            <th scope="col">신청자</th>
                            <th scope="col">출입목적</th>
                            <th scope="col">입장예정시간</th>
                            <th scope="col">입장시간</th>
                            <th scope="col">퇴장예정시간</th>
                            <th scope="col">퇴장시간</th>
                        </tr>
                    </thead>
                    <tbody id="accessHistoryList">
                        <tr>
                            <td>광덕임도</td>
                            <td>100001</td>
                            <td>홍길동</td>
                            <td>유지관리</td>
                            <td>2023-01-05/15:20:10</td>
                            <td>2023-01-05/15:20:10</td>
                            <td>2023-01-05/15:20:10</td>
                            <td>2023-01-05/15:20:10</td>
                        </tr>
                    </tbody>
                    </table>
                    </div>			                
                    <!-- E : table_typA -->    

                    <!-- S : paging_wrap -->
                    <div id="accessHistoryPaging" class="paging_wrap mt15 mb15">
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
        <h2>임도출입이력상세</h2>
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

<div id="popup_2" class="popup_wrap" style="width: 600px; height: 770px;">
    <div class="title_pop">
        <h2>상태변경인자 목록</h2>
        <button type="button" class="btn-close " tabindex="0" title="창닫기"><span class="blind">창닫기</span></button>
    </div>
    <div class="pop_doc">
        <div class="content_area mt15">
        <!-- S : sarchTxpa_box -->    
            <div class="sarchTxpa_box">
                <div class="pacount">
                    <span id="element_totalCnt" class="data_num">총 <strong>?</strong>건의 게시물이 있습니다. <em>(?페이지)</em></span>
                </div>
                <div class="srcR_box ">
                    <input type="text" id="elementSearch" class="form wp65 " title="입력하세요" placeholder="상태변경인자">
                    <a class="btn_list_src" onclick="fetch_getElementList();" style="cursor: pointer;"><span>조회</span></a>
                    </div>
                </div>
            <!-- E : sarchTxpa_box -->    
            <!-- S : table_typA -->    
            <div class="table_typA">
                <input type="hidden" id="selectedId" value="">
                <table class="contTbl txtC" summary="이 표는 목록입니다." >
                    <colgroup>
                        <col width="85%"/>
                        <col width="15%"/>
                    </colgroup>
                    <thead>
                        <tr>
                            <th>상태변경인자</th>
                            <th>선택</th>
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
        <!-- small버튼 A테그 -->
        <div class="btn_popgroup_wrap mt10">
        <div class="btn_g_right">
            <!-- 
            <a href="/breakerManagementPage.do" class="btn_a_small_03"><span>창닫기</span></a>
             -->
        </div>
        </div>
        <!-- small버튼 A -->
    </div>
</div>
<!-- E : wrap -->

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
