package com.cos.bookbackend.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
// 단위 테스트 (DB 관련된 Bean이 IoC에 등록되면 됨.)

@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY) // Replace.ANY 가짜 DB로 테스트, Replace.NONE 실제 DB로 테스트
@DataJpaTest // Repository들을 IoC에 등록해줌.
public class BookRepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;
}
