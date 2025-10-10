-- Sample data for chat_history table
INSERT INTO chat_history (user_message, ai_response, created_at) VALUES 
('안녕하세요', '안녕하세요! 무엇을 도와드릴까요?', CURRENT_TIMESTAMP),
('H2 데이터베이스에 대해 알려주세요', 'H2는 자바로 작성된 관계형 데이터베이스입니다.', CURRENT_TIMESTAMP);