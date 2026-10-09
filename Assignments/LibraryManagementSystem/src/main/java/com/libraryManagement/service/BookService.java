package com.libraryManagement.service;

import com.libraryManagement.Exception.ResourceNotFoundException;
import com.libraryManagement.dto.BookInfoDto;
import com.libraryManagement.enums.Genre;
import com.libraryManagement.enums.Status;
import com.libraryManagement.model.Author;
import com.libraryManagement.model.Book;
import com.libraryManagement.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final AuthorService authorService;
    private final BookRepository bookRepository;

    public BookService(AuthorService authorService, BookRepository bookRepository) {
        this.authorService = authorService;
        this.bookRepository = bookRepository;
    }

    public void insert(String title, Genre genre, Status status, int publishedYear, long authorId) {
        Book book = new Book();
        book.setTitle(title);
        book.setGenre(genre);
        book.setStatus(status);
        book.setPublishedYear(publishedYear);

        Author author = authorService.getAuthorById(authorId);

        book.setAuthor(author);

        bookRepository.insertBook(book);
    }

    public Book findById(int id) {
        Optional<Book> optional = bookRepository.findBookById(id);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("Book id invalid");

        return optional.get();
    }

    public List<BookInfoDto> getAllBooks() {
        return bookRepository.getAllBooks();
    }
}
