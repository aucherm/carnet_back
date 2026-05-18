package com.ma.carnet.journal.service;

import com.ma.carnet.journal.dto.ReadingSheetRequest;
import com.ma.carnet.journal.dto.ReadingSheetWithBookRequest;
import com.ma.carnet.journal.entity.Book;
import com.ma.carnet.journal.entity.ReadingSheet;
import com.ma.carnet.journal.entity.ReadingStatus;
import com.ma.carnet.journal.entity.User;
import com.ma.carnet.journal.repository.BookRepository;
import com.ma.carnet.journal.repository.ReadingSheetRepository;
import com.ma.carnet.journal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadingSheetService {

    private final ReadingSheetRepository readingSheetRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // =========================
    // Toutes les fiches d'un user
    // =========================
    public List<ReadingSheet> findByUser(UUID userId) {
        return readingSheetRepository.findByUserIdUser(userId);
    }

    // =========================
    // Fiches par status
    // =========================
    public List<ReadingSheet> findByStatus(
            UUID userId,
            ReadingStatus status
    ) {
        return readingSheetRepository.findByUserIdUserAndStatus(
                userId,
                status
        );
    }

    // =========================
    // Pile à lire
    // =========================
    public List<ReadingSheet> findToReadByUser(UUID userId) {
        return readingSheetRepository.findByUserIdUserAndStatus(
                userId,
                ReadingStatus.TO_READ
        );
    }

    // =========================
    // Création fiche avec livre existant
    // =========================
    public ReadingSheet save(
            UUID userId,
            UUID bookId,
            ReadingSheetRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable")
                );

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Livre introuvable")
                );

        ReadingSheet sheet = new ReadingSheet();

        sheet.setUser(user);
        sheet.setBook(book);

        sheet.setStatus(
                request.getStatus() != null
                        ? ReadingStatus.valueOf(
                        request.getStatus()
                                .trim()
                                .toUpperCase()
                )
                        : ReadingStatus.TO_READ
        );

        sheet.setGrade(request.getGrade());
        sheet.setReview(request.getReview());
        sheet.setQuote(request.getQuote());
        sheet.setCreatedAt(LocalDateTime.now());

        return readingSheetRepository.save(sheet);
    }

    // =========================
    // Création fiche avec nouveau livre
    // =========================
    public ReadingSheet saveWithBook(
            UUID userId,
            ReadingSheetWithBookRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable")
                );

        Book book;

        if (request.getIsbn() != null &&
                !request.getIsbn().isBlank()) {

            book = bookRepository.findByIsbn(request.getIsbn())
                    .orElseGet(() -> {

                        Book newBook = new Book();

                        newBook.setTitle(request.getTitle());
                        newBook.setAuthor(request.getAuthor());
                        newBook.setIsbn(request.getIsbn());
                        newBook.setCover(request.getCover());

                        return bookRepository.save(newBook);
                    });

        } else {

            Book newBook = new Book();

            newBook.setTitle(request.getTitle());
            newBook.setAuthor(request.getAuthor());
            newBook.setIsbn("");
            newBook.setCover(request.getCover());

            book = bookRepository.save(newBook);
        }

        ReadingSheet sheet = new ReadingSheet();

        sheet.setUser(user);
        sheet.setBook(book);

        sheet.setStatus(
                request.getStatus() != null
                        ? ReadingStatus.valueOf(request.getStatus().toUpperCase())
                        : ReadingStatus.TO_READ
        );

        sheet.setGrade(request.getGrade());
        sheet.setReview(request.getReview());
        sheet.setQuote(request.getQuote());
        sheet.setCreatedAt(LocalDateTime.now());

        return readingSheetRepository.save(sheet);
    }

    // =========================
    // Delete
    // =========================
    public void delete(UUID id) {
        readingSheetRepository.deleteById(id);
    }
}