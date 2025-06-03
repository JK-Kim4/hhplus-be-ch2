CREATE PROCEDURE `hhplus`.`insert_test_products`(IN product_count INT)
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= product_count DO
        INSERT INTO product (name, price, stock)
        VALUES (
            CONCAT('Test Product ', i),
            ROUND(100 + (RAND() * 49900)),
            999999
        );
        SET i = i + 1;
END WHILE;
END