$(function(){
	let bnoValue = $('[name="bno"]').val();
	console.log(bnoValue)
	/*
	$.ajax({
		type : 'get',
		url : `${ctxPath}/board/getAttachList`,
		data : {bno : bnoValue},
		success :  function(attachList){
			console.log(attachList);
		}
	})
	 */
	
	$.getJSON(
		`${ctxPath}/board/getAttachList`,
		{bno : bnoValue},
		function(attachList){
			let fileList = '';
			$.each(attachList,function(i,e){
			fileList += `<li class="list-group-item" data-uuid="${e.uuid}">
				<div class="float-left">`
			if(e.fileType){ // 이미지 파일인 경우 섬네일 표시
				let filePath = e.uploadPath+"/s_"+e.uuid+"_"+e.fileName; 
				filePath = encodeURIComponent(filePath);
				fileList +=`
					<div class="thumnail d-inline-block mr-3">
						<img alt="" src="${ctxPath}/files/display?fileName=${filePath}">	
					</div>`
			} else {
				fileList +=` 
					<div class="thumnail d-inline-block mr-3" style="width:40px">
						<img alt="" src="${ctxPath}/resources/images/attach.png" style="width: 100%">
					</div>`
			}
			fileList +=		
				`<div class="d-inline-block">
					${e.fileName}
				</div>
				</div>
				<div class="float-right">`
			if(e.fileType){
				fileList += `<a href="${e.uploadPath+"/"+e.uuid+"_"+e.fileName}" class="showImage">원본보기</a>`
			}else{
				fileList += `<a href="${e.uploadPath+"/"+e.uuid+"_"+e.fileName}" class="download">다운로드</a>`
			} 
			fileList += `		
				</div>
			</li>`			
		});
		$('.uploadResultDiv ul').html(fileList);
	})
	
	// 원본이미지 보기
	$('.uploadResultDiv ul').on('click','.showImage',function(e){
		e.preventDefault();
		let filePath = $(this).attr('href');
		let imgSrc = `${ctxPath}/files/display?fileName=${filePath}`;
		$('#showImage .modal-body').html($('<img>',{src  : imgSrc, class : 'img-fluid'}));
		$('#showImage').modal();
	})
	
	// 파일 다운로드
	$('.uploadResultDiv ul').on('click','.download',function(e){
		e.preventDefault();
		self.location = `${ctxPath}/files/download?fileName=${$(this).attr('href')}`
	});
	
	// 추천
	$('.like').click(function(){
		let bno = $('[name="bno"]').val();
		$.ajax({
			type : 'post',
			url : `${ctxPath}/board/like`,
			data : {memberId : memberId, bno : bno},
			success : function(message){
				alert(message);
				isLike();
			}
		}) 
	})
	
	// 추천 여부
	function isLike(){
		let bno = $('[name="bno"]').val();
		$.ajax({
			tyep : 'post',
			url : `${ctxPath}/board/islike`,
			data : {memberId : memberId, bno : bno},
			success : function(result){
				if(result){
					$('.like').html('추천취소')
				} else {
					$('.like').html('추천')
				}
			}
		})
	}
	if(memberId!=''){
		isLike();
	}
	
	$(document).ready(function() {
    	viewCnt();
	});
	
	function viewCnt(){
		let bno = $('[name="bno"]').val();
		$.ajax({
			type : 'get',
			url : `${ctxPath}/board/viewCnt/${bno}`,
			data : {bno : bno},
		    success: function() {}
        })
    }
});