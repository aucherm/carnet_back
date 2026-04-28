CREATE TABLE favorites (
                           id_user UUID NOT NULL,
                           id_book UUID NOT NULL,
                           PRIMARY KEY (id_user, id_book),
                           CONSTRAINT fk_fav_user FOREIGN KEY (id_user) REFERENCES users(id_user),
                           CONSTRAINT fk_fav_book FOREIGN KEY (id_book) REFERENCES books(id_book)
);