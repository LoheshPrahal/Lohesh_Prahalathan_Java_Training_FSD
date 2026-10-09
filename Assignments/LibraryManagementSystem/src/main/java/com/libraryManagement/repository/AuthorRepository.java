package com.libraryManagement.repository;

import com.libraryManagement.model.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public Optional<Author> getCustomerById(long authorId) {
        return Optional
                .ofNullable(entityManager
                        .find(Author.class, authorId));
    }
}
