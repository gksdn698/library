$(function(){
	$('.list').click(function(){
		let form = $('<form/>')
		let type = $('[name="type"]');
		let keyword = $('[name="keyword"]');
		if(type.val()&&keyword.val()){
			form.append(type).append(keyword);				
		}
		form.attr('action',`${ctxPath}/book/list`)
			.append($('[name="pageNum"]'))
			.append($('[name="amount"]'))
			.appendTo('body')
			.submit();
	})
	
	// 업로드 결과 표시
 	let attachList = [];
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
				// 파일 정보를 JavaScript 변수에 추가
				attachList.push({uuid: e.uuid, fileName: e.fileName, fileType: e.fileType, uploadPath: e.uploadPath});
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
	
})