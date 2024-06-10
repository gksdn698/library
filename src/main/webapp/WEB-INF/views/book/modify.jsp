<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<div class="container">
	<div class="row">
		<div class="col-12">
			<h1 class="page-header">수정페이지</h1>
		</div>
	</div>
	
	<div class="row">
		<div class="col-12">
			<div class="card">
				<div class="card-body">
					<form action="${ctxPath}/book/modify" method="post" class="modifyForm">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						<div class="form-group">
							<label>번호</label>	
							<input class="form-control" name="bno" value="${book.bno}" readonly="readonly"/>
						</div>
						<div class="form-group">
							<label>제목</label>
							<input class="form-control" name="title" value="${book.title}" />
						</div>
						<div class="form-group">
							<label>내용</label>
							<textarea class="form-control" rows="10" name="content">${book.content}</textarea>
						</div>
						<div class="form-group">
							<label>저자</label>
							<input class="form-control" name="writer" value="${book.writer }"/>
						</div>
						<div class="form-group">
							<label>출판사</label>
							<input class="form-control" name="publisher" value="${book.publisher}"/>
						</div>
						<div class="form-group">
							<label>장르</label>
							<input class="form-control" name="category" value="${book.category }"/>
						</div>
						<div class="form-group">
						    <label>대출 가능 여부</label>
						    <select class="form-control" name="available">
						        <option value="true" ${available ? 'selected' : ''}>대출 가능</option>
						        <option value="false" ${!available ? 'selected' : ''}>대출 불가능</option>
						    </select>
						</div>
						<button type="button" data-oper='modify' class="btn btn-light">수정</button>
						<button type="button" data-oper='remove' class="btn btn-danger">삭제</button>
						<button type="button" data-oper='list' class="btn btn-info">목록</button>
					</form>						
				</div>
			</div>
		</div>
	</div>
	
	<!-- 파일 첨부 -->
	<div class="row my-5">
		<div class="col-lg-12">
			<div class="card">
				<div class="card-header">
					<h4>파일 첨부</h4>
				</div>
				<div class="card-body">
					<div class="uploadDiv">
						<input type="file" name="uploadFile" multiple="multiple">
					</div>
					<div class="uploadResultDiv mt-3"> <!-- 파일업로드 결과 보여주기  -->
						<ul class="list-group"></ul>
					</div>
				</div> <!-- card-body -->
			</div> <!-- card end -->
		</div> <!-- col end -->
	</div><!-- row end -->
</div>

<!-- 원본 이미지 -->
<div class="modal fade" id="showImage">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
	        <div class="modal-header">
	            <h4 class="modal-title">원본 이미지 보기</h4>
	            <button type="button" class="close" data-dismiss="modal">&times;</button>
	        </div>
	        <div class="modal-body"></div>
        </div>
    </div>
</div>

<%@ include file="../includes/footer.jsp" %>

<script>
let formObj = $('.modifyForm');
let type = '${bookCriteria.type}'
let keyword = '${bookCriteria.keyword}'
	
let addCriteria = function(){
	formObj.append($('<input/>',{type : 'hidden', name : 'pageNum', value : '${bookCriteria.pageNum}'}))
		   .append($('<input/>',{type : 'hidden', name : 'amount', value : '${bookCriteria.amount}'}))
	if(type&&keyword){
		formObj.append($('<input/>',{type : 'hidden', name : 'type', value : '${bookCriteria.type}'}))
			.append($('<input/>',{type : 'hidden', name : 'keyword', value : '${bookCriteria.keyword}'}))
	}
}	
</script>
<script src="${ctxPath}/resources/js/book/modify.js"></script>