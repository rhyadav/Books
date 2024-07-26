package com.server.books.dto;

import com.server.books.entity.Book;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Book DTO that represents de-serialized book, here it is assumed that functional requirements
 * define that {@link BookDto#title} and author {@link BookDto#author} are required
 *
 * @param id
 * @param title
 * @param author
 * @param isbn
 * @param price
 */
public record BookDto(
    Long id,
    @NotNull String title,
    @NotNull String author,
    Long isbn,
    Long price,
    LocalDate publicationDate) {

  public Book toBook() {
    var book = new Book();
    book.setAuthor(this.author); // null check done
    book.setTitle(this.title); // null check done
    if (isbn != null) {
      book.setIsbn(isbn);
    }

    if (price != null) {
      book.setPrice(price);
    }
    return book;
  }

  public static BookDto toDto(Book book) {
    var id = book.getId();
    var title = book.getTitle();
    var author = book.getAuthor();
    var isbn = book.getIsbn();
    var price = book.getPrice();
    var publicationDate = book.getPublicationDate();
    return new BookDto(id, title, author, isbn, price, publicationDate);
  }

  public void update(Book book) {
    book.setTitle(this.title);
    book.setAuthor(this.author);
    book.setIsbn(this.isbn);
    book.setPrice(this.price);
  }

  /**
   * returns all books
   *
   * @param books
   * @return
   */
  public static List<BookDto> toBooks(List<Book> books) {
    var book =
        books.stream()
            .map(
                (bookEntity) -> {
                  return new BookDto(
                      bookEntity.getId(),
                      bookEntity.getTitle(),
                      bookEntity.getAuthor(),
                      bookEntity.getIsbn(),
                      bookEntity.getPrice(),
                      bookEntity.getPublicationDate());
                })
            .toList();
    return book;
  }
}
