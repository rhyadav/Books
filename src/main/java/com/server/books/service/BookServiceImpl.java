package com.server.books.service;

import com.server.books.dto.BookDto;
import com.server.books.repository.BookRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** Service implementation for books */
@Service
public class BookServiceImpl implements BookService {

  Logger logger = LoggerFactory.getLogger(BookService.class);

  private BookRepository bookRepository;

  public BookServiceImpl(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  /**
   * Adds a new book
   *
   * @param book add a single book
   */
  @Override
  public void addBook(BookDto book) {
    var bookEntity = book.toBook();
    logger.info("Persisting book {}", book.toString()); // can be customized
    this.bookRepository.save(bookEntity);
  }

  /**
   * Adds a list of new books
   *
   * @param books list of books
   */
  @Override
  public void addBooks(List<BookDto> books) {
    var finalBooks = books.stream().map(BookDto::toBook).toList();
    bookRepository.saveAll(finalBooks);
  }

  /**
   * @return {@link BookDto} list of books with list of ids as argument
   */
  @Override
  public List<BookDto> getBooks(List<Long> id) {
    var books = bookRepository.findAllById(id);
    return BookDto.toBooks(books);
  }

  /**
   * @return List<BookDto> all books
   */
  @Override
  public List<BookDto> getAllBooks() {
    var books = bookRepository.findAll();
    return BookDto.toBooks(books);
  }

  /**
   * @param book book to be updated, this object will replace the existing book
   */
  @Override
  public void updateBook(BookDto book) {
    var bookEntity = bookRepository.findById(book.id());
    if (bookEntity.isPresent()) {
      var finalBook = bookEntity.get();
      book.update(finalBook);
      bookRepository.save(finalBook);
    } else {
      logger.error("The book with id {} does not exist", book.id());
      throw new IllegalArgumentException("The book does not exist");
    }
  }

  /**
   * @param id of book to be deleted
   */
  @Override
  public void deleteBook(Long id) {
    if (bookRepository.existsById(id)) {
      bookRepository.deleteById(id);
    } else {
      logger.error("The book with id {} does not exist", id);
      throw new IllegalArgumentException("the book with id " + id + " does not exist");
    }
  }

  /**
   * @param id of book to be fetched
   */
  @Override
  public BookDto getBookById(Long id) {
    var book = bookRepository.findById(id);
    if (book.isPresent()) {
      return BookDto.toDto(book.get());
    } else {
      logger.error("The book with id {} does not exist", id);
      throw new IllegalArgumentException("the book with id " + id + " does not exist");
    }
  }
}
