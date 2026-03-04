# Admin Gateway

## Equipment / Ammunition
### Create
Подается 1000 rps в течение 50 секунд
![](./screenshots/ammunition_create.png)

## Equipment / Attachment
### Create
Подается 1000 rps в течение 25 секунд для создания демпферов.
Параллельно подается 1000 rps в течение 25 секунд для создания глушителей.
![](./screenshots/attachment_create.png)

## Equipment / Grenade
### Create
Подается 500 rps в течение 100 секунд
![](./screenshots/grenade_create.png)

## Equipment / Currency
### Create
Подается 750 rps в течение 70 секунд
![](./screenshots/currency_create_1.png)
После создания валют произошла сборка мусора
![](./screenshots/currency_create_2.png)

## Equipment / Gun
### Create
Подается 400 rps в течение 60 секунд для создания пистолетов.
Параллельно подается 700 rps в течение 35 секунд для создания автоматов. 
Затем выполняется сборка мусора
![](./screenshots/gun_create_1.png)
![](./screenshots/gun_create_2.png)
![](./screenshots/gun_create_3.png)

## Players 
### Search
Подается 200 rps в течение 90 секунд. Происходит чтение случайной страницы справочника. Страница 100 записей.
Тест выполняется с меньшей нагрузкой на машину, в особенности на жесткий диск.
![](./screenshots/players_search.png)
[//]: # (TODO анализировать низкую производительность)

## Market / Money bundle
### Create
Подается 400 rps в течение 130 секунд
![](./screenshots/money_bundle_create.png)

## Market / Account
### Give currency
Подается 500 rps в течение 200 секунд
![](./screenshots/market_currency_give.png)

## Inventory
### Give equipment
Подается 500 rps в течение 300 секунд
![](./screenshots/inventory_give_equipment.png)

# Player Gateway

## Equipment / Ammunition
### Search
Подается 1000 rps в течение 50 секунд. Происходит чтение случайной страницы справочника. Страница 100 записей.
Затем выполняется сборка мусора
![](./screenshots/ammunition_search_1.png)
![](./screenshots/ammunition_search_2.png)

## Equipment / Attachment
### Search
Подается 500 rps в течение 60 секунд. Происходит чтение случайной страницы справочника. Страница 100 записей.
![](./screenshots/attachment_search.png)

## Equipment / Gun
### Search
Подается 300 rps в течение 90 секунд. Происходит чтение случайной страницы справочника. Страница 100 записей.
![](./screenshots/gun_search.png)

## Equipment / Grenade
### Search
Подается 400 rps в течение 90 секунд. Происходит чтение случайной страницы справочника. Страница 100 записей.
![](./screenshots/grenade_search.png)

## Equipment / Currency
### Search
Подается 350 rps в течение 90 секунд. Происходит чтение случайной страницы справочника. Страница 100 записей.
![](./screenshots/currency_search.png)

## Market / Product trade
### Create
Подается 70 rps в течение 300 секунд. Большее количество параллельных покупок приводит к исключениям с оптимистичной блокировкой в БД.
![](./screenshots/product_trade_create_1.png)
Ошибки:
![](./screenshots/product_trade_create_2.png)
Сборка мусора:
![](./screenshots/product_trade_create_3.png)
[//]: # (TODO исследовать эту проблему)

## Market / Account
### Read
Подается 400 rps в течение 90 секунд. Происходит чтение страницы. Страница 100 записей.
![](./screenshots/product_trade_create_1.png)

## Inventory / Inventory
### Read
Подается 350 rps в течение 120 секунд. Происходит чтение страницы. Страница 100 записей.
Процессор загружен на 50%
[//]: # (TODO подумать насчет одновременными запросами к справочнику, если в кеше нет нужного значения)
![](./screenshots/inventory_read.png)

# Тесты Player Gateway с выключенным кэшированием данных справочника
## Inventory / Inventory
### Read
Подается 350 rps в течение 120 секунд. Происходит чтение страницы. Страница 100 записей.
Максимальная загрузка процессора 55%. Активная сборка мусора.
![](./screenshots/inventory_read_no_cache_1.png)
![](./screenshots/inventory_read_no_cache_2.png)

# Тест с низким RPS в rate limiter
В rate limiter установлено ограничение 10 RPS, подается 15 RPS в течение 120 секунд.
## Inventory / Inventory
### Read
![](./screenshots/inventory_read_low_rps.png)