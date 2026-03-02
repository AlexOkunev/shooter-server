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

Для локального запуска для работы с кафкой в кубере нужно
export DOCKER_HOST_IP=host.docker.internal

После этого запустить docker
docker-compose up

Для получения токенов нужно сделать
sudo sh -c 'echo "127.0.0.1 host.docker.internal" >> /etc/hosts'

Для доступа через ингресс нужно сделать
sudo sh -c 'echo "127.0.0.1 admin.shooter.local" >> /etc/hosts'
sudo sh -c 'echo "127.0.0.1 player.shooter.local" >> /etc/hosts'

Prometheus
kubectl port-forward -n monitoring svc/monitoring-kube-prometheus-prometheus 9090:9090

Grafana
kubectl port-forward -n monitoring svc/monitoring-grafana 3000:80
kubectl get secret -n monitoring monitoring-grafana -o jsonpath="{.data.admin-password}" | base64 -d; echo
