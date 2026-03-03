kubectl -n monitoring create configmap spring-boot-3-dashboard-rest \
  --from-file=../cluster/monitoring/grafana/spring-boot-3-dashboard-rest.json \
  --dry-run=client -o yaml > ../cluster/monitoring/grafana/spring-boot-3-dashboard-rest-configmap.yaml