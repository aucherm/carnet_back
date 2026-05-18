package com.ma.carnet.journal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reading_sheets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_reading_sheet")
    private UUID idReadingSheet;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_book", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ReadingStatus status = ReadingStatus.TO_READ;

    private Float grade;

    @Column(columnDefinition = "TEXT")
    private String review;

    @Column(columnDefinition = "TEXT")
    private String quote;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}