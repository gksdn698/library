<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<div>
    <div><!--  2nd box -->
		<div>
			<div >
				<h4>내가 작성한 글</h4>
				
			</div>
			<div>
				<table class="table">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>작성자</th>
						<th>등록일</th>
					</tr>
					
 					<c:forEach items="${info.userBoardList}" var="board">
			            <tr>
			                <td>${board.bno}</td>
			                <td><a href="${ctxPath }/board/get?bno=${board.bno}">${board.title}</a></td>
			                <td>${board.writer}</td>
			                <td><tf:formatDateTime value="${board.regDate}" pattern="yyyy년MM월dd일 HH시mm분"/></td>
			            </tr>
					</c:forEach>
					
					<c:if test="${empty info.userBoardList}">
						<tr>
							<td colspan="4">작성한 글이 없습니다.</td>
						</tr>
					</c:if>
				</table>
			</div>
		</div>
	</div>
</div>
<%@ include file="../includes/footer.jsp"%>
