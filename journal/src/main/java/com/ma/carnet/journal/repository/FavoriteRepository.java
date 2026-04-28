package com.ma.carnet.journal.repository;

import com.ma.carnet.journal.entity.Favorite;
import com.ma.carnet.journal.entity.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    List<Favorite> findByUserIdUser(UUID idUser);
    boolean existsByUserIdUserAndBookIdBook(UUID idUser, UUID idBook);
    void deleteByUserIdUserAndBookIdBook(UUID idUser, UUID idBook);
}