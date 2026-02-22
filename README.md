# shooter-server

Keycloak для игроков http://localhost:7777/admin/master/console/#/shooter

Kafka UI http://localhost:8090/

Развертывание
Развернуть оба keycloak, в каждом выполнить в реалме master
/opt/keycloak/bin/kcadm.sh config credentials \
--server http://localhost:7777 \
--realm master \
--user admin \
--password admin
/opt/keycloak/bin/kcadm.sh update realms/master -s sslRequired=NONE

Потом выполнить то же самое и в новых реалмах