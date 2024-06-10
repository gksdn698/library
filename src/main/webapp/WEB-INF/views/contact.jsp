<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
연락처 등록

<form method="post">
	<div>
		이름 : <input name="contactList[0].name" type="text">
		이메일 : <input name="contactList[0].email" type="text">
	</div>
	<div>
		이름 : <input name="contactList[1].name" type="text">
		이메일 : <input name="contactList[1].email" type="text">
	</div>
	<div>
		이름 : <input name="contactList[2].name" type="text">
		이메일 : <input name="contactList[2].email" type="text">
	</div>
	<button>등록</button>
</form>
</body>
</html>