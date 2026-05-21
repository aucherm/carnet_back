package com.ma.carnet.journal.service;

import com.ma.carnet.journal.entity.Book;
import com.ma.carnet.journal.entity.Favorite;
import com.ma.carnet.journal.entity.FavoriteId;
import com.ma.carnet.journal.entity.User;
import com.ma.carnet.journal.repository.FavoriteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    void findByUser_shouldReturnFavorites() {
        // GIVEN
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setIdUser(userId);

        Book book = new Book();
        book.setTitle("Dune");

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setBook(book);

        when(favoriteRepository.findByUserIdUser(userId)).thenReturn(List.of(favorite));

        // WHEN
        List<Favorite> result = favoriteService.findByUser(userId);

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBook().getTitle()).isEqualTo("Dune");
    }

    @Test
    void addFavorite_shouldSaveFavorite_whenNotAlreadyPresent() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();

        User user = new User();
        user.setIdUser(userId);

        Book book = new Book();
        book.setIdBook(bookId);
        book.setTitle("Dune");

        Favorite favorite = new Favorite();
        favorite.setId(new FavoriteId(userId, bookId));
        favorite.setUser(user);
        favorite.setBook(book);

        when(favoriteRepository.existsByUserIdUserAndBookIdBook(userId, bookId)).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

        // WHEN
        Favorite result = favoriteService.addFavorite(user, book);

        // THEN
        assertThat(result.getBook().getTitle()).isEqualTo("Dune");
    }

    @Test
    void addFavorite_shouldThrowException_whenAlreadyInFavorites() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();

        User user = new User();
        user.setIdUser(userId);

        Book book = new Book();
        book.setIdBook(bookId);

        when(favoriteRepository.existsByUserIdUserAndBookIdBook(userId, bookId)).thenReturn(true);

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> favoriteService.addFavorite(user, book));
    }

    @Test
    void removeFavorite_shouldDelete_whenExists() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();

        when(favoriteRepository.existsByUserIdUserAndBookIdBook(userId, bookId)).thenReturn(true);

        // WHEN
        favoriteService.removeFavorite(userId, bookId);

        // THEN
        verify(favoriteRepository, times(1))
                .deleteByUserIdUserAndBookIdBook(userId, bookId);
    }

    @Test
    void removeFavorite_shouldThrowException_whenNotInFavorites() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();

        when(favoriteRepository.existsByUserIdUserAndBookIdBook(userId, bookId)).thenReturn(false);

        // WHEN & THEN
        assertThrows(RuntimeException.class, () ->
                favoriteService.removeFavorite(userId, bookId)
        );
    }
}