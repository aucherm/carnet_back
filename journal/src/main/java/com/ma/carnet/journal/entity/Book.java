package com.ma.carnet.journal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_book")
    private UUID idBook;

    @Column(nullable = false, length = 20)
    private String isbn;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "author", nullable = false, length = 255)
    private String author;

    @Column(length = 255)
    private String cover;

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<ReadingSheet> readingSheets;

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<Favorite> favorites;
}
