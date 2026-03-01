cd ../charts/inventory-service || exit

helm upgrade --install shooter-server-inventory-release . -f values.yaml