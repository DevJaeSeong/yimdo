<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="../common/title.jsp" />

<link rel="stylesheet" href="/css/imdo/member/css/font.css">
<link rel="stylesheet" href="/css/imdo/member/css/default.css">
<link rel="stylesheet" href="/css/imdo/member/css/common.css">
<link rel="stylesheet" href="/css/imdo/member/css/layout.css">

<script src="/js/imdo/member/js/jquery-3.3.1.min.js"></script>

<script>
function fetch_breakerRequest() {
    
    let msg = {
        "breakerId": "5001",
        "userNm": "${userDetailVo.userNm}"
    }

    fetch("/user/clientRequestNormalOpen.do", {
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
            
            alert("개방요청으로 정상적으로 실행되었습니다.");
            
        } else {
            
            alert("개방요청을 실패하였습니다. 잠시후 다시 시도해주세요.");
        }
    })
    .catch(error => {
        
        console.error("fetch에러: ", error);
        alert("문제가 발생하였습니다. 관리자에게 문의해주세요.");
    })
}
</script>
</head>
<body>
    <div class="wrap">
        <div class="yim_direct">
            <div class="yim_direct_inner">
                <div class="yim_direct_con">
                    <p class="yim_direct_txt01 mb10">환영합니다.</p>
                    <p class="yim_direct_txt02 mb20"><span class="customer_name">${userDetailVo.userNm}</span>님</p>
                    <p class="yim_direct_txt03 mb20">스마트 임도 차단기 시스템을 이용하시기 위하여<br/>임도 출입 신청을 작성해주세요.</p>
                    <div class="respon_box">
                        <a href="/member/accessYimdo.do" class="direct_btn mr15" title="임도 출입 신청 바로가기"><p>임도 출입 신청<br/><span class="direct_arrow">바로가기</span></p></a>
                        <a href="#none" class="direct_btn gr_c_orange mr15" title="임도 개방 버튼" onclick="fetch_breakerRequest(); return false;"><p>임도 개방<br/><span class="direct_arrow">버튼</span></p></a>
                    </div>
                    <!-- 로그아웃버튼 시작 -->
                    <div class="logout_box">
                        <button class="logout_btn" title="로그아웃" onclick="location.href='/general/logout.do'"><span>로그아웃</span></button>
                    </div>
                    <!-- 로그아웃버튼 끝 -->
                </div>
            </div>
        </div>
    </div>
</body>
</html>