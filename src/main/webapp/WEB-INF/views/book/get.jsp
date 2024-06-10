<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<div class="container mt-5">
    <div class="row mb-4">
        <div class="col-12">
            <h1 class="page-header">도서 상세 페이지</h1>
        </div>
    </div>
    
    <!-- 도서 정보와 첨부 파일을 포함한 테이블 -->
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <table class="table table-bordered">
                        <tbody>
                            <!-- 도서 정보 -->
                            <tr>
                                <th scope="row" style="width: 200px;">번호</th>
                                <td>${book.bno}</td>
                            </tr>
                            <tr>
                                <th scope="row">제목</th>
                                <td>${book.title}</td>
                            </tr>
                            <tr>
                                <th scope="row">저자</th>
                                <td>${book.writer}</td>
                            </tr>
                            <tr>
                                <th scope="row">출판사</th>
                                <td>${book.publisher}</td>
                            </tr>
                            <tr>
                                <th scope="row">장르</th>
                                <td>${book.category}</td>
                            </tr>
                            <tr>
                                <th scope="row">내용</th>
                                <td>${book.content}</td>
                            </tr>
                            <!-- 첨부 파일 -->
                            <tr>
                                <th scope="row">첨부 파일</th>
                                <td>
                                    <ul class="list-unstyled">
                                        <c:forEach items="${book.attachList}" var="attach">
                                            <li>
                                                <a href="${ctxPath}/files/display?fileName=${attach.uploadPath}/${attach.uuid}_${attach.fileName}">
                                                    ${attach.fileName}
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 첨부 파일 업로드 결과 -->
    <div class="row my-5">
        <div class="col-lg-12">
            <div class="card">
                <div class="card-body">
                    <div class="uploadResultDiv mt-3">
                        <ul class="list-group"></ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 버튼들 -->
    <div class="getBtn">
        <div class="col-12 text-center">
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <button data-oper='modify' class="btn btn-light modify">수정페이지</button>
            </sec:authorize>
            <sec:authorize access="isAuthenticated() and principal.username!= #book.writer">
                <a class="btn btn-outline-danger like">추천</a>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <input type="hidden" id="memberId" value="${pageContext.request.userPrincipal.name}">
                <form id="rentForm" class="d-inline">
                    <input type="hidden" id="bookId" value="${book.bno}">
                    <button class="btn btn-outline-info rent" type="button">대출하기</button>
                </form>
            </sec:authorize>
            <button data-oper='list' class="btn btn-info list">목록으로</button>
        </div>
    </div>
</div>

<form id="hiddenForm" method="get">
    <input type="hidden" name="bno" id="bno" value="${book.bno }">    
</form>

<!-- 원본 이미지 모달창 -->
<div class="modal fade" id="showImage">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">책 자세히 보기</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body"></div>
        </div>
    </div>
</div>

<%@ include file="../includes/footer.jsp" %>

<script src="${ctxPath}/resources/js/reply/ReplyService.js"></script>
<script src="${ctxPath}/resources/js/reply/reply.js"></script>
<script src="${ctxPath}/resources/js/book/get.js"></script>

<script>
$(function(){
    let form = $('#hiddenForm');
    $('.getBtn button').click(function(){
        let operation = $(this).data('oper');
        let type = '${bookCriteria.type}';
        let keyword = '${bookCriteria.keyword}';
        
        form.empty();
        form.append($('<input/>', { type: 'hidden', name: 'bno', value: '${book.bno}' }));
        form.append($('<input/>', { type: 'hidden', name: 'pageNum', value: '${bookCriteria.pageNum}' }));
        form.append($('<input/>', { type: 'hidden', name: 'amount', value: '${bookCriteria.amount}' }));
        
        if (type && keyword) {
            form.append($('<input/>', { type: 'hidden', name: 'type', value: type }));
            form.append($('<input/>', { type: 'hidden', name: 'keyword', value: keyword }));
        }
        
        if (operation == 'list') {
            form.attr('action', '${ctxPath}/book/list');
            form.find('input[name="bno"]').remove();
        } else if (operation == 'modify') {
            form.attr('action', '${ctxPath}/book/modify');
        }
        form.submit();
    });
});
</script>
