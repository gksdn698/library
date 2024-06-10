$(function(){
	$('.list').click(function(){
		let form = $('<form/>')
		let type = $('[name="type"]');
		let keyword = $('[name="keyword"]');
		if(type.val()&&keyword.val()){
			form.append(type).append(keyword);				
		}
		form.attr('action',`${ctxPath}/board/list`)
			.append($('[name="pageNum"]'))
			.append($('[name="amount"]'))
			.appendTo('body')
			.submit();
	})
	
	// 업로드 결과 표시
	let uploadResultList = [];
	let showUploadResult = function(attachList){
		let fileList = '';
		for(let e of attachList){
			uploadResultList.push(e);
			fileList +=`
				<li class="list-group-item" data-uuid="${e.uuid}">
					<div class="float-left">`
			if(e.fileType){
				let filePath = `${e.uploadPath}/s_${e.uuid}_${e.fileName}`;
				filePath = encodeURIComponent(filePath);
				fileList +=	`<div class="thumnail d-inline-block mr-3" style="width:40px">
						<img alt="" src="${ctxPath}/files/display/?fileName=${filePath}" style="width: 100%">
					</div>`			
			}else{
				fileList +=	`<div class="thumnail d-inline-block mr-3" style="width:40px">
						<img alt="" src="${ctxPath}/resources/images/attach.png" style="width: 100%">
					</div>`			
			}	
					
			fileList +=	`<div class="d-inline-block">
						<a href="#">${e.fileName}"</a>
					</div>
				</div>
				<div class="float-right">
					<a href="#" class="delete">삭제</a>
				</div>
			</li>`;
		}
		$('.uploadResultDiv ul').append(fileList)
	}
	
	// 파일 업로드 이벤트 
	$('input[type="file"]').change(function(){
		let formData = new FormData(); 
		let files = this.files;
		
		for(let f of files){
			formData.append('uploadFile', f);
		}
		
		$.ajax({
			url : `${ctxPath}/files/upload`, 
			type : 'post', 
			processData : false, 
			contentType : false, 
			data : formData, 
			dataType : 'json', 
			success : function(result){
				showUploadResult(result);
				console.log(uploadResultList);
			}
		});
	});
	
	// 글쓰기 처리
	$('.register').click(function(){
		let form = $('.registerForm');
		let isNotice = $('#noticeCheckbox').is(':checked');
    	// 공지사항 정보를 폼에 추가
    	form.append($('<input/>', {type: 'hidden', name: 'isNotice', value: isNotice ? 'Y' : 'N'}));
    	
		if(uploadResultList.length>0){ // 첨부파일이 있다면
			$.each(uploadResultList, function(i,e){
				let uuid = $('<input/>',{type : 'hidden', name : `attachList[${i}].uuid`, value: e.uuid});
				let fileName = $('<input/>',{type : 'hidden', name : `attachList[${i}].fileName`, value: e.fileName});
				let fileType = $('<input/>',{type : 'hidden', name : `attachList[${i}].fileType`, value: e.fileType});
				let uploadPath = $('<input/>',{type : 'hidden', name : `attachList[${i}].uploadPath`, value: e.uploadPath});
				form.append(uuid)
					.append(fileName)
					.append(fileType)
					.append(uploadPath)
			})
		}
		form.submit();
	})
	
	// 파일 업로드 취소
	$('.uploadResultDiv ul').on('click','.delete',function(e){
		e.preventDefault();
		let uuid = $(this).closest('li').data('uuid');
		let targetFileIdx = -1;
		let targetFile = null;
		
		$.each(uploadResultList, function(i,e){
			if(e.uuid==uuid){
				targetFileIdx = i;
				targetFile = e;
				return;
			}
		});
		
		if(targetFileIdx!=-1)		{
			uploadResultList.splice(targetFileIdx,1);
		}
		console.log(uploadResultList)
		
		// ajax요청
		$.ajax({
			type : 'post',
			url : `${ctxPath}/files/deleteFile`,
			data  : targetFile,
			success : function(result){
				console.log(result);
			}
		})
		
		$(this).closest('li').remove();
	})
	
	// 공지사항 체크박스 클릭 이벤트 처리
	$('#noticeCheckbox').change(function() {
	    // 공지사항 체크박스의 상태를 확인하여 폼 필드의 값을 설정
	    let noticeValue = $(this).is(':checked'); // 체크박스의 체크 여부에 따라 true 또는 false로 설정
	    
	    // 공지사항 체크박스의 상태를 폼 필드에 설정
	    $('[name="notice"]').val(noticeValue);
	});

})