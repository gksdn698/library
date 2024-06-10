package com.jafa.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jafa.config.RootConfig;
import com.jafa.domain.BoardVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
@Log4j
public class DataInsert {
	@Autowired
	BoardRepository repository; 
	
	
	@Test
	public void test() {
		
		for(int i=1;i<=212;i++) {
			BoardVO vo = BoardVO.builder()
					.title("제목 : 스프링 정보처리기사 " + i)
					.content("내용 : 자바 오라클 " + i)
					.writer("작성자" + (i%5))
					.build();
			repository.insert(vo);			
		}
		
		for(int i=1;i<=212;i++) {
			BoardVO vo = BoardVO.builder()
					.title("제목 : 오라클 " + i)
					.content("내용 : 정보처리기사 " + i)
					.writer("글쓴이" + (i%5))
					.build();
			repository.insert(vo);			
		}
		
		for(int i=1;i<=212;i++) {
			BoardVO vo = BoardVO.builder()
					.title("제목 : 자바 " + i)
					.content("내용 : 스프링 정보처리기사 " + i)
					.writer("관리자" + (i%5))
					.build();
			repository.insert(vo);			
		}
		
		for(int i=1;i<=212;i++) {
			BoardVO vo = BoardVO.builder()
					.title("제목 : 테스트 데이터 " + i)
					.content("내용 : 스프링부트 " + i)
					.writer("스프링" + (i%5))
					.build();
			repository.insert(vo);			
		}
	}
}
