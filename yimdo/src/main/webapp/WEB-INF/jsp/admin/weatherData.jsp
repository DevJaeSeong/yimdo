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
<style type="text/css">
:root {
	--defaultHeight: 500px;
}

.content_container {
	display: flex;
}

.chart_layer {
	width: calc(2 * var(--defaultHeight));
	height: var(--defaultHeight);
	padding: 10px;
}

.info_layer {
 	flex: 1;
	height: var(--defaultHeight);
	border: 1px solid #ccc;
	padding: 10px;
}

.pd10 { padding: 10px; }
.mg10 { margin: 10px; }
</style>

<script type="text/javascript" src="/js/imdo/admin/js/jquery.js"></script>
<script type="text/javascript" src="/js/imdo/admin/js/jquery-ui.js"></script>
<script type="text/javascript" src="/js/imdo/admin/js/jquery.bpopup.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="text/javascript">

// 차트 전역변수
let myChart;
let data;
let option;

$(function() {
	
	intiEvents();
	initChartOption();
	initChart();
	selectWeek('1');
})

function intiEvents() {
	
	$('ul.gnb_menu li:nth-child(6) a').removeClass('bul').addClass('bul bulon');
	
	$('input[name="chartType"]').change(function() {
		
		let chartType = $('input[name="chartType"]:checked').val();
		changeChartType(chartType);
	})
}


/* 차트 생성 */
function initChart() {
	
    let div = document.getElementById('myChart').getContext('2d');
    
    myChart = new Chart(div, {
        type: 'bar',
        data: [],
        options: option
    });
}

function initChartOption(chartType) {
	
	switch (chartType) {
	
		case 'pie':
			option = {
				y: { beginAtZero: true }
			};
			break;	
		
		default:
			option = {
				y: { beginAtZero: true , suggestedMin: 0 , suggestedMin: 100 }
			};
			break;
	}
}

function changeChartType(type) {
	
	if (myChart) myChart.destroy();
	
	initChartOption(type);
	
	let ctx = document.getElementById('myChart').getContext('2d');
    myChart = new Chart(ctx, {
        type: type,
        data: data,
        options: option
    });
}

function changeChartData(selectedWeek, weatherDataVos) {
	
	let pastDays = getPastDays(selectedWeek);
	let setLabels = ['온도', '습도', '풍향', '평균풍속', '최고풍속', '강수량', '자외선', '가시광선'];
	
    // 차트 데이터 정의
    data = {
    		
        labels: getLabels(pastDays)
        , datasets: getDatasets(pastDays, setLabels, weatherDataVos)
    };
    
    myChart.data = data;
    myChart.update();
    
    // 사용자 뷰 설정
    setView(data);
}

function setView(data) {
	
    data.datasets.forEach((dataObj) => {
    	
    	let data = dataObj.data;
    	
    	switch (dataObj.label) {
    	
    		case '온도':
    			changeText('temperature', data);
    			break;
    			
    	
    		case '습도':
    			changeText('humidity', data);
    			break;
    	}
    });
}

function changeText(elementId, data) {
	
	let cal = 0;
	data.forEach((value) => {
		
		cal += value;
	})
	
	cal = cal / data.length;
	
	$('#' + elementId).text(cal.toFixed(1));
}

function getData() {
	
	let dateSelect = $('#dateSelect').val();
	let count = '0';
	
	if (dateSelect == 1) count = 7;
	else 				 count = 4;
	
	let queryString = '';
	queryString += '?week=' + dateSelect;
	queryString += '&count=' + count;
	
	fetch('/admin/weatherData/getWeatherData.do' + queryString)
	.then(response => {
		if (response.ok) { return response.json(); }
		else 			 { throw new Error(response.status); }
	})
	.then(data => {
		
		let week = data.week;
		let weatherDataVos = data.weatherDataVos;
		
		changeChartData(week, weatherDataVos);
		//changeChartData($('#dateSelect').val());
	})
	.catch(error => {
		
		console.error("fetch에러: ", error);
		
		if (error.message == 400) { 
			
			alert("쿼리문 오류.");
			return; 
		}
		
		alert("에러가 발생했습니다. 잠시후 다시 시도해주세요.");
	})
}

