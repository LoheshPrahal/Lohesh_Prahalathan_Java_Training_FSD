package com.springboot.librarymanagement.mapper;

import com.springboot.librarymanagement.dto.request.BookDto;
import com.springboot.librarymanagement.dto.response.BookRespDto;
import com.springboot.librarymanagement.model.Book;

public class BookMapper {
    public static Book convertDtoToEntity(BookDto dto){
        Book book = new Book();
        book.setTitle(dto.title());
        book.setAuthorName(dto.author());
        book.setIsbn(dto.isbn());
        book.setPubicationYear(dto.publicationYear());
        return book;
    }

    public static BookRespDto convertEntityToDto(Book book ){
        return new BookRespDto(
                book.getId(),
                book.getTitle(),
                book.getAuthorName(),
                book.getIsbn(),
                book.getPubicationYear()
        );
    }
}
