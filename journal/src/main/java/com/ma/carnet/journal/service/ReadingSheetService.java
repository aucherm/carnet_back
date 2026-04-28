package com.ma.carnet.journal.service;

import com.ma.carnet.journal.entity.ReadingSheet;
import com.ma.carnet.journal.entity.ReadingStatus;
import com.ma.carnet.journal.repository.ReadingSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadingSheetService {

    private final ReadingSheetRepository readingSheetRepository;

    public List<ReadingSheet> findByUser(UUID idUser) {
        return readingSheetRepository.findByUserIdUser(idUser);
    }

    public Optional<ReadingSheet> findByUserAndBook(UUID idUser, UUID idBook) {
        return readingSheetRepository.findByUserIdUserAndBookIdBook(idUser, idBook);
    }

    public List<ReadingSheet> findByUserAndStatus(UUID idUser, ReadingStatus status) {
        return readingSheetRepository.findByUserIdUserAndStatus(idUser, status);
    }

    public ReadingSheet save(ReadingSheet readingSheet) {
        if (readingSheetRepository.findByUserIdUserAndBookIdBook(
                readingSheet.getUser().getIdUser(),
                readingSheet.getBook().getIdBook()).isPresent()) {
            throw new RuntimeException("Une fiche existe déjà pour ce livre");
        }
        return readingSheetRepository.save(readingSheet);
    }

    public ReadingSheet update(ReadingSheet readingSheet) {
        return readingSheetRepository.save(readingSheet);
    }

    public void delete(UUID id) {
        readingSheetRepository.deleteById(id);
    }
}