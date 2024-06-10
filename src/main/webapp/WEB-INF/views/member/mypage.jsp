<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>

<div class="container">
	<ul class="list-group list-group-horizontal justify-content-center mt-3">
		<li class="list-group-item">
			<a href="${ctxPath}/mypage">회원정보변경</a>
		</li>
		<li class="list-group-item">
			<a href="${ctxPath}/member/article?memberId=${memberId}">내가쓴글</a>
		</li>
		<li class="list-group-item">
			<a href="${ctxPath}/member/comment?memberId=${memberId}">내가 쓴 댓글</a>
		</li>
		<li class="list-group-item">
			<a href="${ctxPath}/rent/list">대출중 도서 조회</a>
		</li>
	</ul>
	
	<div class="d-flex justify-content-center">
		<div class="w-50 my-5">
			<div class="jumbotron">
				<h3>회원정보 변경</h3>
			</div>
			<div class="userImage d-flex justify-content-center my-3">
				<label for="imageUpload">
					<img class="rounded-circle" src="${ctxPath}/resources/images/userImage.png"  style="width: 120px">
				</label>
				<input type="file" name="userImage" id="imageUpload" style="display : none;width: 100%;height: 100%">
			</div>
			<form:form class="validation-form" modelAttribute="memberDTO" method="post" action="${ctxPath}/mypage">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<div class="form-group">
					<%-- <input type="hidden" id="memberId" name="memberId" value="${memberDTO.memberId}"/> --%>
					<form:hidden path="memberId"/>
				</div>
				<div class="form-group">
				<label for="memberName">이름</label>
					<form:input path="memberName" class="form-control from-control-lg"  value="${memberDTO.memberName}"/>
					<form:errors  path="memberName" style="color:red;"/>
				</div>
				<div class="form-group">
				<label for="email">이메일</label>
					<form:input path="email" class="form-control from-control-lg"  value="${memberDTO.email}"/>
					<form:errors  path="email" style="color:red;"/>
				</div>
				<div class="form-group">
				<label for="memberPhone">전화번호</label>
					<form:input path="memberPhone" class="form-control from-control-lg"  value="${memberDTO.memberPhone}"/>
					<form:errors  path="memberPhone" style="color:red;"/>
				</div>
				<div class="form-group">
			    <label for="memberGender">성별</label>
				      <form:select path="memberGender" class="form-select" required="required">
			            <form:option value="M">남성</form:option>
			            <form:option value="F">여성</form:option>
			        </form:select>
				</div>
				<div class="form-group">
					<button type="submit" class="btn btn-outline-primary btn-lg form-control modifyBtn">정보수정</button>
				</div>
				<div class="form-group">
					<button type="button" class="btn btn-outline-info btn-lg form-control changePwdForm">비밀번호변경</button>
				</div>
			</form:form>
		</div>
	</div>
</div> <!-- container end -->

<!-- 비밀번호 변경창 -->
<div class="modal" id="changePwdModal">
	<div class="modal-dialog">
		<div class="modal-content">
		
			<!-- Modal header -->
			<div class="modal-header">
				<h4 class="modal-title">비밀번호 변경</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form>
					<input type="text" autocomplete="username" style="display:none"/>
					<div class="form-group">
						현재 비밀번호 : <input type="password" class="form-control currentPwd" autocomplete="new-password">
					</div>
					<div class="form-group">
						변경할 비밀번호 : <input type="password" class="form-control newPwd" autocomplete="new-password">
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn btn-outline-info showPwd">비밀번호보기</button>
				<button class="btn btn-outline-danger changePwd">비밀번호변경</button>
			</div>
		</div>
	</div>
</div>

<script>
window.addEventListener('load', () => {
    const forms = document.getElementsByClassName('validation-form');

    Array.prototype.filter.call(forms, (form) => {
      form.addEventListener('submit', function (event) {
        if (form.checkValidity() === false) {
          event.preventDefault();
          event.stopPropagation();
        }
      }, false);
    });
  }, false);
  
let result = "${result}";
$(function(){
	console.log(result);
	if(result=='modify'){
		alert('회원정보를 수정함')
	}
	
	// 비밀번호 변경 모달
	$('.changePwdForm').click(function(){
		$('#changePwdModal').find('input').val('');
		$('#changePwdModal').modal();
	});
	
	// 비밀번호 변경 처리
	$('.changePwd').click(function(){
		$.ajax({
			type: 'post',
			url : '${ctxPath}/mypage/changePwd',
			data : {
				memberId : $('[name="memberId"]').val(),
				currentPwd : $('.currentPwd').val(),
				newPwd : $('.newPwd').val()
			},
			success : function(result){
				alert(result);
				$('#changePwdModal').modal('hide');
			},
			error: function(xhr,status,er){
				alert(xhr.responseText)
			}
		})
	})
	
	 $('.modifyBtn').click(function(){
	        // memberId 가져오기
	        let memberId = $('#memberId').val();
	        // AJAX 요청 보내기
	        $.ajax({
	            type: 'post',
	            url: '${ctxPath}/member/mypage',
	            data: { memberId: memberId },
	            success: function(response){
	                // 성공적으로 요청이 처리된 경우
	                console.log('회원 정보 수정 요청이 성공적으로 처리되었습니다.');
	                // 추가적인 작업 수행 가능
	            },
	            error: function(xhr, status, error){
	                // 요청 처리 중 에러가 발생한 경우
	                console.error('회원 정보 수정 요청 처리 중 에러가 발생하였습니다: ' + error);
	            }
	        });
	    });
})
</script>

<%@ include file="../includes/footer.jsp"%>