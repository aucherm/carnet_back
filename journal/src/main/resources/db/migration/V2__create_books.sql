CREATE TABLE books (
                       id_book UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                       isbn    VARCHAR(20)  NOT NULL,
                       title   VARCHAR(255) NOT NULL,
                       author  VARCHAR(255) NOT NULL,
                       cover   VARCHAR(255)
);