<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="/css/imdo/member/css/font.css">
<link rel="stylesheet" href="/css/imdo/member/css/default.css">
<link rel="stylesheet" href="/css/imdo/member/css/common.css">
<link rel="stylesheet" href="/css/imdo/member/css/layout.css">
<script src="/js/imdo/member/js/jquery-3.3.1.min.js"></script>

<jsp:include page="../common/title.jsp" />
<jsp:include page="../common/loadingHead.jsp" />

<script type="text/javascript">

$(function() {
    
    initEventListener();
})

function initEventListener() {

    $("#loginBtn").on("click", () => { doLogin(); });
    $("#createAccountBtn").on("click", () => { location.href = "/createAccountPage.do"; });
    
    $("#loginId, #loginPasswd").keypress(function(event) {
    	
	    if (event.which === 13) {
	    	
	    	doLogin();
	    }
    });
}

function doLogin() {
	
	let id = $('input[name="loginId"]').val();
	let pw = $('input[name="loginPasswd"]').val();
	
	if (!id) {
		
		alert("아이디를 입력해주세요.");
		return;
	}
	
	if (!pw) {
		
		alert("비밀번호를 입력해주세요.");
		return;
	}

	let data = {
		"userId": id,
		"password": pw
	}

    showLoadingOverlay();

	fetch("/general/loginConfirm.do", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
            "${_csrf.headerName}": "${_csrf.token}"
		},
		body: JSON.stringify(data)
	})
	.then(response => {
		if (response.ok) { return response.json(); }
		else 			 { throw new Error(response.status); }
	})
	.then(data => {
		
		switch (data.result) {
		
			case "success":
				window.location.href="/general/home.do";
				break;
				
			case "fail":
				alert("입력하신 아이디 또는 비밀번호가 일치하지 않습니다.");
				break;
		}
	})
	.catch(error => {
		
		let errorMessage = error.message;
		console.error("fetch에러: ", errorMessage);
		
		if (errorMessage == 400) { 
			
			alert("아이디는 영문 또는 숫자만 입력 가능합니다.");
			return; 
		}
		
		alert("에러가 발생했습니다. 잠시후 다시 시도해주세요.");
		location.reload();
	})
    .finally(() => {
        
        hideLoadingOverlay();
    })
}
</script>

</head>
<body>
    <div class="wrap">
        <section class="log_sec">
            <article>
                <!-- 로그인화면 왼쪽 텍스트 부분 시작 -->
                <div class="log_inner01">
                    <p class="main_title mb35"><span>스마트</span> <span>임도</span> <span>차단기</span></p>
                    <p class="sub_title mb25">스마트 임도 차단기 시스템에<br/>오신것을 환영합니다.</p>
                    <p class="title_txt mb25">산림환경, 기후변화 등 변화에 대한 정략적 분석을 도출하여<br/>임도를 통한 산림의 이용 및 활용성을 확인해보세요.</p>
                    <p class="log_sub_txt">사용을 위해 로그인 해주세요.</p>
                </div>
                <!-- 로그인화면 왼쪽 텍스트 부분 끝 -->

                <!-- 로그인화면 오른쪽 아이디, 비밀번호 입력부분 시작 -->
                <div class="log_inner02">
                    <div class="log_box">
                        <p class="log_wel mb55">환영합니다</p>
                        <ul class="info_box">
                            <li>
                                <p class="info_box_title mb15">아이디</p>
                                <label for="loginId" class="txt_hidden">아이디 입력</label>
                                <input type="text" placeholder="" id="loginId" name="loginId" value="">
                            </li>
                            <li class="mt15">
                                <p class="info_box_title mb15">비밀번호</p>
                                <label for="loginPasswd" class="txt_hidden">비밀번호 입력</label>
                                <input type="password" placeholder="" id="loginPasswd" name="loginPasswd" value="">
                            </li>
                        </ul>
                        <button id="loginBtn" class="log_btn mt50" title="로그인">로그인</button>
                        <button id="createAccountBtn" class="log_btn mt50" title="회원가입" >회원가입</button>
                    </div>
                    <p class="m_log_sub_txt">사용을 위해 로그인 해주세요.</p>
                </div>
                <!-- 로그인화면 오른쪽 아이디, 비밀번호 입력부분 끝 -->
            </article>
        </section>
    </div>
    <jsp:include page="../common/loadingBody.jsp" />
</body>
</html>