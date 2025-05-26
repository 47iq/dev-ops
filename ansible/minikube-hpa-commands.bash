minikube start --cpus=2 --memory=4096
minikube addons enable metrics-server
kubectl get deployment metrics-server -n kube-system
minikube ssh
top

curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm install prometheus prometheus-community/kube-prometheus-stack --set prometheus.prometheusSpec.maximumStartupDurationSeconds=300

kubectl port-forward <pod> <port>:<port>
kubectl get secret prometheus-grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo

