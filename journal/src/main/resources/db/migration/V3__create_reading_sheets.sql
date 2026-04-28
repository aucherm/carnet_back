CREATE TABLE reading_sheets (
                                id_reading_sheet UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
                                id_user          UUID        NOT NULL,
                                id_book          UUID        NOT NULL,
                                status           VARCHAR(20) DEFAULT 'to_read'
                                    CHECK (status IN ('to_read', 'reading', 'finished')),
                                grade            FLOAT       CHECK (grade >= 0 AND grade <= 5),
                                review           TEXT,
                                quote            TEXT,
                                created_at       TIMESTAMP   DEFAULT NOW(),
                                CONSTRAINT uq_user_book  UNIQUE      (id_user, id_book),
                                CONSTRAINT fk_rs_user    FOREIGN KEY (id_user) REFERENCES users(id_user),
                                CONSTRAINT fk_rs_book    FOREIGN KEY (id_book) REFERENCES books(id_book)
);