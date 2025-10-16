-- H2 Database Schema
DROP TABLE IF EXISTS story_pages;
DROP TABLE IF EXISTS stories;
DROP TABLE IF EXISTS chat_history;
DROP TABLE IF EXISTS users;

-- Stories Table
CREATE TABLE stories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    background VARCHAR(255) NOT NULL,
    characters VARCHAR(255) NOT NULL,
    theme VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Story Pages Table
CREATE TABLE story_pages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    story_id BIGINT,
    page_number INTEGER NOT NULL,
    content TEXT,
    image_prompt VARCHAR(1000),
    image_path TEXT,
    choice1 VARCHAR(255),
    choice2 VARCHAR(255),
    choice3 VARCHAR(255),
    choice4 VARCHAR(255),
    selected_choice VARCHAR(255),
    FOREIGN KEY (story_id) REFERENCES stories(id)
);

-- Chat History Table
CREATE TABLE chat_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_message VARCHAR(1000) NOT NULL,
    ai_response VARCHAR(2000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);