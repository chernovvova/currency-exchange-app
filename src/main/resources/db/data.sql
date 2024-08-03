INSERT INTO currencies(code, full_name, sign) VALUES ("EUR", "Euro", "€");
INSERT INTO currencies(code, full_name, sign) VALUES ("RUB", "Russian Ruble", "₽");
INSERT INTO currencies(code, full_name, sign) VALUES ("USD", "US Dollar", "$");

INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate) VALUES (3, 2, 80);
INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate) VALUES (1, 2, 90);
INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate) VALUES (1, 3, 1.1);


SELECT * FROM currencies;
SELECT * FROM exchange_rates;