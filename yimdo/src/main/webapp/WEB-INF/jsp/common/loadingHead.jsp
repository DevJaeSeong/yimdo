<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
    /* 불투명한 배경과 모달 창 스타일링 */
    .overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5); /* 불투명도 설정 */
        display: flex;
        align-items: center;
        justify-content: center;
        display: none;
        z-index: 9999;
    }
    
    .modal {
        background-color: #fff;
        padding: 20px;
        border-radius: 5px;
        text-align: center;
    }

    /* 아이콘 애니메이션 스타일링 */
    .spinner {
        width: 40px;
        height: 40px;
        border: 4px solid #f3f3f3;
        border-top: 4px solid #3498db;
        border-radius: 50%;
        animation: spin 2s linear infinite;
        margin: 0 auto;
    }

    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }
</style>

<script>
    function showLoadingOverlay() {

        $('#loadingOverlay').css('display', 'flex');
    }
    
    function hideLoadingOverlay() {

        $('#loadingOverlay').css('display', 'none');
    }
</script>
