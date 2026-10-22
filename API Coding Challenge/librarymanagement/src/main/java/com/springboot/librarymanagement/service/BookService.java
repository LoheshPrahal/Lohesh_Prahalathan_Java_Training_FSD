package com.springboot.librarymanagement.service;

import com.springboot.librarymanagement.dto.request.BookDto;
import com.springboot.librarymanagement.dto.response.BookRespDto;
import com.springboot.librarymanagement.exception.InvalidCallException;
import com.springboot.librarymanagement.exception.ResourceNotFoundException;
import com.springboot.librarymanagement.mapper.BookMapper;
import com.springboot.librarymanagement.model.Book;
import com.springboot.librarymanagement.repository.BookRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<BookRespDto> getAllBooks() {
        List<Book> list = bookRepository.findAll();
        return list.stream().map(BookMapper :: convertEntityToDto).toList();
    }

    public BookRespDto getBookByISBN(String isbn) {
        return BookMapper.convertEntityToDto(bookRepository.getBookByISBN(isbn));
    }

    public void addBook(@Valid BookDto dto) {
        Book book = BookMapper.convertDtoToEntity(dto);
        bookRepository.save(book);
    }

    public Book updateBook(String isbn, @Valid BookDto dto) {
        Book book = bookRepository.getBookByISBN(isbn);
        if(book == null) throw new ResourceNotFoundException("Book not found in db!");
        boolean flag = false;

        if(!(dto.title() == null) && !(dto.title().isEmpty())){
            book.setTitle(dto.title());
            flag = true;
        }

        if(!(dto.author() == null) && !(dto.author().isEmpty())){
            book.setAuthorName(dto.author());
            flag = true;
        }

        if(!flag) throw new InvalidCallException("Update operation terminated!");

        return bookRepository.save(book);
    }

    public void deleteBook(String isbn) {
        Book book = bookRepository.getBookByISBN(isbn);
        if(book == null) throw new ResourceNotFoundException("Book not found in db!");

        bookRepository.deleteBookByIsbn(isbn);
    }
}