/* 과거 시간 얻기 */
function getPastDays(weekAgo) {
	
	let currentDate = new Date();
	let options = { year: 'numeric', month: '2-digit', day: '2-digit' };
	let daysArr = [];
	let pastDays = [];
	
	switch (weekAgo) {
		
		case 1: 
			daysArr = [6, 5, 4, 3, 2, 1, 0];
			break;
		
		case 4:
			daysArr = [21, 14, 7, 0];
			break;
		
		case 12:
			daysArr = [61, 42, 21, 0];
			break;
		
		case 36:
			daysArr = [189, 163, 126, 0];
			break;
	}
	
	daysArr.forEach((pastDay) => {
		
		let pastDate = new Date(currentDate);
		pastDate.setDate(currentDate.getDate() - pastDay);
		
		let dateArr = [];
		dateArr[0] = pastDate.getUTCFullYear();
		dateArr[1] = pastDate.getMonth() + 1;
		dateArr[2] = pastDate.getDate();
		
		pastDays.push(dateArr);
	})
	
	return pastDays;
}

/* labels 세팅 */
function getLabels(pastDays) {
	
	let labels = [];
	
	pastDays.forEach((dateArr) => {
		
		let dateStr = dateArr[1] + '월 ' + dateArr[2] + '일';
		labels.push(dateStr);
	})
	
	return labels;
}

/* datasets 세팅 */
function getDatasets(pastDays, labels, weatherDataVos) {
	
	let pastDaysLength = pastDays.length;
	let labelsLength = labels.length;
	let weatherDataVosLength = weatherDataVos.length;
	
	let minRgb = 0;
	let maxRgb = 255;
	
	let dataObjArr = [];
	
	for (let i = 0; i < labelsLength; i++) {
		
		// data 설정
		let dataArr = [];
		for (let j = (weatherDataVosLength - 1); j >= 0; j--) {
			
			let weatherDataVo = weatherDataVos[j];
			
			/*
			for (k = 0; k < pastDaysLength; k++) {
				
				let weatherDataVoDate = new Date(weatherDataVo.regDate);
				let compareDate = new Date(pastDays[k][0], pastDays[k][1], pastDays[k][2]);
			}
			*/
			
			pushData(dataArr, labels[i], weatherDataVo);
		}
		
		let dataObj = {
				
			label: labels[i]
			, data: dataArr
			, backgroundColor: getBackgroundRgba(labels[i])
			, borderColor: getBackgroundRgba(labels[i])
			, borderWidth: 1
		};
		
		dataObjArr.push(dataObj);
	}
	
	return dataObjArr;
}

function pushData(dataArr, label, weatherDataVo) {
	
	switch (label) {
	
		case '온도':
			dataArr.push(parseFloat(weatherDataVo.tmp));
			break;
	
		case '습도':
			dataArr.push(parseFloat(weatherDataVo.hm));
			break;
	
		case '풍향':
			dataArr.push(parseFloat(weatherDataVo.dir));
			break;
	
		case '평균풍속':
			dataArr.push(parseFloat(weatherDataVo.wind));
			break;
	
		case '최고풍속':
			dataArr.push(parseFloat(weatherDataVo.gust));
			break;
	
		case '강수량':
			dataArr.push(parseFloat(weatherDataVo.rain));
			break;
	
		case '자외선':
			dataArr.push(parseFloat(weatherDataVo.uv));
			break;
	
		case '가시광선':
			dataArr.push(parseFloat(weatherDataVo.light));
			break;
	}
}

