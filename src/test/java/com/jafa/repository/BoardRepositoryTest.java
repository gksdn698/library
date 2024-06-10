package com.jafa.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jafa.config.RootConfig;
import com.jafa.domain.BoardVO;
import com.jafa.domain.Criteria;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
@Log4j
public class BoardRepositoryTest{

	@Autowired
	BoardRepository boardRepository;
	
	@Ignore
	@Test
	public void readtest() {
		log.info(boardRepository.read(1L));
	}
	
	@Ignore
	@Test
	public void getlisttest() {
		Criteria criteria = new Criteria(3,10);
		log.info(boardRepository.getList(criteria));
	}

	@Test
	@Ignore
	public void insert() {
		BoardVO vo = new BoardVO();
		vo.setTitle("title");
		vo.setContent("content");
		vo.setWriter("writer");
		boardRepository.insert(vo);
		log.info(vo.getBno());
	}
	
	@Test
	@Ignore
	public void update() {
		BoardVO vo = new BoardVO();
		vo.setBno(5L);
		vo.setTitle("title2");
		vo.setContent("content2");
		boardRepository.update(vo);
	}
	
	@Test
	@Ignore
	public void delete() {
		boardRepository.delete(5L);
	}
	
	@Test
	public void search() {
		Criteria criteria = new Criteria(30,10);
		criteria.setType("TC");
		criteria.setKeyword("오라클");
		boardRepository.getList(criteria);
	}
}
