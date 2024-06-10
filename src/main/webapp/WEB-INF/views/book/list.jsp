<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<style>
    /* 대출 가능한 경우의 텍스트 색상 설정 */
    .available {
        color: green;
    }

    /* 대출 불가능한 경우의 텍스트 색상 설정 */
    .unavailable {
        color: red;
    }
</style>
<div class="container">
	<div class="row">
		<div class="col-12">
			<h1 class="page-header"></h1>
		</div>
	</div>
	
	<div class="row">
		<div class="col-12">
			<div class="card-columns">
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="book">
					    <div class="card">
					        <c:choose>
					            <c:when test="${not empty book.attachList}">
					                <c:set var="attach" value="${book.attachList[0]}" />
					                <c:if test="${attach.fileType}">
					                    <img src="${ctxPath}/files/display?fileName=${attach.uploadPath}/s_${attach.uuid}_${attach.fileName}"
					                         class="card-img-top" style="width: 100%; height: 238px;">
					                </c:if>
					                <c:if test="${empty attach.fileType}">
					                    <img src="${ctxPath}/resources/images/empty.png"
					                         class="card-img-top" style="width: 100%; height: 238px;">
					                </c:if>
					            </c:when>
					            <c:otherwise>
					                <img src="${ctxPath}/resources/images/empty.png"
					                     class="card-img-top" style="width: 100%; height: 238px;">
					            </c:otherwise>
					        </c:choose>
					        <div class="card-body">
					            <h5 class="card-title">${book.title}</h5>
					            <p class="card-text">저자: ${book.writer}</p>
					            <p class="card-text">출판사: ${book.publisher}</p>
					            <p class="card-text">장르: ${book.category}</p>
					            <p class="card-text">조회수: ${book.viewCnt}</p>
					            <p class="card-text">추천수: ${book.likeHit}</p>
					            <p class="card-text">대출 가능 여부: <span class="${book.available ? 'available' : 'unavailable'}">${book.available ? '대출 가능' : '대출 불가'}</span></p>
					            <a href="${ctxPath}/book/get?bno=${book.bno}" class="btn btn-primary">도서 상세보기</a>
					        </div>
					    </div>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</div>
	<form class="my-3" id="searchForm" action="${ctxPath}/book/list">
		<div class="d-inline-block">
			<select name="type" class="form-control">
				<option value="" ${p.bookCriteria.type == null ? 'selected' : '' }>------</option>
				<option value="T" ${p.bookCriteria.type eq 'T' ? 'selected' : '' }>제목</option>
				<option value="C" ${p.bookCriteria.type eq 'C' ? 'selected' : '' }>내용</option>
				<option value="W" ${p.bookCriteria.type eq 'W' ? 'selected' : '' }>작성자</option>
				<option value="TC" ${p.bookCriteria.type eq 'TC' ? 'selected' : '' }>제목+내용</option>
				<option value="TW" ${p.bookCriteria.type eq 'TW' ? 'selected' : '' }>제목+작성자</option>
				<option value="TCW" ${p.bookCriteria.type eq 'TCW' ? 'selected' : '' }>제목+내용+작성자</option>
			</select>
		</div>
		<div class="d-inline-block col-4">
			<input type="text" name="keyword" value="${p.bookCriteria.keyword}" class="form-control">
		</div>
		<div class="d-inline-block">
			<button class="btn btn-primary">검색</button>
		</div>
		<div class="d-inline-block">
			<a href="${ctxPath}/book/list" class="btn btn-outline-info">새로고침</a>
		</div>
		<input type="hidden" name="pageNum" value="${p.bookCriteria.pageNum}">
		<input type="hidden" name="amount" value="${p.bookCriteria.amount}">
	</form>
	
	<ul class="pagination justify-content-center">
		<c:if test="${p.prev }">
			<li class="page-item">
				<a class="page-link" href="${p.startPage-p.displayPageNum}">이전페이지</a>
			</li>
		</c:if>
		<c:forEach begin="${p.startPage}" end="${p.endPage }" var="pagelink">
			<li class="page-item ${ pagelink == p.bookCriteria.pageNum ? 'active':''}">
				<a href="${pagelink}" class="page-link ">${pagelink}</a>
			</li>
		</c:forEach>
		<c:if test="${p.next }">
			<li class="page-item">
				<a class="page-link" href="${p.endPage+1}">다음페이지</a>
			</li>
		</c:if>
	</ul>
	<form id="listForm" action="${ctxPath}/book/list">
		<input type="hidden" name="pageNum" value="${p.bookCriteria.pageNum}">
		<input type="hidden" name="amount" value="${p.bookCriteria.amount}">
	</form>
