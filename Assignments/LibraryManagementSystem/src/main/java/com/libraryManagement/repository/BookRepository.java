package com.libraryManagement.repository;

import com.libraryManagement.dto.BookInfoDto;
import com.libraryManagement.model.Author;
import com.libraryManagement.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class BookRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void insertBook(Book book){
        entityManager.persist(book);
    }


    public Optional<Book> findBookById(long bookId) {
        return Optional
                .ofNullable(entityManager
                        .find(Book.class, bookId));
    }
    public List<BookInfoDto> getAllBooks() {
        String jpql = """
        SELECT new com.libraryManagement.dto.BookInfoDto(
            b.id,
            b.title,
            b.genre,
            b.status,
            a.name
        )
        FROM Book b
        JOIN b.author a
        """;

        return entityManager
                .createQuery(jpql, BookInfoDto.class)
                .getResultList();
    }
}
