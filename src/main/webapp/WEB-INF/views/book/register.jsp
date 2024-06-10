<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<div class="container">
	<div class="row">
		<div class="col-12">
			<h1 class="page-header">도서등록</h1>
		</div>
	</div>
	
	<div class="row">
		<div class="col-12">
			<div class="card">
				<div class="card-body">
					<form action="${ctxPath}/book/register" method="post" class="registerForm">
						<div class="form-group">
							<label>제목</label>
							<input class="form-control" name="title"/>
						</div>
						<div class="form-group">
							<label>내용</label>
							<textarea class="form-control" rows="10" name="content"></textarea>
						</div>
						<div class="form-group">
							<label>저자</label>
							<input class="form-control" name="writer"/>
						</div>
						<div class="form-group">
							<label>출판사</label>
							<input class="form-control" name="publisher"/>
						</div>
						<div class="form-group">
							<label>장르</label>
							<input class="form-control" name="category"/>
						</div>
						<div class="form-group">
						    <label>대출 가능 여부</label><br>
						    <div class="form-check form-check-inline">
						        <input class="form-check-input" type="radio" name="availability" id="available" value="available">
						        <label class="form-check-label" for="available">대출 가능</label>
						    </div>
						    <div class="form-check form-check-inline">
						        <input class="form-check-input" type="radio" name="availability" id="unavailable" value="unavailable">
						        <label class="form-check-label" for="unavailable">대출 불가능</label>
						    </div>
						</div>
						<button type="button" class="btn btn-outline-primary register">작성</button>
						<button type="button" class="btn btn-outline-info list">목록</button>	
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>				
					</form>
				</div>
			</div>
		</div>
	</div>
	
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
					<div class="uploadResultDiv"> <!-- 파일업로드 결과 보여주기  -->
						<ul class="list-group"></ul>
					</div>
				</div> <!-- panel-body -->
			</div> <!-- panel end -->
		</div> <!-- col end -->
	</div><!-- row end -->
</div>

<input type="hidden" name="pageNum" value="${param.pageNum }" >
<input type="hidden" name="amount" value="${param.amount }" >
<input type="hidden" name="type" value="${param.type }" >
<input type="hidden" name="keyword" value="${param.keyword }" >

<script src="${ctxPath}/resources/js/book/register.js"></script>

<%@ include file="../includes/footer.jsp" %>