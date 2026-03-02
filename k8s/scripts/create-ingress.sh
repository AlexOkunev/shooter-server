cd ../cluster/nginx-ingress || exit
kubectl apply -f shooter-admin-ingress.yaml
kubectl apply -f shooter-player-ingress.yaml