<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<div>
    <div><!--  2nd box -->
		<div>
			<div >
				<h4>내가 작성한 댓글</h4>
				
			</div>
			<div>
				<table class="table">
					<tr>
						<th>댓글 작성한 게시물번호</th>
						<th>댓글내용</th>
						<th>작성자</th>
						<th>등록일</th>
					</tr>
					
 					<c:forEach items="${CommentInfo.userReplyList}" var="reply">
			            <tr>
			                <td><a href="${ctxPath }/board/get?bno=${reply.bno}">${reply.bno}</a></td>
			                <td>${reply.reply }</td>
			                <td>${reply.replyer}</td>
			                <td><tf:formatDateTime value="${reply.replyDate}" pattern="yyyy년MM월dd일 HH시mm분"/></td>
			            </tr>
					</c:forEach>
					
					<c:if test="${empty CommentInfo.userReplyList}">
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
