<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<style>
    .table thead th {
        background-color: #f8f9fa;
        color: #343a40;
    }
    .table tbody tr:hover {
        background-color: #f1f1f1;
    }
    .delete-button {
        background-color: #dc3545;
        color: white;
    }
</style>

<div class="container mt-5">
    <h1 class="mb-4">회원 목록</h1>
    <table class="table table-bordered table-hover">
        <thead class="thead-light">
            <tr>
                <th>아이디</th>
                <th>이름</th>
                <th>전화번호</th>
                <th>이메일</th>
                <th>성별</th>
                <th>회원 권한</th>
                <th>계정 생성일</th>
                <th>회원 계정 삭제</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="member" items="${members}">
                <tr>
                    <td>${member.memberId}</td>
                    <td>${member.memberName}</td>
                    <td>${member.memberPhone}</td>
                    <td>${member.email}</td>
                    <td>${member.memberGender}</td>
                    <td>
                        <c:forEach var="auth" items="${member.authList}">
                            <span class="badge badge-secondary">${auth.auth}</span><br/>
                        </c:forEach>
                    </td>
                    <td><tf:formatDateTime value="${member.regDate}" pattern="yyyy년MM월dd일 HH시mm분"/></td>
                    <td>
                        <form action="${ctxPath}/admin/members/delete" method="post" class="d-inline">
                            <input type="hidden" name="memberId" value="${member.memberId}"/>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-danger btn-sm delete-button">삭제</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
