package com.ma.carnet.journal.controller;

import com.ma.carnet.journal.entity.ReadingSheet;
import com.ma.carnet.journal.entity.ReadingStatus;
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

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<ReadingSheet>> findByUser(@PathVariable UUID idUser) {
        return ResponseEntity.ok(readingSheetService.findByUser(idUser));
    }

    @GetMapping("/user/{idUser}/book/{idBook}")
    public ResponseEntity<ReadingSheet> findByUserAndBook(
            @PathVariable UUID idUser,
            @PathVariable UUID idBook) {
        return readingSheetService.findByUserAndBook(idUser, idBook)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{idUser}/status/{status}")
    public ResponseEntity<List<ReadingSheet>> findByUserAndStatus(
            @PathVariable UUID idUser,
            @PathVariable ReadingStatus status) {
        return ResponseEntity.ok(readingSheetService.findByUserAndStatus(idUser, status));
    }

    @PostMapping
    public ResponseEntity<ReadingSheet> save(@RequestBody ReadingSheet readingSheet) {
        return ResponseEntity.ok(readingSheetService.save(readingSheet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadingSheet> update(
            @PathVariable UUID id,
            @RequestBody ReadingSheet readingSheet) {
        readingSheet.setIdReadingSheet(id);
        return ResponseEntity.ok(readingSheetService.update(readingSheet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        readingSheetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}