function getBackgroundRgba(label) {
	
	let rgba = '';
	
	switch (label) {
	
		case '온도':
			rgba = 'rgba(255, 0, 0, 0.5)';
			break;
	
		case '습도':
			rgba = 'rgba(0, 0, 255, 0.5)';
			break;
	
		case '풍향':
			rgba = 'rgba(255, 165, 0, 0.5)';
			break;
	
		case '평균풍속':
			rgba = 'rgba(0, 128, 0, 0.5)';
			break;
	
		case '최고풍속':
			rgba = 'rgba(128, 0, 128, 0.5)';
			break;
	
		case '강수량':
			rgba = 'rgba(30, 144, 255, 0.5)';
			break;
	
		case '자외선':
			rgba = 'rgba(255, 255, 0, 0.5)';
			break;
	
		case '가시광선':
			rgba = 'rgba(255, 192, 203, 0.5)';
			break;
	}
	
	return rgba;
}

function getBorderRgba(label) {
	
	let rgba = '';
	
	switch (label) {
	
		case '온도':
			rgba = 'rgba(255, 0, 0, 1.0)';
			break;
	
		case '습도':
			rgba = 'rgba(0, 0, 255, 1.0)';
			break;
	
		case '풍향':
			rgba = 'rgba(255, 165, 0, 1.0)';
			break;
	
		case '평균풍속':
			rgba = 'rgba(0, 128, 0, 1.0)';
			break;
	
		case '최고풍속':
			rgba = 'rgba(128, 0, 128, 1.0)';
			break;
	
		case '강수량':
			rgba = 'rgba(30, 144, 255, 1.0)';
			break;
	
		case '자외선':
			rgba = 'rgba(255, 255, 0, 1.0)';
			break;
	
		case '가시광선':
			rgba = 'rgba(255, 192, 203, 1.0)';
			break;
	}
	
	return rgba;
}

function selectWeek(selectedweek) {
	
	let dayHtml = '';
	
	switch (selectedweek) {
		
		case '1':
			dayHtml = '최근 7일';
			break;
		
		case '4':
			dayHtml = '최근 4주';
			break;
		
		case '12':
			dayHtml = '최근 12주';
			break;
		
		case '36':
			dayHtml = '최근 36주';
			break;
	}
	
	$('#day').html(dayHtml);
	getData();
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
                    <li><span><strong>기상관측기 기상정보</strong></span></li>
                    </ol>
                    </div>
                    <h2 class="titlesb">기상관측기 기상정보</h2>
                </div>
                <!-- E : sub_head -->   
            	
            	<div class="content_area mt15">
	            	<div class="content_container">
						<div class="chart_layer">
							<canvas id="myChart"></canvas>
						</div>
						<div class="info_layer">
							<div class="pd10">
								<h2>최근 <span id="day"></span>간 수집된 기상 정보</h2>
						        <p>데이터 출처: 5001 차단기</p>
						        <div>
							        <label for="dateSelect">날짜 선택:</label>
							        <select id="dateSelect" onchange="selectWeek(this.value);">
							            <option value="1">최근 7일</option>
							            <option value="4">최근 4주</option>
							            <option value="12">최근 12주</option>
							            <option value="36">최근 36주</option>
							        </select>
						        </div>
						        <div>
					        	    <div class="radio-buttons">
						        		<label>그래프 유형: </label>
								        <label>
								            <input type="radio" name="chartType" value="bar" checked>
								            막대
								        </label>
								        <label>
								            <input type="radio" name="chartType" value="line">
								            선
								        </label>
								        <label>
								            <input type="radio" name="chartType" value="pie">
								            원형
								        </label>
								        <label>
								            <input type="radio" name="chartType" value="radar">
								            분포도
								        </label>
								        <label>
								            <input type="radio" name="chartType" value="doughnut">
								            도넛
								        </label>
								        <label>
								            <input type="radio" name="chartType" value="polarArea">
								            폴라
								        </label>
								    </div>
						        </div>
							</div>
							<div class="pd10">
						        <p>평균 온도: <span id="temperature"></span>°C</p>
						        <p>평균 습도: <span id="humidity"></span>%</p>
							</div>
						</div>
	            	</div>
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
