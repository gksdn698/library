$(function(){
	$(function(){
		let termForm = $('#termForm')
		let agreementCheck = $('[name="agreement"]');
		
		// 모두 동의
		$('.checkAll').change(function(){
			if($(this).is(':checked')){
				$('[name="agreement"]').prop('checked',true);
			}else{
				$('[name="agreement"]').prop('checked',false);
			}
		});
		
		// 다음단계로 이동
		$('.nextBtn').click(function(){
			let checkFlag = [];
			$(agreementCheck).each(function(index,element){
				checkFlag.push($(element).is(':checked'));
			})
			
			if(!checkFlag[0]){
				alert('이용약관에 동의해주세요')
				return;
			}
			
			if(!checkFlag[1]){
				alert('개인정보 수집 및 이용에 대한 안내에 동의해주세요')
				return;
			}
			termForm.submit();
		});
	})
})