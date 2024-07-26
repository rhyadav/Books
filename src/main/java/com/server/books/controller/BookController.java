package com.server.books.controller;

import com.server.books.dto.BookDto;
import com.server.books.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Entry point to the Book Application. Assumptions are:
 *
 * <ul>
 *   <li>ids are unique
 *   <li>fields author and title are mandatory and books cannot exist without them
 *   <li>ids are the primary search criteria
 *   <li>multiple items can be fetched by clients by currying single items, this is for brevity
 *   <li>embedded hsql db for brevity
 *   <li>single db table with Book Entity
 * </ul>
 */
@Validated
@RestController()
@RequestMapping("v1")
public class BookController {

  Logger logger = LoggerFactory.getLogger(BookController.class);

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  /**
   * Controller endpoint for adding multiple books
   *
   * @param books list of books
   */
  @PostMapping(value = "books", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void addBook(
      @RequestBody @NotEmpty(message = "The books list cannot be empty")
          List<@Valid BookDto> books) {
    bookService.addBooks(books);
  }

  /**
   * Controller endpoint for adding one book
   *
   * @param book single book
   */
  @PostMapping(value = "books/book", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void addBook(@RequestBody @Valid BookDto book) {
    logger.info("Adding book {}", book.toString()); // can be customized
    bookService.addBook(book);
  }

  /**
   * Controller endpoint for updating/replacing a book, the book object will be replaced with the
   * argument object.
   *
   * @param book request payload as book object
   * @param bookId id of the book
   */
  @PutMapping(value = "books/book/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void updateBook(@RequestBody @Valid BookDto book, @PathVariable("id") long bookId) {
    logger.info("update book {}", book.toString()); // can be customized
    bookService.updateBook(book);
  }

  /**
   * Controller endpoint for fetching a book by Id assuming that Id's are unique.
   *
   * @param bookId search by book id
   * @return book
   */
  @GetMapping(value = "books/book/{id}")
  @ResponseStatus(HttpStatus.OK)
  public BookDto getBookById(@PathVariable("id") long bookId) {
    BookDto book = null;
    try {
      book = bookService.getBookById(bookId);
    } catch (IllegalArgumentException exception) {
      logger.error("Exception while fetching book with id {}", bookId);
    }
    return book;
  }

  /**
   * Controller endpoint for deleting a book by Id assuming that Id's are unique.
   *
   * @param bookId the id of the book to be deleted
   */
  @DeleteMapping(value = "books/book/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteBookById(@PathVariable("id") long bookId) {
    try {
      bookService.deleteBook(bookId);
    } catch (IllegalArgumentException exception) {
      logger.error("Exception while deleting books {}", exception.getMessage());
    }
  }

  /**
   * Controller endpoint for fetching books. all books that have any of the ids are returned,
   * effectively an OR, if ids are not present return all books
   *
   * @param id list of ids as request param
   * @return list of books
   */
  @GetMapping(value = "books")
  @ResponseStatus(HttpStatus.OK)
  public List<BookDto> getBooksByIds(@RequestParam(required = false, name = "id") List<Long> id) {
    List<BookDto> books = List.of();
    try {
      if (id == null || id.isEmpty()) {
        books = bookService.getAllBooks();
      } else {
        books = bookService.getBooks(id);
      }
    } catch (IllegalArgumentException exception) {
      logger.error("Exception while getting books {}", exception.getMessage());
    }
    return books;
  }
}
