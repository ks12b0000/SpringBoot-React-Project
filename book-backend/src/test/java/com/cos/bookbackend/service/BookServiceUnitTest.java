package com.cos.bookbackend.service;


import com.cos.bookbackend.domain.BookRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
