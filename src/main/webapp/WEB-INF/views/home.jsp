<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="includes/header.jsp"%>

<!-- Bootstrap CSS 추가 (필요 시 제거) -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<style>
    /* 추가한 CSS */
    .carousel-item img {
        width: 100%;
        height: 80vh;
        object-fit: cover;
    }

    .carousel-caption {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        text-align: center;
    }

    .carousel-caption h1, .carousel-caption h3 {
        font-size: 36px;
        font-weight: normal; /* 폰트 굵기를 보통으로 조정 */
        color: white;
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.8);
    }

    .button-container {
        position: absolute;
        top: 75%; /* 버튼 그룹을 위로 올리기 위해 값을 조정합니다. */
        left: 50%;
        transform: translateX(-50%);
    }

    .action-button {
        display: inline-block;
        margin: 10px;
        padding: 10px 20px;
        font-size: 16px;
        font-weight: bold;
        color: white;
        background-color: #007bff;
        border: none;
        border-radius: 5px;
        text-decoration: none;
        transition: background-color 0.3s;
    }

    .action-button:hover {
        background-color: #0056b3;
    }

    /* 도서대출 부분의 구분선 제거 */
    .button-container .action-button:not(:last-child) {
        margin-right: 10px;
    }
    
    /* 슬라이드 넘기는 버튼의 구분선 제거 */
    .carousel-control-prev, .carousel-control-next {
        border: none;
    }
</style>

<div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
    <ol class="carousel-indicators">
        <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
    </ol>
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img src="${ctxPath}/resources/images/library1.jpg" class="d-block w-100" alt="Library Image 1">
            <div class="carousel-caption">
                <h1>책의로의 이끌림, 꿈을 향한 두드림</h1>
                <h3>OO도서관</h3>
            </div>
        </div>
        <div class="carousel-item">
            <img src="${ctxPath}/resources/images/library2.jpg" class="d-block w-100" alt="Library Image 2">
            <div class="carousel-caption">
                <h1>문화와 지식의 보고</h1>
                <h3>OO도서관</h3>
            </div>
        </div>
        <div class="carousel-item">
            <img src="${ctxPath}/resources/images/library3.jpg" class="d-block w-100" alt="Library Image 3">
            <div class="carousel-caption">
                <h1>열린 도서관, 행복한 독서 문화</h1>
                <h3>OO도서관</h3>
            </div>
        </div>
    </div>
    <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
    </a>
    <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
    </a>
</div>

<div class="button-container">
    <a href="${ctxPath}/board/list" class="action-button">열린공간</a>
    <a href="${ctxPath}/book/list" class="action-button">도서대출</a>
    <a href="${ctxPath}/join/step1" class="action-button">회원가입안내</a>
</div>

<!-- Bootstrap JS 추가 (필요 시 제거) -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<%@ include file="includes/footer.jsp"%>
