<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>

<div class="container">
    <h2>모든 대출 목록</h2>
    
    <form action="${ctxPath}/admin/rents" method="get" class="form-inline mb-3">
        <input type="text" name="memberId" placeholder="회원 아이디" class="form-control mr-2"/>
        <input type="date" name="startDate" class="form-control mr-2"/>
        <input type="date" name="endDate" class="form-control mr-2"/>
        <button type="submit" class="btn btn-primary">검색</button>
    </form>
    
    <table class="table">
        <thead>
            <tr>
                <th>대출 번호</th>
                <th>책 번호</th>
                <th>회원</th>
                <th>대출 날짜</th>
                <th>반납 날짜</th>
                <th>도서대출 삭제</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="rent" items="${rents}">
                <tr>
                   <td>${rent.rentId}</td>
                    <td>
                        <a class="move" href="${ctxPath}/book/get?bno=${rent.bookId}">${rent.bookId}</a>
                    </td>
                    <td>${rent.memberId}</td>
                    <td><tf:formatDateTime value="${rent.rentDate}" pattern="yyyy년MM월dd일 HH시mm분"/></td>
                    <td><tf:formatDateTime value="${rent.returnDate}" pattern="yyyy년MM월dd일 HH시mm분"/></td>
                     <td>
                        <form action="${ctxPath}/admin/rents/delete" method="post" class="d-inline">
                            <input type="hidden" name="rentId" value="${rent.rentId}"/>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-danger btn-sm delete-button">삭제</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
