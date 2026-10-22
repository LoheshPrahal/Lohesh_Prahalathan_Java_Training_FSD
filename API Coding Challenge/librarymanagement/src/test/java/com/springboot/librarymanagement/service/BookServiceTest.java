package com.springboot.librarymanagement.service;

import com.springboot.librarymanagement.dto.request.BookDto;
import com.springboot.librarymanagement.model.Book;
import com.springboot.librarymanagement.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;

    @Test
    public void getAllBooksTest(){
        Book book1 = new Book(1L, "test1", "author1", "1234567890987", 1997);
        Book book2 = new Book(2L, "test2", "author2", "0987654321234", 2000);
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        assertEquals(2, bookService.getAllBooks().size());
        verify(bookRepository, times(1)).findAll();
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void getBookByISBNTest(){
        Book book = new Book(1L, "test1", "author1", "1234567890987", 1997);
        when(bookRepository.getBookByISBN("1234567890987")).thenReturn(book);

        assertEquals("test1", bookService.getBookByISBN("1234567890987").title());
        assertEquals("1234567890987", bookService.getBookByISBN("1234567890987").isbn());

        verify(bookRepository, times(2)).getBookByISBN("1234567890987");
        verifyNoMoreInteractions(bookRepository);

    }

    @Test
    public void getBookByISBNTest_NotFound() {
        when(bookRepository.getBookByISBN("0987654321456")).thenReturn(null);

        assertNull(bookService.getBookByISBN("0987654321456"));

        verify(bookRepository, times(1)).getBookByISBN("0987654321456");
        verifyNoMoreInteractions(bookRepository);
    }



}
