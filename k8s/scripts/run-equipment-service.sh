cd ../charts/equipment-service || exit

helm upgrade --install shooter-server-equipment-release . -f values.yaml