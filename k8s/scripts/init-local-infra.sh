cd ../charts/local-infra || exit

helm upgrade --install local-infra-release . \
   -f values.yaml