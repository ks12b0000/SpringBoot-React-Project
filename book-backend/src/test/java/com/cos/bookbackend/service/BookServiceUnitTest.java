package com.cos.bookbackend.service;


import com.cos.bookbackend.domain.Book;
import com.cos.bookbackend.domain.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

/*
    단위 테스트 (Service와 관련된 애들만 메모리에 뛰우면 됨.)
    BoardRepository = 가짜 객체로 만들 수 있음.
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

    @InjectMocks // BookService객체가 만들어질 때 BookServiceUnitTest 파일에 @Mock로 등록된 모든 애들을 주입받음.
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void save_Test() {
        // given
        Book book = new Book();
        book.setTitle("제목1");
        book.setAuthor("저자1");

        when(bookRepository.save(book)).thenReturn(book);

        // when
        Book bookEntity = bookService.저장하기(book);

        // then
        assertThat(book).isEqualTo(bookEntity);
    }

    @Test
    public void findAll_Test() {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "제목1", "저자1"));
        books.add(new Book(2L, "제목2", "저자2"));

        when(bookRepository.findAll()).thenReturn(books);

        // when
        List<Book> booksEntity = bookService.모두가져오기();

        // then
        assertThat(books).isEqualTo(booksEntity);
    }

    @Test
    public void findById_Test() {
        // given
        Long id = 1L;
        Book book = new Book(null, "스프링따라하기", "코스");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        // when
        Book bookEntity = bookService.한건가져오기(id);

        // then
        assertThat(book).isEqualTo(bookEntity);

    }

    @Test
    public void update_Test() {
        // given
        Long id = 1L;
        Book book = new Book();
        book.setTitle("제목1");
        book.setAuthor("저자1");

        Book book2 = new Book(1L, "제목2", "저자2");
        when(bookRepository.findById(id)).thenReturn(Optional.of(book2));

        // when
        Book bookEntity1 = bookService.수정하기(id, book);

        // then
        assertThat(book.getTitle()).isEqualTo(bookEntity1.getTitle());
    }
}
