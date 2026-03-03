kubectl -n monitoring create configmap spring-boot-3-dashboard-grpc \
  --from-file=../cluster/monitoring/grafana/spring-boot-3-dashboard-grpc.json \
  --dry-run=client -o yaml > ../cluster/monitoring/grafana/spring-boot-3-dashboard-grpc-configmap.yaml