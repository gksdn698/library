package com.jafa.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jafa.config.RootConfig;
import com.jafa.config.ServletConfig;
import com.jafa.domain.ReplyVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class,ServletConfig.class})
@WebAppConfiguration
@Log4j
public class ReplyControllerTest {

	@Autowired
	WebApplicationContext ctx;
	
	ObjectMapper objectMapper;
	
	MockMvc mockMvc;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
		objectMapper = Jackson2ObjectMapperBuilder.json().build();
	}
	
	@Test
	@Ignore
	public void registertest() throws Exception {
		ReplyVO vo = ReplyVO.builder()
			.bno(1L)
			.reply("웹 계층 : 댓글 추가 테스트")
			 .replyer("작성자").build();
		 String content = objectMapper.writeValueAsString(vo);
		 mockMvc.perform(MockMvcRequestBuilders.post("/replies/new")
				 .content(content).contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@Ignore
	public void modifytest() throws Exception {
		ReplyVO vo = ReplyVO.builder()
			.reply("웹 계층 : 댓글 테스트 수정").build();
		 String content = objectMapper.writeValueAsString(vo);
		 mockMvc.perform(MockMvcRequestBuilders.put("/replies/6")
				 .content(content).contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	public void removetest() throws Exception {
		 mockMvc.perform(MockMvcRequestBuilders.delete("/replies/6"));
	}
}
