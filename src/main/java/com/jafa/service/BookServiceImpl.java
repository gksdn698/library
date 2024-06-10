package com.jafa.service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jafa.domain.BookAttachVO;
import com.jafa.domain.BookCriteria;
import com.jafa.domain.BookVO;
import com.jafa.domain.LikeDTO;
import com.jafa.repository.ArticleLikeRepository;
import com.jafa.repository.BookAttachRepository;
import com.jafa.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private BookAttachRepository bookAttachRepository;

	@Autowired
	private ArticleLikeRepository articleLikeRepository;
	
	@Override
	public List<BookVO> getList(BookCriteria bookCriteria) {
		List<BookVO> list = bookRepository.getList(bookCriteria);
        // 각 BookVO 객체에 attachList를 설정하는 작업 수행
        for (BookVO book : list) {
            book.setAttachList(bookAttachRepository.selectByBno(book.getBno()));
        }
        return list;	
    }

	@Override
	public void register(BookVO book) {
		bookRepository.insertSelectKey(book);
		List<BookAttachVO> attachList = book.getAttachList();
		if(attachList!=null && !attachList.isEmpty()) { // 첨부파일 있다면
			attachList.forEach(attachFile -> {attachFile.setBno(book.getBno());
			bookAttachRepository.insert(attachFile);
			});
		}
	}

	@Override
	public BookVO get(Long bno) {
		return bookRepository.read(bno);
	}

	@Transactional
	@Override
	public boolean modify(BookVO book) {
		List<BookAttachVO> attachList = book.getAttachList();
		
		if(attachList!=null && !attachList.isEmpty()) { // 첨부파일 삭제 또는 추가
			List<BookAttachVO> delList = attachList.stream()
				.filter(attach ->attach.getBno()!=null)
				.collect(Collectors.toList());
			// 파일 삭제
			deleteFiles(delList);
			
			// 데이터베이스 기록삭제
			delList.forEach(vo->{
				bookAttachRepository.delete(vo.getUuid());
			});
			
			// 새로운 파일 추가
			attachList.stream().filter(attach ->attach.getBno()==null).forEach(vo->{
				vo.setBno(book.getBno());
				bookAttachRepository.insert(vo);
			});
		}
		
		return bookRepository.update(book)==1;
	}

	@Override
	public boolean remove(Long bno) {
		List<BookAttachVO> attachList = getAttachList(bno);
		if(attachList!=null && !attachList.isEmpty()) {
			deleteFiles(attachList);
			bookAttachRepository.deleteAll(bno);
		}
		
		return bookRepository.delete(bno)==1;
	}

	@Override
	public int totalCount(BookCriteria bookCriteria) {
		return bookRepository.getTotalCount(bookCriteria);
	}

	@Override
	public List<BookAttachVO> getAttachList(Long bno) {
		return bookAttachRepository.selectByBno(bno);
	}

	@Override
	public BookAttachVO getAttach(String uuid) {
		return bookAttachRepository.selectByUuid(uuid);
	}

	@Transactional
	@Override
	public boolean hitLike(LikeDTO likeDTO) {
		LikeDTO result = articleLikeRepository.Bget(likeDTO);
		if(result==null) { // 추천 
			articleLikeRepository.Binsert(likeDTO);
			bookRepository.updateLikeCnt(likeDTO.getBno(), 1);
			return true; 
		} else { // 추천 취소 
			articleLikeRepository.Bdelete(likeDTO);
			bookRepository.updateLikeCnt(likeDTO.getBno(), -1);
			return false;
		}	
	}

	@Override
	public Boolean isLike(LikeDTO likeDTO) {
		return articleLikeRepository.Bget(likeDTO) != null ;
	}

	@Override
	public void viewCnt(Long bno, int amount) {
		bookRepository.viewCnt(bno, amount);
	}

	@Override
	public boolean isAvailable(Long bno) {
		return bookRepository.isAvailable(bno);
	}

	@Override
	public void updateAvailability(Long bno, boolean available) {
		bookRepository.updateAvailability(bno, available);
	}

	private void deleteFiles(List<BookAttachVO> delList) {
		delList.forEach(vo->{
			File file = new File("C:/storage/book/"+vo.getUploadPath(),vo.getUuid() + "_" + vo.getFileName());
			file.delete();
			if(vo.isFileType()) {
				file = new File("C:/storage/book/"+vo.getUploadPath(),"s_"+vo.getUuid() + "_" + vo.getFileName());
				file.delete();
			}
		});
	}
}
