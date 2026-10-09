package com.libraryManagement.main;

import com.libraryManagement.config.AppConfig;
import com.libraryManagement.dto.BookInfoDto;
import com.libraryManagement.enums.Genre;
import com.libraryManagement.enums.Status;
import com.libraryManagement.model.Book;
import com.libraryManagement.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class App {
    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        BookService bookService = context.getBean(BookService.class);

//        bookService.insert("Harry Potter and the Philosopher''s Stone", Genre.FICTION, Status.AVAILABLE, 1997, 2);
//        bookService.insert("Norwegian Wood", Genre.NON_FICTION, Status.BORROWED, 1987, 2);

        Book book = bookService.findById(1);
//        System.out.println(book);


        List<BookInfoDto> allBooks = bookService.getAllBooks();

        allBooks.forEach(System.out::println);
    }
}
