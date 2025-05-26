minikube start --cpus=2 --memory=4096
minikube addons enable metrics-server
kubectl get deployment metrics-server -n kube-system
minikube ssh
top