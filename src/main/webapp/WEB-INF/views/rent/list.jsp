<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<div class="container">
    <h2>대출 목록</h2>
    <table class="table">
        <thead>
            <tr>
                <th>대출 번호</th>
                <th>책 번호</th>
                <th>회원</th>
                <th>대출 날짜</th>
                <th>반납 날짜</th>
                <th>반납</th>
                <th>연장</th>
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
                        <button class="btn btn-danger returnBook" data-rent-id="${rent.rentId}">반납</button>
                    </td>
                    <td>
                         <button class="btn btn-primary extendBook" data-rent-id="${rent.rentId}" data-extension-count="${rent.extensionCount}">연장</button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
<%@ include file="../includes/footer.jsp" %>

<script>
$(function() {
    const csrfToken = $('#csrfToken').val();
    
    // 반납 버튼 클릭 이벤트 처리
    $('.returnBook').click(function() {
        let rentId = $(this).data('rent-id');
        if (confirm('이 도서를 반납하시겠습니까?')) {
            $.ajax({
                type: 'post',
                url: '${ctxPath}/rent/return',
                data: { rentId: rentId},
                success: function() {
                    alert("도서 반납이 완료되었습니다.");
                    location.reload();
                },
                error: function() {
                    alert('반납 요청에 실패했습니다. 다시 시도해주세요.');
                }
            });
        }
    });
    
    // 연장 버튼 클릭 이벤트 처리
    $('.extendBook').click(function(e) {
        e.preventDefault();
        let rentId = $(this).data('rent-id');
        let extensionCount = $(this).data('extension-count');
        if (confirm('이 도서를 연장하시겠습니까?')) {
            $.ajax({
                type: 'GET',
                url: '${ctxPath}/rent/extendCheck',
                data: { rentId: rentId},
                success: function(response) {
                    if (response === 'duplicate') {
                        alert("이미 연장된 책입니다. 연장은 한 번만 가능합니다.");
                    } else {
                        let now = new Date().toISOString();
                        $.ajax({
                            type: 'POST',
                            url: '${ctxPath}/rent/extend',
                            data: { rentId: rentId, newReturnDate: now, extensionCount: extensionCount},
                            success: function() {
                                alert('대출 연장이 완료되었습니다.');
                                location.reload();
                            },
                            error: function() {
                                alert('연장 요청에 실패했습니다. 다시 시도해주세요.');
                            }
                        });
                    }
                },
                error: function() {
                    alert('연장 요청 상태 확인에 실패했습니다. 다시 시도해주세요.');
                }
            });
        }
    });
});
</script>
