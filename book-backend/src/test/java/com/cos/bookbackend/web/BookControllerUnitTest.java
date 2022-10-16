package com.cos.bookbackend.web;

import com.cos.bookbackend.domain.Book;
import com.cos.bookbackend.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 단위 테스트 (Controller, Filter, ControllerAdvice)

@Slf4j
@WebMvcTest
public class BookControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean // IoC환경에 bean 등록됨.
    private BookService bookService;

    // BDDMockito 패턴
    @Test
    public void save_Test() throws Exception{
        // given (테스트를 하기 위한 준비)
        Book book = new Book(null, "스프링따라하기", "코스");
        String content = new ObjectMapper().writeValueAsString(book);

        // stub (행동 지정)
        when(bookService.저장하기(book)).thenReturn(new Book(1L, "스프링따라하기", "코스"));

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
        books.add(new Book(1L, "스프링부트 따라하기", "코스"));
        books.add(new Book(2L, "리엑트 따라하기", "코스"));
        when(bookService.모두가져오기()).thenReturn(books);

        // when
        ResultActions resultActions = mvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findById_Test() throws Exception{
        // given
        Long id = 1L;
        when(bookService.한건가져오기(id)).thenReturn(new Book(1L, "자바 공부하기", "쌀"));

        // when
        ResultActions resultActions = mvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("자바 공부하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_Test() throws Exception{
        // given
        Long id = 1L;
        Book book = new Book(null, "따라하기2", "코스");
        String content = new ObjectMapper().writeValueAsString(book);
        when(bookService.수정하기(id, book)).thenReturn(new Book(1L, "따라하기2", "코스"));

        // when
        ResultActions resultActions = mvc.perform(put("/book/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("따라하기2"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_Test() throws Exception{
        // given
        Long id = 1L;
        when(bookService.삭제하기(id)).thenReturn("ok");

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
