<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>


<div class="container">
	<div class="row my-5">
		<div class="col-2">
			<ul class="list-group">
				<li class="list-group-item">이용약관</li>
				<li class="list-group-item">이메일 인증</li>
				<li class="list-group-item active">회원가입작성</li>
			</ul>		
		</div>
		<div class="col-6 mx-auto">
			<form:form class="validation-form" method="post" modelAttribute="memberVO" >
				<h1 class="text-center py-3">회원가입</h1>
				<div class="form-group row">
					<div class="col-8">
						<form:input class="form-control" path="memberId" placeholder="아이디를 입력해주세요"/>
						<form:errors path="memberId" style="color:red;"/>
					</div>
					<div class="col-4">
						<button type="button" class="btn btn-outline-info form-control idCheck">ID중복확인</button>
					</div>
				</div>
				<div class="form-group">
					<form:input class="form-control"  path="memberName" placeholder="이름을 입력해주세요"/>
					<form:errors path="memberName" style="color:red;"/>
				</div>
				
				<div class="form-group">
					<form:input class="form-control" path="email" placeholder="이메일" readonly="true"/>
					<form:errors path="email" style="color:red;"/>
				</div>
				
				<div class="form-group">
					<form:password class="form-control"  path="memberPwd" placeholder="비밀번호를 입력해주세요"/>
					<form:errors path="memberPwd" style="color:red;"/>
				</div>
				
				<div class="form-group">
					<form:password class="form-control"  path="confirmPwd" placeholder="비밀번호를 확인해주세요"/>
					<form:errors path="confirmPwd" style="color:red;"/>
				</div>
				
				<div class="form-group">
					<form:input class="form-control" path="memberPhone" placeholder="000-0000-0000"/>
					<form:errors path="memberPhone" style="color:red;"/>
				</div>
				
				<div class="form-group">
					<select class="form-select" name="memberGender" required="required">
						<option value="M">남자</option>
						<option value="F">여자</option>
					</select>
				</div>
				
				<button class="btn btn-primary btn-lg btn-block" type="submit">가입완료</button>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			</form:form>
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
</script>

<script src="${ctxPath}/resources/js/member/join.js"></script>
