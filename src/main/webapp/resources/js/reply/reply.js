$(function(){
	
	let commentContainer = $('.comment');
	let bno = $('input[name="bno"]').val();
	let pageNum = 1; // 기본 페이지 번호
	let paginationWrap = $('.pagination_wrap');
	
	// 페이지 네이션
	let showReplyPage = function(replyCount){
		let endNum = Math.ceil(pageNum/10.0)*10;
		let startNum = endNum -10 + 1;
		let tempEndNum = Math.ceil(replyCount/5.0);
		
		let prev = startNum !=1;
		let next = endNum < tempEndNum;
		if(endNum>tempEndNum) endNum = tempEndNum;
		
		let pagination = '<ul class="pagination">';
		if(prev){
			pagination +=`<li class="page-item">
						<a class="page-link" href="${startNum-1}">이전페이지</a>
					</li>`;
		}
		
		for(let pagelink = startNum ; pagelink <= endNum ; pagelink++){
			pagination +=`<li class="page-item ${ pagelink == pageNum ? 'active':''}">
						<a href="${pagelink}" class="page-link ">${pagelink}</a>
					</li>`;
		}
		
		if(next){
			pagination +=`<li class="page-item">
						<a class="page-link" href="${endNum+1}">다음페이지</a>
					</li>`;
		}
		
		pagination += '</ul>';
		paginationWrap.html(pagination)
	}
	
	// 댓글 목록
	let showList = function(page){
		let param = {
			bno : bno,
			page : page 
		}
		replyService.getList(param,function(replyCount,list){
			
			// 댓글 작성후 마지막 페이지 호출
			if(page== -1){
				pageNum = Math.ceil(replyCount/5.0); // let 쓰지말기
				showList(pageNum);
				return;
			}
			
			// 글삭제 이후 페이지 번호 유지
			if(page==-2){
				pageNum = replyCount%5==0 ? pageNum-1 : pageNum;
				showList(pageNum);
				return;
			}
			
			// 댓글이 존재하지 않을 경우
			if(replyCount==0){
				commentContainer.html('등록된 댓글이 없습니다.');
				return;
			}
			
			let replyList = '';
			$.each(list,function(i,e){
				replyList += `
					<li class="list-group-item" data-rno="${e.rno}">
		            <div class="row">
		                <div class="col-1">
		                    <div style="width: 75px;">
		                        <img src="${ctxPath}/resources/images/siba.png" class="rounded-circle" style="width: 100%;">
		                    </div>
		                </div>
		                <div class="col-11">
		                    <div>
		                        <div>
		                            <span class="userName badge badge-pill badge-info mr-2">${e.replyer}</span>
		                            <span>${e.replyDate}</span>
		                        </div>
		                        <div class="d-flex justify-content-between">
		                            <div class="py-2">${e.reply}</div>`
		                    if(memberId==e.replyer || auth.includes('ROLE_ADMIN')){
                            replyList += `<div class="dropdown">
		                                <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
		                                    변경
		                                </button>
		                                <div class="dropdown-menu replyModify">
		                                    <button class="dropdown-item btn btn-light" data-oper="modify">수정</button>
		                                    <button class="dropdown-item btn btn-light" data-oper="delete">삭제</button>
		                                </div>
		                            </div>`
							}
                        	replyList +=`</div>
		                    </div>
		                </div>
		            </div>
		        </li>`;	
			})
			commentContainer.html(replyList)
			showReplyPage(replyCount);
		})
	}
	showList(pageNum);
	
	// 페이지 이동
	paginationWrap.on('click','a',function(e){
		e.preventDefault();
		pageNum = $(this).attr('href');
		showList(pageNum);
	})
	
	// 댓글 등록
	$('.submit button').click(function(){
		let reply = {
			bno : bno,
			reply : $('.replyContent').val(),
			replyer : $('.replyer').html()
		};
		
		replyService.add(reply,function(result){
			if(result=='success'){
				alert('댓글 등록성공')
			}else{
				alert('실패')
			}
			$('.replyContent').val('');
			showList(-1);
		});
	})
	
	// 수정 삭제
	$('.comment').on('click','.replyModify button',function(e){
		e.preventDefault();
		let operation = $(this).data('oper');
		let listTag = $(this).closest('li');
		let rno = listTag.data('rno');
		let replyer = $(this).closest('li').find('.userName').text();

		if(replyer!=memberId && !auth.includes('ROLE_ADMIN')){
			return;
		}
				
		if(operation=='delete'){
			replyService.remove(rno,function(result){
				if(result=='success'){
					alert(`${rno}번 댓글을 삭제`);
				}else{
					alert('삭제실패');
				}
				showList(-2);
			})
		}
		
		if(operation=='modify'){
			let replyUpdateForm = $('.replyWriterForm').clone();
			replyUpdateForm.attr('class','replyUpdateForm');
			let updateBtn = replyUpdateForm.find('button').html('수정');
			
			if(listTag.find('.replyUpdateForm').length > 0){
				listTag.find('.replyUpdateForm').remove();
				$(this).html('수정');
				$(this).next().show();
				return;
			}
			
			replyService.get(rno,function(r){
				replyUpdateForm.find('.replyContent').val(r.reply);
				replyUpdateForm.find('.replyer').html(r.replyer);
			})
			
			listTag.append(replyUpdateForm);
			$(this).html('취소');
			$(this).next().hide();
			
			updateBtn.click(function(){
				let reply ={
					rno  : rno,
					reply : replyUpdateForm.find('.replyContent').val()
				}
				replyService.update(reply,function(result){
					if(result=='success'){
						alert(`${rno}번 댓글을 수정`);
					}else{
						alert('수정 실패');
					}
					showList(pageNum);
				})
			}) 
		}
	})
});