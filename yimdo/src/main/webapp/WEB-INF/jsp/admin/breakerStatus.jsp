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
    $(document).ready(function () {

        // 버튼 클릭 시 색 변경
        $('table tr').mouseover(function () {
            $(this).css("backgroundColor", "#faf8f1");
        });

        $('table tr').mouseout(function () {
            $(this).css("backgroundColor", "#fff");
        });
    });

    // 레이어팝업
    ;
    (function ($) {

        // DOM Ready
        $(function () {
            // Binding a click event
            // From jQuery v.1.7.0 use .on() instead of .bind()
            $('#popbtn1').bind('click', function (e) {
            	console.log("yimdoDetailView clicked");
                // Prevents the default action to be triggered. 
                e.preventDefault();
                // Triggering bPopup when click event is fired
                $('#popup_1').bPopup();
            });

            $('#elementList_btn').bind('click', function (e) {
                // Prevents the default action to be triggered. 
                e.preventDefault();
                // Triggering bPopup when click event is fired
                $('#popup_2').bPopup();
            });

            $('#popbtn3').bind('click', function (e) {
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
	
	$('ul.gnb_menu li:nth-child(1) a').removeClass('bul').addClass('bul bulon');
	
	fetch_getSido();
	fetch_getYimdoList();
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

function fetch_getYimdoList(pageNo) {
	
    $("#breakerSearchInput").val("");
    fetch_getBreakerList();

	if (pageNo <= 0 || pageNo == null) pageNo = 1;
	
	let sido = $('#sido').val();
	if (sido == undefined) sido = "";
	let sigungu = $('#sigungu').val();
	if (sigungu == undefined) sigungu = "";
	let searchKeyword = $('#yimdoSearchInput').val();
	
	let queryString = "pageIndex=" + pageNo + 
					  "&searchKeyword=" + searchKeyword +
					  "&sido=" + sido +
					  "&sigungu=" + sigungu;
	
	fetch("/common/getYimdoList.do?" + queryString)
	.then(response => {
		if (response.ok) return response.json();
		else throw new Error("네트워크 에러");
	})
	.then(data => {
		
		let yimdoInfoVos = data.yimdoInfoVos;
		let paginationInfo = data.paginationInfo;
		let prev = (paginationInfo.firstPageNoOnPageList == 1 ? 1 : (paginationInfo.currentPageNo - paginationInfo.pageSize < 1 ? 1 : paginationInfo.currentPageNo - paginationInfo.pageSize));
		let next = (paginationInfo.lastPageNoOnPageList == paginationInfo.lastPageNo ? paginationInfo.lastPageNo : (paginationInfo.currentPageNo + paginationInfo.pageSize > paginationInfo.lastPageNo ? paginationInfo.lastPageNo : paginationInfo.currentPageNo + paginationInfo.pageSize));
		
		$('#yimdoTotalCount').html("<span class='data_num'>총 <strong>" + paginationInfo.totalRecordCount + "</strong>건의 게시물이 있습니다. <em>(" + paginationInfo.totalPageCount + "페이지)</em></span>");
		
		$('#yimdoList').empty();
		yimdoInfoVos.forEach(yimdoInfoVo => {
			
			$('#yimdoList').append('<tr><td><a style="cursor: pointer;" onclick="set_selectedYimdo(' + yimdoInfoVo.yimdoCode + '); return false;">' + yimdoInfoVo.yimdoName + '</a></td><td>' + yimdoInfoVo.yimdoAddress + '</td> <td>' + yimdoInfoVo.yimdoDistance + '</td><td>' + yimdoInfoVo.yimdoSidoName + '</td><td>' + yimdoInfoVo.yimdoGunguName + '</td><td>' + yimdoInfoVo.yimdoDetail + '</td></tr>');
		})
		
		$("#yimdoListPaging").empty();
		
		$("#yimdoListPaging").append('<button type=\"button\" class=\"btn_page_first\" title=\"맨앞 페이지\" onclick=\"fetch_getYimdoList(1);\">맨 처음</button>');
		$("#yimdoListPaging").append('<button type=\"button\" class=\"btn_page_prev\" title=\"이전 페이지\" onclick=\"fetch_getYimdoList(' + prev + ');\">이전</button>');

		for (let i = paginationInfo.firstPageNoOnPageList; i <= paginationInfo.lastPageNoOnPageList; i++) {
			
		  if (i == paginationInfo.currentPageNo) {
			  
			 $("#yimdoListPaging").append('<a class="ml2 mr2 current" href=\"#\" title=\"현재 ' + i + '페이지\" >' + i + '</a>');
			 
		  } else {
			  
			 $("#yimdoListPaging").append('<a class="ml2 mr2" href=\"#\" title=\"' + i + '페이지\" onclick=\"fetch_getYimdoList(' + i + ');\">' + i + '</a>');
		  }
		}
		
		$("#yimdoListPaging").append('<button type=\"button\" class=\"btn_page_next\" title=\"다음 페이지\" onclick=\"fetch_getYimdoList(' + next + ');\">다음</button>');
		$("#yimdoListPaging").append('<button type=\"button\" class=\"btn_page_last\" title=\"맨 마지막\" onclick=\"fetch_getYimdoList(' + paginationInfo.lastPageNo + ');\">맨 마지막</button>');
	})
	.catch(error => {
		
		console.error("fetch에러: ", error);
	})
	
}

function set_selectedYimdo(yimdoCode) {
	
	$('#selectedYimdo').val(yimdoCode);
    $('#breakerSearchOption').val('');
    $('#breakerSearchInput').val('');
	fetch_getBreakerList();
}

function searchBreakerList() {

    let searchCondition = $('#breakerSearchOption').val();
    if (searchCondition == "" || searchCondition == null) {

        alert('검색 항목을 선택해주세요');
        return;

    } else {

        fetch_getBreakerList();
    }
}

function fetch_getBreakerList(pageNo) {

	fetch_getBreakerListEachStatusCount();
	
	if (pageNo <= 0 || pageNo == null) pageNo = 1;
    let sido = $('#sido').val(); if (sido == undefined) sido = "";
    let sigungu = $('#sigungu').val(); if (sigungu == undefined) sigungu = "";

	let yimdoCode = $('#selectedYimdo').val();
    let searchCondition = $('#breakerSearchOption').val();
	let searchKeyword = $('#breakerSearchInput').val();
	let queryString = "pageIndex=" + pageNo + 
					  "&yimdoCode=" + yimdoCode +
					  "&sido=" + sido +
					  "&sigungu=" + sigungu +
					  "&searchCondition=" + searchCondition +
					  "&searchKeyword=" + searchKeyword;
	
	fetch("/common/getBreakerList.do?" + queryString)
	.then(response => {
		if (response.ok) return response.json();
		else throw new Error("네트워크 에러");
	})
	.then(data => {
		
		let breakerInfoVos = data.breakerInfoVos;
		let paginationInfo = data.paginationInfo;
		let prev = (paginationInfo.firstPageNoOnPageList == 1 ? 1 : (paginationInfo.currentPageNo - paginationInfo.pageSize < 1 ? 1 : paginationInfo.currentPageNo - paginationInfo.pageSize));
		let next = (paginationInfo.lastPageNoOnPageList == paginationInfo.lastPageNo ? paginationInfo.lastPageNo : (paginationInfo.currentPageNo + paginationInfo.pageSize > paginationInfo.lastPageNo ? paginationInfo.lastPageNo : paginationInfo.currentPageNo + paginationInfo.pageSize));
		
		$('#breakerTotalCount').html("<span class='data_num'>총 <strong>" + paginationInfo.totalRecordCount + "</strong>건의 게시물이 있습니다. <em>(" + paginationInfo.totalPageCount + "페이지)</em></span>");
		
		$('#breakerList').empty();
		breakerInfoVos.forEach(breakerInfoVo => {
			
			let breakerStatusDeco;
            let breakerPolicyDeco;
			
			switch (breakerInfoVo.breakerPolicyCode) {
			
			  case "1001":
				  breakerPolicyDeco = "status_value01";
			    break;
			    
			  case "1002":
				  breakerPolicyDeco = "status_value05";
			    break;
			  
			  case "2001":
				  breakerPolicyDeco = "status_value02";
			    break;
			    
			  case "2002":
				  breakerPolicyDeco = "status_value04";
			    break;
			  
			  case "3001":
				  breakerPolicyDeco = "status_value03";
			    break;
			  
			  default:
				  breakerPolicyDeco = "status_value03";
			    break;
			};

			switch (breakerInfoVo.breakerStatusCode) {

			  case "01":
				breakerStatusDeco = "status_value01";
			    break;
			    
			  case "02":
				  breakerStatusDeco = "status_value05";
			    break;
			  
			  case "03":
				  breakerStatusDeco = "status_value02";
			    break;
			    
			  case "04":
				  breakerStatusDeco = "status_value04";
			    break;
			  
			  case "05":
				  breakerStatusDeco = "status_value03";
			    break;
			  
			  default:
				  breakerStatusDeco = "status_value03";
			    break;
			}
			
            if (breakerPolicyDeco == breakerStatusDeco) {

                $('#breakerList').append('<tr><td><ul class="checkboL"><li><div class="checkboX"><input class="breakerCheckBox" type="checkbox" value="' + breakerInfoVo.breakerId + '" id="' + breakerInfoVo.breakerId + '" name="' + breakerInfoVo.breakerId + '"><label for="' + breakerInfoVo.breakerId + '"></label></div></li></ul></td><td>' + breakerInfoVo.breakerId + '</td><td>' + breakerInfoVo.yimdoName + '</td><td><span class="' + breakerPolicyDeco + '">' + breakerInfoVo.breakerPolicyName + '</td><td><span class="' + breakerStatusDeco + '">' + breakerInfoVo.breakerStatusName + '</span></td><td>' + breakerInfoVo.modifier + '</td><td>' + breakerInfoVo.modifyDate + '</td><td>' + breakerInfoVo.elementName + '</span></td><td></td></tr>');
            
            } else {

                $('#breakerList').append('<tr><td><ul class="checkboL"><li><div class="checkboX"><input class="breakerCheckBox" type="checkbox" value="' + breakerInfoVo.breakerId + '" id="' + breakerInfoVo.breakerId + '" name="' + breakerInfoVo.breakerId + '"><label for="' + breakerInfoVo.breakerId + '"></label></div></li></ul></td><td>' + breakerInfoVo.breakerId + '</td><td>' + breakerInfoVo.yimdoName + '</td><td><span class="' + breakerPolicyDeco + '">' + breakerInfoVo.breakerPolicyName + '</td><td><span class="' + breakerStatusDeco + '">' + breakerInfoVo.breakerStatusName + '</span></td><td>' + breakerInfoVo.modifier + '</td><td>' + breakerInfoVo.modifyDate + '</td><td>' + breakerInfoVo.elementName + '</span></td><td>차단기 정책과 상태가 일치하지 않습니다.</td></tr>');
            }

		})
		
		$("#breakerListPaging").empty();
		
		$("#breakerListPaging").append('<button type=\"button\" class=\"btn_page_first\" title=\"맨앞 페이지\" onclick=\"fetch_getBreakerList(1); return false;\">맨 처음</button>');
		$("#breakerListPaging").append('<button type=\"button\" class=\"btn_page_prev\" title=\"이전 페이지\" onclick=\"fetch_getBreakerList(' + prev + '); return false;\">이전</button>');

		for (let i = paginationInfo.firstPageNoOnPageList; i <= paginationInfo.lastPageNoOnPageList; i++) {
			
		  if (i == paginationInfo.currentPageNo) {
			  
			 $("#breakerListPaging").append('<a class="ml2 mr2 current" href=\"#\" title=\"현재 ' + i + '페이지\" >' + i + '</a>');
			 
		  } else {
			  
			 $("#breakerListPaging").append('<a class="ml2 mr2" href=\"#\" title=\"' + i + '페이지\" onclick=\"fetch_getBreakerList(' + i + '); return false;\">' + i + '</a>');
		  }
		}
		
		$("#breakerListPaging").append('<button type=\"button\" class=\"btn_page_next\" title=\"다음 페이지\" onclick=\"fetch_getBreakerList(' + next + '); return false;\">다음</button>');
		$("#breakerListPaging").append('<button type=\"button\" class=\"btn_page_last\" title=\"맨 마지막\" onclick=\"fetch_getBreakerList(' + paginationInfo.lastPageNo + '); return false;\">맨 마지막</button>');
		
	})
	.catch(error => {
		
		console.error("fetch에러: ", error);
	})
}

function fetch_getBreakerListEachStatusCount() {
	
    let sido = $('#sido').val();
    if (sido == undefined) sido = "";
    let sigungu = $('#sigungu').val();
    if (sigungu == undefined) sigungu = "";
	let yimdoCode = $('#selectedYimdo').val();
	let searchKeyword = $('#breakerSearchInput').val()
	let queryString = "yimdoCode=" + yimdoCode +
                      "&sido=" + sido +
                      "&sigungu=" + sigungu +
					  "&searchKeyword=" + searchKeyword;
	
	fetch("/admin/breakerStatus/getBreakerListEachStatusCount.do?" + queryString)
	.then(response => {
		if (response.ok) return response.json();
		else throw new Error("네트워크 에러");
	})
	.then(statusCounts => {

		if (statusCounts == null) {
			
			breakerReset();
			return;
		}
        
        $("#breakerCount_01").text(statusCounts["01"] + "건");
        $("#breakerCount_02").text(statusCounts["02"] + "건");
        $("#breakerCount_03").text(statusCounts["03"] + "건");
        $("#breakerCount_04").text(statusCounts["04"] + "건");
        $("#breakerCount_05").text(statusCounts["05"] + "건");
	})
	.catch(error => {
		
		breakerReset();
		console.error("fetch에러: ", error);
	})
}

function breakerReset() {
	
	$('#breakerTotalCount').html("<span class='data_num'>총 <strong> 0</strong>건의 게시물이 있습니다. <em>(1페이지)</em></span>");
	$("#breakerCount_01").text("0건");
	$("#breakerCount_02").text("0건");
	$("#breakerCount_03").text("0건");
	$("#breakerCount_04").text("0건");
	$("#breakerCount_05").text("0건");
	$('#breakerList').empty();
	$("#breakerListPaging").empty();
}

function checkAll() {
	
	if ($("#allCheckBox").prop('checked')) {
		
	  $('.breakerCheckBox').prop('checked', true);
	  
	} else {
		
	  $('.breakerCheckBox').prop('checked', false);
	}
}

function fetch_getElementList(pageNo) {
	
	if (pageNo <= 0 || pageNo == null) pageNo = 1;
	
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
			
			$("#pop_elementList").append('<tr><td style="text-align: center;">' + modifyElementVo.elementName + '</td><td class="ac"><input type="button" class="btn_addsq" onclick="selectElement(\'' + modifyElementVo.elementCode + '\', \'' + modifyElementVo.elementName + '\'); return false;" style="width: 65px; cursor: pointer;" value="선택"></td></tr>');
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

function fetch_updateBreakerStatus() {
	
	let selectedBreakers = [];
	let selectedBoxes = $(".breakerCheckBox").filter(':checked');
	if (selectedBoxes.length <= 0) {
		
		alert("변경할 차단기를 선택해주세요.");
		return;
	}
	selectedBoxes.each(function() {
		
		selectedBreakers.push($(this).attr("id"));
	})
	
	let selectedElement = $("#selectedElement").val();
	if (selectedElement == "" || selectedElement == null) {
		
		alert("상태변경인자를 선택해주세요.");
		return;
	}
	
	let modifier = "관리자";
	let modifyDetail = $("#modifyDetail").val();
	let selectedPolicy = $(".policyRadio").filter(':checked:first').val();
	let systemControl = 'n';

	if (selectedPolicy == "1002") {
		
		systemControl = 'y'
		modifyDetail = "관리자에 의한 강제 정책 해제.";
	}
	
	let msg = {
		"selectedBreakers": selectedBreakers,
		"selectedElement": selectedElement,
		"selectedPolicy": selectedPolicy,
		"modifyDetail": modifyDetail,
        "modifier": modifier,
		"systemControl": systemControl
	}

	fetch("/admin/breakerStatus/updateBreakerStatus.do", {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			"${_csrf.headerName}": "${_csrf.token}"
		},
		body: JSON.stringify(msg)
	})
	.then(response => {
		if (response.ok) return response.json();
		else throw new Error("네트워크 에러");
	})
	.then(data => {
		
		let breakerListCurrentPage = $("#breakerListCurrentPage").data("breakerlistcurrentpage");
		fetch_getBreakerList(breakerListCurrentPage);

        alert("적용되었습니다.");
        console.log(data);
	})
	.catch(error => {
		
		console.error("fetch에러: ", error);
	})
}
</script>
</head>

<body class="admin_company">

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
                                <li><span><strong>차단기상태현황</strong></span></li>
                            </ol>
                        </div>
                        <h2 class="titlesb">차단기상태현황</h2>
                    </div>
                    <!-- E : sub_head -->

                    <!-- S : section_01 -->
                    <div class="condition mt30">
                        <ul>
                            <li class="c_red">강제차단<p id="breakerCount_04" class="bold_01">0건</p>
                            </li>
                            <li class="c_orange">강제개방<p id="breakerCount_03" class="bold_01">0건</p>
                            </li>
                            <li class="c_green">정상개방<p id="breakerCount_01" class="bold_01">0건</p>
                            </li>
                            <li class="c_purple">정상차단<p id="breakerCount_02" class="bold_01">0건</p>
                            </li>
                            <li class="c_gray">고장<p id="breakerCount_05" class="bold_01">0건</p>
                            </li>
                        </ul>
                    </div>
                    <!-- E : section_01 -->
                    <!-- S : table_typA_02 -->
                    <div class="table_typA_02 mt30 mb30">
                         <!-- S : sarchTxpa_box -->
                         <div class="sarchTxpa_box">
                            <div id="yimdoTotalCount" class="pacount">
                                <span class="data_num">총 <strong> 2145</strong>건의 게시물이 있습니다. <em>(9페이지)</em></span>
                            </div>
                            <div class="city_box" data-label="시/도">
                                <select id="sido" title="시/도" class="wp65" onchange="fetch_getSigungu();">
                                	<option value="">시/도 선택</option>
                                </select>
                            </div>
                            <div class="city_box_02" data-label="구/군">
                                <select id="sigungu" title="구/군" class="wp65">
                                	<option value="">시/군/구 선택</option>
                                </select>
                            </div>
                            <div class="srcR_box ">
                                <input type="text" id="yimdoSearchInput" class="form wp65 " title="입력하세요" placeholder="임도명">
                                <a href="#none" class="btn_list_src" onclick="fetch_getYimdoList(); return false;"><span>조회</span></a>
                            </div>
                        </div>
                        <!-- E : sarchTxpa_box -->
                        <table class="contTbl txtC" summary="이 표는 목록입니다.">
                            <colgroup>
                                <col style="width:10%">
                                <col style="width:10%">
                                <col style="width:10%">
                                <col style="width:10%">
                                <col style="width:10%">
                                <col style="width:10%">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th scope="col">임도명</th>
                                    <th scope="col">임도 주소</th>
                                    <th scope="col">임도 길이</th>
                                    <th scope="col">시/도</th>
                                    <th scope="col">군/구</th>
                                    <th scope="col">임도상세설명</th>
                                </tr>
                            </thead>
                            <tbody id="yimdoList">
                            </tbody>
                        </table>
                         <!-- S : paging_wrap -->
                         <div id="yimdoListPaging" class="paging_wrap mt30 mb15">
                        </div>
                        <!-- E : paging_wrap -->
                    </div>
                    <!-- E : table_typA_02 -->

                    <!-- S : content_area -->
                    <div class="content_area">
                        <!-- S : sarchTxpa_box -->
                        <div class="sarchTxpa_box">
                            <div id="breakerTotalCount" class="pacount">
                            </div>
                            <div class="city_box_02" data-label="시/도">
                                <select id="breakerSearchOption" title="차단기 검색항목" class="wp65">
                                	<option value="">검색항목 선택</option>
                                	<option value="breakerId">차단기ID</option>
                                	<option value="breakerStatus">차단기상태</option>
                                	<option value="modifier">변경자</option>
                                	<option value="modifyDate">변경일</option>
                                	<option value="elementName">상태변경인자</option>
                                </select>
                            </div>
                            <div class="srcR_box ">
                            	<input type="hidden" id="selectedYimdo" value="noSelected">
                                <input type="text" id="breakerSearchInput" class="form wp65 " title="입력하세요">
                                <a href="#none" class="btn_list_src" onclick="fetch_getBreakerList(); return false;"><span>조회</span></a>
                            </div>
                        </div>
                        <!-- E : sarchTxpa_box -->
                        <!-- S : table_typA -->
                        <div class="table_typA">
                            <table class="contTbl txtC" summary="이 표는 목록입니다.">
                                <colgroup>
                                    <col style="width:8%">
                                    <col style="width:10%">
                                    <col style="width:10%">
                                    <col style="width:10%">
                                    <col style="width:10%">
                                    <col style="width:10%">
                                    <col style="width:12%">
                                    <col style="width:10%">
                                    <col style="width:20%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">
                                            <ul class="checkboL">
                                                <li>
                                                    <div class="checkboX">
                                                        <input type="checkbox" value="" id="allCheckBox" name="allCheckBox" onclick="checkAll();">
                                                        <label for="allCheckBox">전체선택</label>
                                                    </div>
                                                </li>
                                            </ul>
                                        </th>
                                        <th scope="col">차단기ID</th>
                                        <th scope="col">임도명</th>
                                        <th scope="col">차단기정책</th>
                                        <th scope="col">차단기상태</th>
                                        <th scope="col">변경자</th>
                                        <th scope="col">변경일</th>
                                        <th scope="col">상태변경인자</th>
                                        <th scope="col">비고</th>
                                    </tr>
                                </thead>
                                <tbody id="breakerList">
                                </tbody>
                            </table>
                        </div>
                        <!-- E : table_typA -->

                        <!-- S : paging_wrap -->
                        <div id="breakerListPaging" class="paging_wrap mt15 mb15">
                        </div>
                        <!-- E : paging_wrap -->

                        <!-- S : table_typA -->
                        <div class="table_typA mt30">
                            <table class="contTbl txtC" summary="이 표는 목록입니다.">
                                <colgroup>
                                    <col style="width:10%">
                                    <col style="width:10%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">상태변경인자</th>
                                        <th scope="col">상세사유</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>
                                            <input type="hidden" id="selectedElement" value="">
                                            <input type="text" id="selectedElementName" class="form wp55 " disabled>
                                            <button type="button" id="elementList_btn" class="btn_addsq " onclick="fetch_getElementList();"><span>조회</span></button>
                                        </td>
                                        <td class="tbtdL"><input type="text" id="modifyDetail" class="form wp100"></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <!-- E : table_typA -->

                        <!-- S : table_typA -->
                        <div class="table_typA mt30">
                            <table class="contTbl txtC" summary="이 표는 목록입니다.">
                                <colgroup>
                                    <col style="width:100%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">차단기정책</th>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td class="pt20 pb20">
                                            <ul class="radio">
                                                <li>
                                                    <input type="radio" value="2002" name="date_area" id="date_area6" class="policyRadio" checked="checked">
                                                    <label for="date_area6"><span>출입차단</span></label>
                                                </li>
                                                <li>
                                                    <input type="radio" value="2001" name="date_area" id="date_area7" class="policyRadio">
                                                    <label for="date_area7"><span>출입개방</span></label>
                                                </li>
                                                <li>
                                                    <input type="radio" value="1002" name="date_area" id="date_area8" class="policyRadio">
                                                    <label for="date_area8"><span>정책해제</span></label>
                                                </li>
                                            </ul>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <!-- E : table_typA -->

                        <!-- S : btn_center -->
                        <div class="mt30 mb30 btn_center">
                            <a href="#none" class="btn_list_src mr10" onclick="fetch_updateBreakerStatus(); return false;"><span>적용</span></a>
                            <a href="#none" class="btn_list_src"><span>취소</span></a>
                        </div>
                        <!-- E : btn_center -->

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
            <button type="button" class="btn-close " tabindex="0" title="창닫기"><span
                    class="blind">창닫기</span></button>
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

    <!-- S : 레이어팝업 -->
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
    <!-- E : 레이어팝업 -->

    <!-- S : footer_wrap -->
    <jsp:include page="./common/adminPageFooter.jsp" />
    <!-- E : footer_wrap -->

    <script>
        // 달력
        $(function () {
            $('input[id^="dateStar"]').datepicker();
            $('input[id^="dateEnd"]').datepicker();
            $('input[id^="datepickers"]').datepicker();
            $.datepicker.setDefaults({
                dateFormat: 'yy-mm-dd',
                prevText: '이전 달',
                nextText: '다음 달',
                monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
                monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월',
                    '12월'
                ],
                dayNames: ['일', '월', '화', '수', '목', '금', '토'],
                dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
                dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
                showMonthAfterYear: true,
                changeYear: true, //콤보박스에서 년 선택 가능  
                changeMonth: true, //콤보박스에서 월 선택 가능  
                //yearSuffix: '년',
            });

        });
    </script>
</body>

</html>
