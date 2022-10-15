package com.cos.bookbackend.web;

import com.cos.bookbackend.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    통합 테스트 (모든 Bean 들을 똑같이 IoC올리고 테스트 하는 것.)
    WebEnvironment.MOCK = 실제 톰캣을 올리는게 아니라 다른 톰캣으로 테스트
    WebEnvironment.RANDOM_PORT = 실제 톰캣으로 테스트
    @AutoConfigureMockMvc = MockMvc를 IoC에 등록해줌.
    @Transactional = 각각의 테스트함수가 종료될 때 마다 트랜잭션을 rollback해주는 어노테이션
 */

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {

    @Autowired
    private MockMvc mvc;

    // BDDMockito 패턴
    @Test
    public void save_Test() throws Exception{
        // given (테스트를 하기 위한 준비)
        Book book = new Book(null, "스프링따라하기", "코스");
        String content = new ObjectMapper().writeValueAsString(book);

        // when (테스트 실행)
        ResultActions resultActions = mvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then (검증)
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("스프링따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }
}
