CREATE TABLE user_table (
                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                  username VARCHAR(50) UNIQUE NOT NULL,
                  email VARCHAR(50) UNIQUE NOT NULL,
                  password VARCHAR(100) NOT NULL
);

CREATE TABLE recipe_table (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(100) NOT NULL,
                description TEXT
);

CREATE TABLE event_table (
               id BIGINT AUTO_INCREMENT PRIMARY KEY,
               name VARCHAR(100) NOT NULL,
               date DATE NOT NULL,
               user_id BIGINT NOT NULL,
               recipe_id BIGINT NOT NULL,
               FOREIGN KEY (user_id) REFERENCES user_table(id),
               FOREIGN KEY (recipe_id) REFERENCES recipe_table(id)
);