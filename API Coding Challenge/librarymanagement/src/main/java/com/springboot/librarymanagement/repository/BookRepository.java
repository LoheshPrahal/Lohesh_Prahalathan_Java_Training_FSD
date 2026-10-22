package com.springboot.librarymanagement.repository;

import com.springboot.librarymanagement.model.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b from Book b where b.isbn = ?1")
    Book getBookByISBN(String isbn);

    @Modifying
    @Query("delete from Book b where b.isbn = ?1")
    void deleteBookByIsbn(String isbn);
}