</div>
<%@ include file="../includes/footer.jsp" %>

<!-- Modal -->
<div class="modal fade" id="listModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">처리 결과</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <!-- Modal body -->
            <div class="modal-body">
                처리가 완료되었습니다.
            </div>
            <!-- Modal footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-light" data-dismiss="modal">변경</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<sec:authorize access="hasRole('ROLE_ADMIN')">
    <button id="regBtn" class="btn btn-xs btn-primary">도서 등록</button>
</sec:authorize>

<script>
$(function(){
	let attachList = "${book.attachList}";
	let result = "${result}"; // 처리 후 응답
	let searchForm = $('#searchForm'); // 검색 폼
	let listForm = $('#listForm'); // 페이지 폼
	
	let searchCondition = function(){
		if(searchForm.find('option:selected').val() && searchForm.find('[name="keyword"]')){ // 검색 조건이 있을 때
			listForm.append($('<input/>',{type : 'hidden', name : 'type', value : '${bookCriteria.type}'}))
					.append($('<input/>',{type : 'hidden', name : 'keyword', value : '${bookCriteria.keyword}'}))
		}		
	} 
	
	checkModal(result);
	
	function checkModal(result){
		if(result=='') return; // 값이 없으면 함수 종료
		let operation = "${operation}"
		if(operation=='register'){
			$('.modal-body').html(result+'번 게시글을 등록하였습니다.');
		} else if(operation=='modify'){
			$('.modal-body').html(result+'번 게시글을 수정하였습니다.');
		} else if(operation=='remove') {
			$('.modal-body').html(result+'번 게시글을 삭제하였습니다.');
		}
		$('#listModal').modal('show'); // 값이 있으면 모달창 열기
	}
	
	// 글쓰기 페이지로 이동
	$('#regBtn').click(function(){
	    // 도서 등록 페이지로 이동
	    window.location.href = '${ctxPath}/book/register';
	});
	
	// 페이지 이동 
	$('.pagination a').click(function(e){
		e.preventDefault();
		let pageNum = $(this).attr('href');
		listForm.find('input[name="pageNum"]').val(pageNum)
		searchCondition();
		listForm.submit();
	});	 
	
	// 조회 페이지로 이동 
	$('.move').click(function(e){
		e.preventDefault();
		let bnoValue = $(this).attr('href');
		searchCondition();	
		listForm.append($('<input/>',{type : 'hidden', name : 'bno', value : bnoValue}))
				.attr('action','${ctxPath}/book/get')
				.submit();
	});
	
	// 게시물 수 변경 
	$('.amount').change(function(){
		let amount = $(this).val();
		searchCondition();
		listForm.find('input[name="amount"]').val(amount)
		listForm.submit();		
	})
	
	// 검색 이벤트 처리 
	$('#searchForm button').click(function(e){
		e.preventDefault();
		if(!searchForm.find('option:selected').val()){
			alert('검색종류를 선택하세요');
			return; 
		}
		if(!searchForm.find('[name="keyword"]').val()){
			alert('키워드를 입력하세요');
			return; 
		}
		searchForm.find('[name="pageNum"]').val(1); 
		searchForm.submit();
	});
})
	
</script>
<script src="${ctxPath}/resources/js/book/list.js"></script>