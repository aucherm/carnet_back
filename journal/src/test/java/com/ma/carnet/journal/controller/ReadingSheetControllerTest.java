package com.ma.carnet.journal.controller;

import com.ma.carnet.journal.entity.Book;
import com.ma.carnet.journal.entity.ReadingSheet;
import com.ma.carnet.journal.entity.ReadingStatus;
import com.ma.carnet.journal.entity.User;
import com.ma.carnet.journal.repository.BookRepository;
import com.ma.carnet.journal.repository.ReadingSheetRepository;
import com.ma.carnet.journal.service.ReadingSheetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadingSheetControllerTest {

    @Mock
    private ReadingSheetService readingSheetService;

    @Mock
    private ReadingSheetRepository readingSheetRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ReadingSheetController readingSheetController;

    private ReadingSheet createSheet(UUID userId) {
        User user = new User();
        user.setIdUser(userId);

        Book book = new Book();
        book.setTitle("Dune");
        book.setAuthor("Frank Herbert");

        ReadingSheet sheet = new ReadingSheet();
        sheet.setIdReadingSheet(UUID.randomUUID());
        sheet.setUser(user);
        sheet.setBook(book);
        sheet.setStatus(ReadingStatus.TO_READ);
        return sheet;
    }

    @Test
    void findByUser_shouldReturn200_withSheets() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        ReadingSheet sheet = createSheet(userId);

        when(readingSheetService.findByUser(userId)).thenReturn(List.of(sheet));

        // WHEN
        ResponseEntity<List<ReadingSheet>> response = readingSheetController.findByUser(userId);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void findById_shouldReturn200_whenExists() {
        // GIVEN
        UUID id = UUID.randomUUID();
        ReadingSheet sheet = createSheet(UUID.randomUUID());
        sheet.setIdReadingSheet(id);

        when(readingSheetRepository.findById(id)).thenReturn(Optional.of(sheet));

        // WHEN
        ResponseEntity<ReadingSheet> response = readingSheetController.findById(id);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findById_shouldReturn404_whenNotFound() {
        // GIVEN
        UUID id = UUID.randomUUID();
        when(readingSheetRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        ResponseEntity<ReadingSheet> response = readingSheetController.findById(id);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void delete_shouldReturn204_whenExists() {
        // GIVEN
        UUID id = UUID.randomUUID();
        ReadingSheet sheet = createSheet(UUID.randomUUID());

        when(readingSheetRepository.findById(id)).thenReturn(Optional.of(sheet));

        // WHEN
        ResponseEntity<Void> response = readingSheetController.delete(id);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(readingSheetService, times(1)).delete(id);
    }

    @Test
    void delete_shouldReturn404_whenNotFound() {
        // GIVEN
        UUID id = UUID.randomUUID();
        when(readingSheetRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        ResponseEntity<Void> response = readingSheetController.delete(id);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void getToReadBooks_shouldReturn200_withToReadSheets() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        ReadingSheet sheet = createSheet(userId);

        when(readingSheetService.findToReadByUser(userId)).thenReturn(List.of(sheet));

        // WHEN
        ResponseEntity<List<ReadingSheet>> response = readingSheetController.getToReadBooks(userId);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void getByStatus_shouldReturn200_withFilteredSheets() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        ReadingSheet sheet = createSheet(userId);

        when(readingSheetService.findByStatus(userId, ReadingStatus.TO_READ))
                .thenReturn(List.of(sheet));

        // WHEN
        ResponseEntity<List<ReadingSheet>> response =
                readingSheetController.getByStatus(userId, "TO_READ");

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
    }
}