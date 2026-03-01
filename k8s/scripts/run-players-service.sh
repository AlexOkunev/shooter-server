cd ../charts/players-service || exit

helm upgrade --install shooter-server-players-release . -f values.yaml