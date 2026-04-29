package com.ma.carnet.journal.controller;

import com.ma.carnet.journal.entity.Book;
import com.ma.carnet.journal.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable UUID id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> findByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.findByTitle(title));
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> findByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.findByAuthor(author));
    }

    @PostMapping
    public ResponseEntity<Book> save(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.save(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable UUID id, @RequestBody Book book) {
        if (bookService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        book.setIdBook(id);
        return ResponseEntity.ok(bookService.save(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (bookService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
