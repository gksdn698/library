package com.jafa.repository;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jafa.config.RootConfig;
import com.jafa.domain.Criteria;
import com.jafa.domain.ReplyVO;
import com.jafa.domain.ReplyVO.ReplyVOBuilder;

import lombok.extern.log4j.Log4j;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
@Log4j
public class ReplyRepositoryTest {

	@Autowired
	ReplyRepository replyRepository;
	
	@Test
	@Ignore
	public void gettest() {
		replyRepository.getList(1L, new Criteria());
	}

	@Test
	@Ignore
	public void read() {
		ReplyVO read = replyRepository.read(4L);
		log.info(read);
	}
	
	@Test
	@Ignore
	public void insert() {
		ReplyVO vo = ReplyVO.builder().bno(2L)
			.reply("댓글 추가")
			.replyer("작성자")
			.build();
		replyRepository.insert(vo);
	}
	
	@Test
	@Ignore
	public void update() {
		ReplyVO vo = ReplyVO.builder().rno(5L)
			.reply("댓글 추가 수정")
			.build();
		replyRepository.update(vo);
	}
	
	@Test
	@Ignore
	public void delete() {
			replyRepository.delete(5L);
	}
	
}
