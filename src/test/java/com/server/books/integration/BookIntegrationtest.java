package com.server.books.integration;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.books.dto.BookDto;
import com.server.books.service.BookService;
import java.time.LocalDate;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
public class BookIntegrationtest {

  @Autowired private MockMvc mvc;

  @MockBean private BookService service;

  BookDto bookDto1;
  BookDto bookDto2;
  List<BookDto> bookDtos;
  List<Long> ids;

  @BeforeEach
  public void setup() {
    this.bookDto1 = new BookDto(1L, "Atlas Shrugged", "Ayn Rand", 127824L, 250L, LocalDate.now());
    this.bookDto2 = new BookDto(2L, "FountainHead", "Ayn Rand", 127834332L, 250L, LocalDate.now());
    bookDtos = List.of(bookDto1);
    ids = List.of(3L); // negative
  }

  @Test
  public void get_books_with_valid_ids() throws Exception {
    TestRestTemplate testRestTemplate = new TestRestTemplate();
    when(service.getBooks(ids)).thenReturn(bookDtos);
    mvc.perform(get("/v1/books?id=1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  public void delete_books_with_valid_id() throws Exception {
    mvc.perform(get("/v1/books/book/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
