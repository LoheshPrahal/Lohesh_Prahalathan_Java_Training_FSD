package com.springboot.librarymanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(nullable = false)
    String title;
    @Column(nullable = false)
    String authorName;
    @Column(nullable = false, unique = true, length = 13)
    String isbn;

    @Max(value = 2026, message = "Publication year cannot be in the future!")
    @Column(nullable = false)
    int pubicationYear;

}
