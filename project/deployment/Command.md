
============================course, langchain

docker build -t ooad-server:latest . 

docker tag ooad-server:latest frosky/ooad-server:latest

docker build -t ooad-server:latest .

docker push frosky/ooad-server:latest


==========================Object Storage

docker build -t ooad-object-storage:latest .

docker tag ooad-object-storage:latest frosky/ooad-object-storage:latest

docker build -t ooad-object-storage:latest .

docker push frosky/ooad-object-storage:latest

==========================NGINX MEDIA SERVER(only accept one)

docker build -t ooad-nginx-media-server:latest .

docker tag ooad-nginx-media-server:latest frosky/ooad-object-storage:latest

docker build -t ooad-object-storage:latest .

docker push frosky/ooad-object-storage:latest

============================Auth

docker build -t ooad-auth-server:latest .

docker tag ooad-auth-server:latest frosky/ooad-auth-server:latest

docker build -t ooad-auth-server:latest .

docker push frosky/ooad-auth-server:latest

================================================================================
kubectl apply -f auth-service.yaml
kubectl apply -f ingress.yaml 

