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
		`${ctxPath}/book/getAttachList`,
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
				</div>
				</div>
				<div class="float-right">`
			if(e.fileType){
				fileList += `<a href="${e.uploadPath+"/"+e.uuid+"_"+e.fileName}" class="showImage">책 크게 보기</a>`
			}else{
				fileList += `<a href="${e.uploadPath+"/"+e.uuid+"_"+e.fileName}" class="download">다운로드</a>`
			} 
			fileList += `		
				</div>
			</li>`			
		});
		$('.uploadResultDiv ul').html(fileList);
	})
	
	// 대출 요청 버튼 클릭 시
	$('.rent').click(function(e) {
		e.preventDefault();
	    let bookId = $('#bookId').val();
	    let memberId = $('#memberId').val();
	    if (confirm('이 도서를 대출하시겠습니까?'))
	    // 이미 대출된 책인지 확인
	    $.ajax({
	        type: 'get',
	        url: `${ctxPath}/rent/check`,
	        data: { bookId: bookId, memberId: memberId },
	        async: false, // 동기식 요청으로 변경
	        success: function(response) {
	            if (response === 'duplicate') {
	                alert("이미 대출된 책입니다.");
	            } else {
	                // 대출 요청 보내기
	                $.ajax({
	                    type: 'post',
	                    url: `${ctxPath}/rent/request`,
	                    data: { bookId: bookId, memberId: memberId },
	                    success: function() {
	                        alert("도서대출 요청이 성공적으로 요청되었습니다.");
	                    },
	                    error: function() {
	                        alert("해당 도서는 대출이 불가능 합니다.");
	                    }
	                });
	            }
	        },
	        error: function() {
	            alert("해당 도서는 대출이 불가능 합니다.");
	        }
	    });
	});


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
			url : `${ctxPath}/book/like`,
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
			url : `${ctxPath}/book/islike`,
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
			url : `${ctxPath}/book/viewCnt/${bno}`,
			data : {bno : bno},
		    success: function() {}
        })
    }
});