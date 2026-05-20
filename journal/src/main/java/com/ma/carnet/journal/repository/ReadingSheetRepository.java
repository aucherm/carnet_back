package com.ma.carnet.journal.repository;

import com.ma.carnet.journal.entity.ReadingSheet;
import com.ma.carnet.journal.entity.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadingSheetRepository
        extends JpaRepository<ReadingSheet, UUID> {

    List<ReadingSheet> findByUserIdUser(UUID idUser);

    Optional<ReadingSheet> findByUserIdUserAndBookIdBook(
            UUID idUser,
            UUID idBook
    );

    List<ReadingSheet> findByUserIdUserAndStatus(
            UUID idUser,
            ReadingStatus status
    );
}