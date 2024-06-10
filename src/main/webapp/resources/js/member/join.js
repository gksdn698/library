$(function(){
	let idCheckFlag = false; 
	$('.idCheck').click(function(){
		let idInput = $('#memberId')
		let memberId = $('#memberId').val();
		
		if(idInput.attr('readonly')){ // 이미 값이 입력된 경우 
			idInput.attr('readonly',false);
			idInput.focus();
			$(this).html('ID중복확인');
			idCheckFlag = false;
			return; 
		}
		
		if(memberId=='') {
			alert('아이디를 입력하세요')
			return; 			
		}
		
		$.ajax({ // 중복검사 
			type : 'post', 
			url : `${ctxPath}/member/idCheck`,
			data : { memberId : memberId },
			async : false,
			success : function(result){
				if(result){ // 사용할 수 있는 경우
					alert('사용할 수 있는 아이디 입니다.')
					idCheckFlag = true;
					$('.idCheck').html('변경');
					idInput.attr('readonly',true);
				} else { // 중복되는 경우 
					alert('사용할수 없는 아이디입니다.');
					idInput.focus();
				}
			}
		});
	});
	
	$('.join').click(function(){
		if(!idCheckFlag){
			alert('ID 중복체크 바람');
			return;
		} 
		$('#memberVO').submit(); 
	});
});