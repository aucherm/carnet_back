CREATE TABLE users (
                       id_user    UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                       first_name VARCHAR(100) NOT NULL,
                       last_name  VARCHAR(100) NOT NULL,
                       mail       VARCHAR(255) UNIQUE NOT NULL,
                       password   VARCHAR(255) NOT NULL,
                       role       VARCHAR(50)  NOT NULL
);