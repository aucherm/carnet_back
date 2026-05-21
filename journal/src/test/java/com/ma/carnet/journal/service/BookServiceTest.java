package com.ma.carnet.journal.service;

import com.ma.carnet.journal.entity.Book;
import com.ma.carnet.journal.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void findAll_shouldReturnAllBooks() {
        // GIVEN
        Book book1 = new Book();
        book1.setTitle("Dune");
        book1.setAuthor("Frank Herbert");

        Book book2 = new Book();
        book2.setTitle("1984");
        book2.setAuthor("George Orwell");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // WHEN
        List<Book> result = bookService.findAll();

        // THEN
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Dune");
        assertThat(result.get(1).getTitle()).isEqualTo("1984");
    }

    @Test
    void findByIsbn_shouldReturnBook_whenExists() {
        // GIVEN
        Book book = new Book();
        book.setIsbn("978-2-07-036024-5");
        book.setTitle("Dune");

        when(bookRepository.findByIsbn("978-2-07-036024-5")).thenReturn(Optional.of(book));

        // WHEN
        Optional<Book> result = bookService.findByIsbn("978-2-07-036024-5");

        // THEN
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Dune");
    }

    @Test
    void findByTitle_shouldReturnMatchingBooks() {
        // GIVEN
        Book book = new Book();
        book.setTitle("Dune");

        when(bookRepository.findByTitleContainingIgnoreCase("dune")).thenReturn(List.of(book));

        // WHEN
        List<Book> result = bookService.findByTitle("dune");

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Dune");
    }

    @Test
    void findByAuthor_shouldReturnMatchingBooks() {
        // GIVEN
        Book book = new Book();
        book.setAuthor("Frank Herbert");

        when(bookRepository.findByAuthorContainingIgnoreCase("herbert")).thenReturn(List.of(book));

        // WHEN
        List<Book> result = bookService.findByAuthor("herbert");

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo("Frank Herbert");
    }

    @Test
    void save_shouldThrowException_whenIsbnAlreadyExists() {
        // GIVEN
        Book book = new Book();
        book.setIsbn("978-2-07-036024-5");
        book.setTitle("Dune");

        Book existing = new Book();
        existing.setIdBook(UUID.randomUUID());
        existing.setIsbn("978-2-07-036024-5");

        when(bookRepository.findByIsbn("978-2-07-036024-5")).thenReturn(Optional.of(existing));

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> bookService.save(book));
    }

    @Test
    void save_shouldSaveBook_whenIsbnIsNew() {
        // GIVEN
        Book book = new Book();
        book.setIsbn("978-2-07-036024-5");
        book.setTitle("Dune");

        when(bookRepository.findByIsbn("978-2-07-036024-5")).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        // WHEN
        Book result = bookService.save(book);

        // THEN
        assertThat(result.getTitle()).isEqualTo("Dune");
    }
}