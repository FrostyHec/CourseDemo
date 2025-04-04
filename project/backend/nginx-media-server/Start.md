1. 执行下面步骤创建docker
docker build -t nginx-streaming-server .

2. 执行下面步骤运行docker,创建完后默认就在启动状态
docker run -d -p 8088:8088 -p 3935:1935 --name streaming-server nginx-streaming-server

3. 执行下面步骤运行docker
docker start streaming-server

4. 下面步骤停止docker 
docker stop streaming-server

5. 下面步骤删除docker
docker rm streaming-server


访问
http://localhost:1935/${appname}/${streamname}
http://localhost:8088/live?app=live&stream=s1