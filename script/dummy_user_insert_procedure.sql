CREATE PROCEDURE hhplus.insert_sample_users_and_balances(IN user_count INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE inserted_user_id BIGINT;

    WHILE i <= user_count DO
        -- User 생성
        INSERT INTO user (user_name)
        VALUES (CONCAT('TestUser_', i));

        -- 방금 삽입한 User의 id 가져오기 (auto_increment id)
        SET inserted_user_id = LAST_INSERT_ID();

        -- Balance 생성 (초기 point는 1,000,000)
INSERT INTO balance (user_id, point, version)
VALUES (inserted_user_id, 1000000, 0);

SET i = i + 1;
END WHILE;
END