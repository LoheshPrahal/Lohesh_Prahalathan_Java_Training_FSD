package com.libraryManagement.service;

import com.libraryManagement.Exception.ResourceNotFoundException;
import com.libraryManagement.model.Author;
import com.libraryManagement.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getAuthorById(long id){
        Optional<Author> optional = authorRepository.getCustomerById(id);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("Author id invalid");

        return optional.get();
    }
}
