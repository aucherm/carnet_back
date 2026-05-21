package com.ma.carnet.journal.service;

import com.ma.carnet.journal.entity.*;
import com.ma.carnet.journal.repository.BookRepository;
import com.ma.carnet.journal.repository.ReadingSheetRepository;
import com.ma.carnet.journal.repository.UserRepository;
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
class ReadingSheetServiceTest {

    @Mock
    private ReadingSheetRepository readingSheetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ReadingSheetService readingSheetService;

    // =========================
    // findByUser
    // =========================

    @Test
    void findByUser_shouldReturnSheets_whenUserHasSheets() {
        // GIVEN
        UUID userId = UUID.randomUUID();

        Book book = new Book();
        book.setTitle("Les Misérables");
        book.setAuthor("Victor Hugo");

        User user = new User();
        user.setIdUser(userId);

        ReadingSheet sheet1 = new ReadingSheet();
        sheet1.setUser(user);
        sheet1.setBook(book);
        sheet1.setStatus(ReadingStatus.FINISHED);

        ReadingSheet sheet2 = new ReadingSheet();
        sheet2.setUser(user);
        sheet2.setBook(book);
        sheet2.setStatus(ReadingStatus.READING);

        when(readingSheetRepository.findByUserIdUser(userId))
                .thenReturn(List.of(sheet1, sheet2));

        // WHEN
        List<ReadingSheet> result = readingSheetService.findByUser(userId);

        // THEN
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStatus()).isEqualTo(ReadingStatus.FINISHED);
        assertThat(result.get(1).getStatus()).isEqualTo(ReadingStatus.READING);
    }

    @Test
    void findByUser_shouldReturnEmptyList_whenUserHasNoSheets() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        when(readingSheetRepository.findByUserIdUser(userId))
                .thenReturn(List.of());

        // WHEN
        List<ReadingSheet> result = readingSheetService.findByUser(userId);

        // THEN
        assertThat(result).isEmpty();
    }

    // =========================
    // findByStatus
    // =========================

    @Test
    void findByStatus_shouldReturnOnlySheetsWithGivenStatus() {
        // GIVEN
        UUID userId = UUID.randomUUID();

        Book book = new Book();
        book.setTitle("Dune");
        book.setAuthor("Frank Herbert");

        User user = new User();
        user.setIdUser(userId);

        ReadingSheet sheet = new ReadingSheet();
        sheet.setUser(user);
        sheet.setBook(book);
        sheet.setStatus(ReadingStatus.TO_READ);

        when(readingSheetRepository.findByUserIdUserAndStatus(userId, ReadingStatus.TO_READ))
                .thenReturn(List.of(sheet));

        // WHEN
        List<ReadingSheet> result = readingSheetService.findByStatus(userId, ReadingStatus.TO_READ);

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(ReadingStatus.TO_READ);
    }

    // =========================
    // findToReadByUser
    // =========================

    @Test
    void findToReadByUser_shouldReturnOnlyToReadSheets() {
        // GIVEN
        UUID userId = UUID.randomUUID();

        Book book = new Book();
        book.setTitle("1984");
        book.setAuthor("George Orwell");

        User user = new User();
        user.setIdUser(userId);

        ReadingSheet sheet = new ReadingSheet();
        sheet.setUser(user);
        sheet.setBook(book);
        sheet.setStatus(ReadingStatus.TO_READ);

        when(readingSheetRepository.findByUserIdUserAndStatus(userId, ReadingStatus.TO_READ))
                .thenReturn(List.of(sheet));

        // WHEN
        List<ReadingSheet> result = readingSheetService.findToReadByUser(userId);

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(ReadingStatus.TO_READ);
    }

    // =========================
    // save — erreurs
    // =========================

    @Test
    void save_shouldThrowException_whenUserNotFound() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RuntimeException.class, () ->
                readingSheetService.save(userId, bookId, null)
        );
    }

    @Test
    void save_shouldThrowException_whenBookNotFound() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();

        User user = new User();
        user.setIdUser(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RuntimeException.class, () ->
                readingSheetService.save(userId, bookId, null)
        );
    }
}