package com.ma.carnet.journal.controller;

import com.ma.carnet.journal.dto.ReadingSheetRequest;
import com.ma.carnet.journal.dto.ReadingSheetWithBookRequest;
import com.ma.carnet.journal.entity.ReadingSheet;
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadingSheet>> findByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(readingSheetService.findByUser(userId));
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
}