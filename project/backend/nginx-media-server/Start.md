
docker build -t nginx-streaming-server .


docker run -d -p 8088:8088 -p 1935:1935 --name streaming-server nginx-streaming-server


docker rm streaming-server
docker stop streaming-server
访问
http://localhost:1935/${appname}/${streamname}
http://localhost:8088/live?app=live&stream=s1