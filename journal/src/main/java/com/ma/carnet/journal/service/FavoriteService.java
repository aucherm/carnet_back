package com.ma.carnet.journal.service;

import com.ma.carnet.journal.entity.Favorite;
import com.ma.carnet.journal.entity.FavoriteId;
import com.ma.carnet.journal.entity.User;
import com.ma.carnet.journal.entity.Book;
import com.ma.carnet.journal.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public List<Favorite> findByUser(UUID idUser) {
        return favoriteRepository.findByUserIdUser(idUser);
    }

    public Favorite addFavorite(User user, Book book) {
        if (favoriteRepository.existsByUserIdUserAndBookIdBook(
                user.getIdUser(), book.getIdBook())) {
            throw new RuntimeException("Ce livre est déjà dans les favoris");
        }
        Favorite favorite = new Favorite();
        favorite.setId(new FavoriteId(user.getIdUser(), book.getIdBook()));
        favorite.setUser(user);
        favorite.setBook(book);
        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(UUID idUser, UUID idBook) {
        if (!favoriteRepository.existsByUserIdUserAndBookIdBook(idUser, idBook)) {
            throw new RuntimeException("Ce livre n'est pas dans les favoris");
        }
        favoriteRepository.deleteByUserIdUserAndBookIdBook(idUser, idBook);
    }
}