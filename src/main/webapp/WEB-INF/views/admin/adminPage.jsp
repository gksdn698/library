<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<div class="container mt-5">
    <h1 class="text-center mb-4">관리자 페이지</h1>
    <div class="card-deck">
        <div class="card text-center">
            <div class="card-body">
                <h5 class="card-title">모든 회원 목록</h5>
                <p class="card-text">회원의 정보를 조회하고 관리할 수 있습니다.</p>
                <a href="${ctxPath}/admin/members" class="btn btn-primary">회원 목록 보기</a>
            </div>
        </div>
        <div class="card text-center">
            <div class="card-body">
                <h5 class="card-title">모든 대출 목록</h5>
                <p class="card-text">모든 회원의 도서 대출 내역을 확인할 수 있습니다.</p>
                <a href="${ctxPath}/admin/rents" class="btn btn-primary">대출 목록 보기</a>
            </div>
        </div>
        <div class="card text-center">
            <div class="card-body">
                <h5 class="card-title">모든 글 목록</h5>
                <p class="card-text">회원들이 작성한 모든 게시글을 조회하고 관리할 수 있습니다.</p>
                <a href="${ctxPath}/board/list" class="btn btn-primary">글 목록 보기</a>
            </div>
        </div>
    </div>
</div>
