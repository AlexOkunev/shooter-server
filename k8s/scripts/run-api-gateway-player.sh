cd ../charts/api-gateway-player || exit

helm upgrade --install shooter-server-api-gateway-player-release . -f values.yaml