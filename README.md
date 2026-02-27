# shooter-server

Keycloak для игроков http://localhost:7777/admin/master/console/#/shooter-players
Keycloak для администраторов http://localhost:7778/admin/master/console/#/shooter-admin

Kafka UI http://localhost:8090/

Развертывание
Развернуть оба keycloak, в каждом выполнить в реалме master
/opt/keycloak/bin/kcadm.sh config credentials \
--server http://localhost:7777 \
--realm master \
--user admin \
--password admin
/opt/keycloak/bin/kcadm.sh update realms/master -s sslRequired=NONE

Создать реалмы shooter-players и shooter-admin

Выполнить в новых реалмах команды
/opt/keycloak/bin/kcadm.sh update realms/shooter-players -s sslRequired=NONE
/opt/keycloak/bin/kcadm.sh update realms/shooter-admin -s sslRequired=NONE