package com.ma.carnet.journal.controller;

import com.ma.carnet.journal.dto.ReadingSheetRequest;
import com.ma.carnet.journal.dto.ReadingSheetWithBookRequest;
import com.ma.carnet.journal.entity.ReadingSheet;
import com.ma.carnet.journal.repository.BookRepository;
import com.ma.carnet.journal.repository.ReadingSheetRepository;
import com.ma.carnet.journal.service.ReadingSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reading-sheets")
@RequiredArgsConstructor
public class ReadingSheetController {

    private final ReadingSheetService readingSheetService;
    private final ReadingSheetRepository readingSheetRepository;
    private final BookRepository bookRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadingSheet>> findByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(readingSheetService.findByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadingSheet> findById(@PathVariable UUID id) {
        return readingSheetRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<ReadingSheet> save(
            @PathVariable UUID userId,
            @PathVariable UUID bookId,
            @RequestBody ReadingSheetRequest request) {
        return ResponseEntity.ok(readingSheetService.save(userId, bookId, request));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<ReadingSheet> saveWithBook(
            @PathVariable UUID userId,
            @RequestBody ReadingSheetWithBookRequest request) {
        return ResponseEntity.ok(readingSheetService.saveWithBook(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadingSheet> update(
            @PathVariable UUID id,
            @RequestBody ReadingSheetWithBookRequest request) {
        return readingSheetRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(request.getStatus());
                    existing.setGrade(request.getGrade());
                    existing.setReview(request.getReview());
                    existing.setQuote(request.getQuote());
                    existing.getBook().setTitle(request.getTitle());
                    existing.getBook().setAuthor(request.getAuthor());
                    existing.getBook().setCover(request.getCover());
                    bookRepository.save(existing.getBook());
                    return ResponseEntity.ok(readingSheetRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}