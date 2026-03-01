cd ../charts/api-gateway-admin || exit

helm upgrade --install shooter-server-api-gateway-admin-release . -f values.yaml