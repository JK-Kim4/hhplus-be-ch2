CREATE PROCEDURE `hhplus`.`insert_test_products`(IN product_count INT)
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= product_count DO
        INSERT INTO product (name, price, stock)
        VALUES (
            CONCAT('Test Product ', i),
            10000,
            999999 								## 충분한 재고량 설정
        );
        SET i = i + 1;
END WHILE;
END