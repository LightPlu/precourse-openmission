CREATE TABLE round (
                       id SERIAL PRIMARY KEY,
                       round_number INT NOT NULL UNIQUE
);

CREATE TABLE lotto_ticket (
                              id SERIAL PRIMARY KEY,
                              round_id INT NOT NULL,
                              numbers TEXT NOT NULL
);

CREATE TABLE winning_lotto_numbers (
                                 id SERIAL PRIMARY KEY,
                                 round_id INT NOT NULL,
                                 numbers TEXT NOT NULL,
                                 bonus_number INT NOT NULL
);

CREATE TABLE round_result (
                              id SERIAL PRIMARY KEY,
                              round_id INT NOT NULL,
                              first INT NOT NULL,
                              second INT NOT NULL,
                              third INT NOT NULL,
                              fourth INT NOT NULL,
                              fifth INT NOT NULL,
                              miss INT NOT NULL
);
