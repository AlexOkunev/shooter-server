helm upgrade --install monitoring prometheus-community/kube-prometheus-stack \
  --namespace monitoring --create-namespace \
  -f ../cluster/monitoring/kube-prometheus-stack/values.yaml