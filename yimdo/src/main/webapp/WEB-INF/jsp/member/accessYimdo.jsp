<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <jsp:include page="../common/title.jsp" />

    <link rel="stylesheet" href="/css/imdo/member/css/font.css">
    <link rel="stylesheet" href="/css/imdo/member/css/jquery-ui.css">
    <link rel="stylesheet" href="/css/imdo/member/css/default.css">
    <link rel="stylesheet" href="/css/imdo/member/css/reset.css">
    <link rel="stylesheet" href="/css/imdo/member/css/common.css">
    <link rel="stylesheet" href="/css/imdo/member/css/layout.css">

    <script src="/js/imdo/member/js/jquery-3.3.1.min.js"></script>
    <script src="/js/imdo/member/js/jquery-ui.js"></script>
    <script src="/js/imdo/member/js/common.js"></script>
    
<script type="text/javascript">
function formSubmit() {
	
	let userId = "${userDetailVo.userId}";
	
	let purposeEntry = $('select[name="purposeEntry"]').val();
	let forestRoadInfo = $('select[name="forestRoadInfo"]').val();
	
	let in_date = $('input[name="in_date"]').val();
	let in_hour = $('input[name="in_hour"]').val();
	let in_min = $('input[name="in_min"]').val();
	let in_dateTime = in_date + " " + in_hour + ":" + in_min;
	
	let out_date = $('input[name="out_date"]').val();
	let out_hour = $('input[name="out_hour"]').val();
	let out_min = $('input[name="out_min"]').val();
	let out_dateTime = out_date + " " + out_hour + ":" + out_min;
	
	let car_num = $('input[name="car_num"]').val();
	let entering_num = $('input[name="entering_num"]').val();
	
	let agreement = $('input[name="agreement_checkbox"]');
	
	// 필수 입력값 체크
	if (!purposeEntry || !forestRoadInfo || !in_date || !in_hour || !in_min || !out_date || !out_hour || !out_min || !car_num || !entering_num) {
		
	  alert("모든 필수 입력값을 입력해주세요.");
	  return;
	}

	if (!(in_hour >= 0 && in_hour <= 23)) {
		
		alert("입장예정시간(시) 을 다시 입력해주세요.");
		return;
	}
	
	if (!(in_min >= 0 && in_min <= 59)) {
		
		alert("입장예정시간(분) 을 다시 입력해주세요.");
		return;
	}
	
	if (!(out_hour >= 0 && out_hour <= 23)) {
		
		alert("퇴장예정시간(시) 을 다시 입력해주세요.");
		return;
	}
	
	if (!(out_min >= 0 && out_min <= 59)) {
		
		alert("퇴장예정시간(분) 을 다시 입력해주세요.");
		return;
	}
	
	// 입장 날짜/시간과 퇴장 날짜/시간이 올바른지 체크
	let inDateObj = new Date(in_date + " " + in_hour + ":" + in_min);
	let outDateObj = new Date(out_date + " " + out_hour + ":" + out_min);

	if (inDateObj >= outDateObj) {
		
	  alert("입장 시간이 퇴장 시간과 같거나 빠릅니다. 다시 입력해주세요.");
	  return;
	}
	
	if (!agreement.prop('checked')) {
		
		alert("개인정보 수집 및 이용에 동의해주세요.");
		return;
	}
	
	let data = {
		"userId": userId,
		"purposeEntryCode": purposeEntry,
		"yimdoCode": forestRoadInfo,
		"expectedEntryDate": in_dateTime,
		"expectedExitDate": out_dateTime,
		"carNum": car_num,
		"entryNum": entering_num
	}
	
	fetch("/user/accessYimdoConfirm.do", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
            "${_csrf.headerName}": "${_csrf.token}"
		},
		body: JSON.stringify(data)
	})
	.then(response => {
		if (response.ok) return response.json();
		else throw new Error("네트워크 에러");
	})
	.then(data => {
		
		if (data.result == "success") {
			
			alert("정상적으로 신청되었습니다.");
			location.href = "/home.do"
			
		} else {
			
			alert("신청에 실패하였습니다. 잠시후 다시 시도해주세요.");
		}
	})
	.catch(error => {
		
		console.error("fetch에러: ", error);
	})
}

function formClean() {
	
	$('select[name="purposeEntry"]').val("");
	$('select[name="forestRoadInfo"]').val("");
	
	$('input[name="in_date"]').val("");
	$('input[name="in_hour"]').val("");
	$('input[name="in_min"]').val("");
	
	$('input[name="out_date"]').val("");
	$('input[name="out_hour"]').val("");
	$('input[name="out_min"]').val("");
	
	$('input[name="car_num"]').val("");
	$('input[name="entering_num"]').val("");
}
</script>
    
</head>

