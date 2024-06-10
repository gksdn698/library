$(function(){
	let authForm = $('#authForm');
	let code = null; // 인증번호 
	let submitFlag = { 
		email : '', 
		isAuth : false,	
	}; 
	
	// 이메일과 인증 여부
	$('#mailCheckBtn').click(function() {
		const email = $('#email').val(); // 이메일 
		const checkInput = $('.checkInput');
		
		//ajax성공		
		$.ajax({
			type : 'get', 
			url : `${ctxPath}/mailCheck?email=`+email, 
			success : function(result){
				submitFlag.email = email;
				checkInput.attr('disabled',false);
				code = result;
				alert('인증번호가 전송되었습니다.')
			}
		});
	});
	
	// 인증 일치 여부 확인
	$('.checkInput').on('keyup',function(){
		const inputCode = $(this).val();
		const resultMessage = $('.resultMessage');
	
	// AJAX 요청을 통해 코드를 비교	
		if(inputCode == code){
			resultMessage.html('인증되었습니다.');
			submitFlag.isAuth = true;
			resultMessage.css('color','green');
			$(this).removeClass('border-danger')
				.addClass('border border-success')	
				.css('box-shadow','0 0 0 0.2rem rgba(0,128,0,.25)')
		} else {
			resultMessage.html('인증번호가 일치하지 않음.');
			submitFlag.isAuth = false;
			resultMessage.css('color','red');
			$(this).addClass('border border-danger')
				.css('box-shadow','0 0 0 0.2rem rgba(255,0,0,.25)')
		}
});
	// 다음 단계로 
	$('.nextBtn').click(function(){
		console.log(submitFlag);
		$('#email').val(submitFlag.email);
		if(!submitFlag.isAuth){
			alert('인증되지 않았습니다.')
			return;
		}
		authForm.submit();
	})
});
