1. Macos 上端口冲突，修改 customers-service 端口 8081 为 8085
2. 修改 customers、vists、vets 的 docker 容器内端口为 8080
3. 修改 grafana 容器挂载路径，添加 loki、tempo
4. 添加 sonar，运行命令：`mvn clean verify -DskipTests sonar:sonar -Dsonar.login=admin -Dsonar.password=admin`
