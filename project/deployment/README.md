主要放部署的配置文件，比如k8s的

docker run -p 9000:9000 -p 9001:9001 --name minio -v .\minio\data:/data -v .\minio\config:/root/.minio -d minio/minio server /data --console-address “:9000” --address “:9001”

docker restart minio

docker stop minio

docker rm minio

dokcer ps -a