cd ../charts/market-service || exit

helm upgrade --install shooter-server-market-release . -f values.yaml