package com.cos.bookbackend.web;

import com.cos.bookbackend.domain.Book;
import com.cos.bookbackend.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void init() {
        entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate(); // ec2
//        entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate(); // mySQL
    }

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

    @Test
    public void findAll_Test() throws Exception{
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "스프링부트 따라하기", "코스"));
        books.add(new Book(null, "리엑트 따라하기", "코스"));
        bookRepository.saveAll(books);

        // when
        ResultActions resultActions = mvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findById_Test() throws Exception{
        // given
        Long id = 1L;

        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "스프링부트 따라하기", "코스"));
        books.add(new Book(null, "리엑트 따라하기", "코스"));
        bookRepository.saveAll(books);

        // when
        ResultActions resultActions = mvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("스프링부트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_Test() throws Exception{
        // given
        Long id = 1L;

        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "스프링부트 따라하기", "코스"));
        books.add(new Book(null, "리엑트 따라하기", "코스"));
        bookRepository.saveAll(books);

        Book book = new Book(null, "따라하기2", "코스");
        String content = new ObjectMapper().writeValueAsString(book);

        // when
        ResultActions resultActions = mvc.perform(put("/book/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("따라하기2"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_Test() throws Exception{
        // given
        Long id = 1L;

        Book book = new Book(null, "스프링부트 따라하기", "코스");
        bookRepository.save(book);

        // when
        ResultActions resultActions = mvc.perform(delete("/book/{id}", id)
                .accept(MediaType.TEXT_PLAIN));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        MvcResult requestResult = resultActions.andReturn();
        String result = requestResult.getResponse().getContentAsString();

        assertThat("ok").isEqualTo(result);
    }
}
