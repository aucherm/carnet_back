package com.ma.carnet.journal.controller;

import com.ma.carnet.journal.entity.Favorite;
import com.ma.carnet.journal.entity.Book;
import com.ma.carnet.journal.entity.User;
import com.ma.carnet.journal.service.FavoriteService;
import com.ma.carnet.journal.service.UserService;
import com.ma.carnet.journal.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;
    private final BookService bookService;

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<Favorite>> findByUser(@PathVariable UUID idUser) {
        return ResponseEntity.ok(favoriteService.findByUser(idUser));
    }

    @PostMapping("/user/{idUser}/book/{idBook}")
    public ResponseEntity<Favorite> addFavorite(
            @PathVariable UUID idUser,
            @PathVariable UUID idBook) {
        User user = userService.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Book book = bookService.findById(idBook)
                .orElseThrow(() -> new RuntimeException("Livre introuvable"));
        return ResponseEntity.ok(favoriteService.addFavorite(user, book));
    }

    @DeleteMapping("/user/{idUser}/book/{idBook}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable UUID idUser,
            @PathVariable UUID idBook) {
        favoriteService.removeFavorite(idUser, idBook);
        return ResponseEntity.noContent().build();
    }
}
