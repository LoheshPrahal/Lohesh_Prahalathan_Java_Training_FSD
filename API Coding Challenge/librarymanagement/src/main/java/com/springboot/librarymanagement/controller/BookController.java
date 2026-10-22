package com.springboot.librarymanagement.controller;

import com.springboot.librarymanagement.dto.request.BookDto;
import com.springboot.librarymanagement.dto.response.BookRespDto;
import com.springboot.librarymanagement.model.Book;
import com.springboot.librarymanagement.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/api/book/all")
    public List<BookRespDto> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/api/book/{isbn}")
    public BookRespDto getBookByISBN(@PathVariable String isbn){
        return bookService.getBookByISBN(isbn);
    }

    @PostMapping("/api/book/add")
    public void addBook(@Valid @RequestBody BookDto dto){
        bookService.addBook(dto);
    }

    @PutMapping("/api/book/update/{isbn}")
    public Book updateBook(@PathVariable String isbn, @Valid @RequestBody BookDto dto){
        return bookService.updateBook(isbn, dto);
    }

    @DeleteMapping("/api/book/delete/{isbn}")
    public void deleteBookByISBN(@PathVariable String isbn){
        bookService.deleteBook(isbn);
    }
}
