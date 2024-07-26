package com.server.books.service;

import com.server.books.dto.BookDto;
import java.util.List;

public interface BookService {
  public void addBook(BookDto book);

  public void addBooks(List<BookDto> books);

  public List<BookDto> getBooks(List<Long> id);

  public List<BookDto> getAllBooks();

  public void updateBook(BookDto book);

  public void deleteBook(Long id);

  public BookDto getBookById(Long id);
}
