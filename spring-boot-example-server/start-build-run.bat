cd /d %~dp0
set startDir=%cd%
echo %startDir%


rem ���ɾ���ͷ���
docker build -t spring-boot-example-server:1.0 .
docker run -itd -p 8080:8080 -e TZ="Asia/Shanghai"   --name spring-boot-example-server   spring-boot-example-server:1.0

pause