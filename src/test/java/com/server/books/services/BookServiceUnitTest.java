package com.server.books.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.server.books.entity.Book;
import com.server.books.repository.BookRepository;
import com.server.books.service.BookService;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class BookServiceUnitTest {

  @Autowired BookService bookService;

  @MockBean private BookRepository bookRepository;

  Book book1;
  Book book2;
  List<Book> books;
  Long id;

  @BeforeEach
  public void setup() {
    this.book1 = new Book();
    this.book1.setTitle("Book 1");
    this.book1.setId(1L);
    this.book2 = new Book();
    this.book2.setTitle("Book 2");
    books = Stream.of(book1, book2).toList();
  }

  @Test
  public void test_get_all_books() {
    when(bookRepository.findAll()).thenReturn(books);
    var result = bookService.getAllBooks();
    assertEquals(result.size(), 2);
    assertEquals(result.get(0).title(), book1.getTitle());
  }

  @Test
  public void test_get_book_by_id() {
    var ids = List.of(1L);
    when(bookRepository.findAllById(ids)).thenReturn(List.of(book1));
    var result = bookService.getBooks(ids);
    assertEquals(result.size(), 1);
  }
}