<body>
    <div class="wrap">
        <div class="yim_form">
            <div class="yim_form_inner">
                <div class="yim_form_con">
                    <p class="yim_form_title mb35">임도 출입 신청서 작성</p>
                    <p class="yim_form_sub_title mb10">정보입력</p>
                    <!-- 회원정보  시작 -->
                    <div class="table_typA">
                        <table class="contTbl" summary="이 표는 목록입니다.">
                            <colgroup>
                                <col style="width:15%">
                                <col style="width:35%">
                                <col style="width:15%">
                                <col style="width:35%">
                            </colgroup>
                            <tbody>
                                <!-- DB불러오는 값 -->
                                <tr>
                                    <th scope="col">성명</th>
                                    <td data-label="성명"><input type="text" class="form wp90" value="${userDetailVo.userNm}" title="입력" disabled></td>
                                    <th scope="col">휴대전화</th>
                                    <td data-label="휴대전화"><input type="text" class="form wp90" value="${userDetailVo.mbtlNum}" title="입력" disabled></td>
                                </tr>
                                <tr>
                                    <th scope="col">이메일</th>
                                    <td data-label="이메일"><input type="text" class="form wp90" value="${userDetailVo.email}" title="입력" disabled></td>
                                    <th scope="col">소속기관</th>
                                    <td data-label="소속기관"><input type="text" class="form wp90" value="${userDetailVo.affiliation}" title="입력" disabled></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <!-- 회원정보 끝 -->
                    <!-- 정보입력테이블 시작 -->
                    <div class="table_typA">
                        <table class="contTbl" summary="이 표는 목록입니다.">
                            <colgroup>
                                <col style="width:15%">
                                <col style="width:35%">
                                <col style="width:15%">
                                <col style="width:35%">
                            </colgroup>
                            <tbody>
                                <tr>
                                    <th scope="col">출입목적</th>
                                    <td data-label="출입목적">
                                        <select name="purposeEntry" id="search_001" title="출입목적 선택" class="form wp90">
                                            <option value="">출입목적선택</option>
                                           	<c:forEach var="purposeEntryVo" items="${purposeEntryVos}">
                                                <option value="${purposeEntryVo.purposeEntryCode}">${purposeEntryVo.purposeEntryName}</option>
                                           	</c:forEach>
                                        </select>
                                    </td>
                                    <th scope="col">임도명</th>
                                    <td data-label="임도명">
                                        <select name="forestRoadInfo" id="search_001" title="임도명 선택" class="form wp90">
                                            <option value="">임도명선택</option>
											<c:forEach var="yimdoInfoVo" items="${yimdoInfoVos}">
												<option value="${yimdoInfoVo.yimdoCode}">${yimdoInfoVo.yimdoName}</option>
											</c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="col">입장예정시간</th>
                                    <td data-label="입장예정시간">
                                        <div class="respon_box">
                                            <div>
                                                <span class="sim">날짜 - </span>
                                                <input type="text" name="in_date" id="dateStar" class="inp_date wp60"
                                                    placeholder="시작일" title="시작년월일을 입력합니다. YYYYMMDD형식으로 입력하세요.">
                                            </div>
                                            <div>
                                                <span class="sim ml20">시간 - </span>
                                                <input type="number" name="in_hour" id="tx01" class="form wp10" title="입력">
                                                <span class="sim"> : </span>
                                                <input type="number" name="in_min" id="tx01" class="form wp10" title="입력">
                                            </div>
                                        </div>
                                    </td>
                                    <th scope="col">퇴장예정시간</th>
                                    <td data-label="퇴장예정시간">
                                        <div class="respon_box">
                                            <div>
                                                <span class="sim">날짜 - </span>
                                                <input type="text" name="out_date" id="dateEnd" class="inp_date wp60" placeholder="시작일" title="시작년월일을 입력합니다. YYYYMMDD형식으로 입력하세요.">
                                            </div>
                                            <div>
                                                <span class="sim ml20">시간 - </span>
                                                <input type="number" name="out_hour" id="tx01" class="form wp10" title="입력">
                                                <span class="sim"> : </span>
                                                <input type="number" name="out_min" id="tx01" class="form wp10" title="입력">
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="col">차량번호</th>
                                    <td data-label="차량번호"><input type="text" name="car_num" class="form wp90" title="입력"></td>
                                    <th scope="col">출입인원</th>
                                    <td data-label="출입인원"><input type="number" name="entering_num"  class="form wp90" title="입력"></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <!-- 정보입력끝 -->

                    <!-- 개인정보 수집동의 시작 -->
                    <div class="btn_box mt10 mb10">
                        <ul class="mb10">
                            <li>
                                <ul class="checkboL">
                                    <li>
                                        <div class="checkboX">
                                            <input type="checkbox" value="" id="01_01" name="agreement_checkbox">
                                            <label for="01_01" title="개인정보 수집 및 이용(필수)"><strong>개인정보 수집 및 이용(필수)</strong></label>
                                        </div>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                        <p class="ToS"><a href="#none" title="이용약관">이용약관</a></p>
                    </div>
                    <!-- 개인정보 수집동의 끝 -->
                    <!-- 저장,초기화버튼 시작 -->
                    <div class="btn_box mt10">
                        <button class="btn_01 wd90" onclick="formSubmit();">신청</button>
                        <button class="btn_02 wd90" onclick="formClean();">초기화</button>
                    </div>
                    <!-- 저장,초기화버튼 끝 -->

                    <!-- 로그아웃 버튼 시작 -->
                    <!-- <div class="logout_box">
                        <button class="logout_btn" title="로그아웃"><span>로그아웃</span></button>
                    </div> -->
                    <!-- 로그아웃 버튼 끝 -->
                </div>
            </div>
        </div>
    </div>
</body>

</html>