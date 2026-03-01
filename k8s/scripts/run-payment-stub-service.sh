cd ../charts/payment-stub-service || exit

helm upgrade --install shooter-server-payment-stub-release . -f values.yaml