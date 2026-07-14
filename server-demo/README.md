# JoyCue 本地后端

该后端已经改造成可离线于公司运行环境的标准 Spring Boot 2.7 应用。运行时不再调用 JMF/DongBoot、DUCC、JSF、DongDAL、Guardian 或公司上下文服务。

## 本地替代关系

| 原能力 | 本地实现 |
| --- | --- |
| JMF / DongBoot 启动与父 POM | Spring Boot 2.7.18 |
| DUCC 动态配置 | `application.properties` + 环境变量 |
| JSF RPC 示例 | 同进程 Gateway + H2 查询，保留旧 HTTP 路径兼容前端 |
| DongDAL / MySQL | MyBatis + 本地 H2 文件库 |
| DongThread | Java 线程池 + `ThreadPoolTaskExecutor` |
| Guardian | 本地直通实现；生产级限流不在 demo 范围内 |
| 公司健康检查 | Spring Boot Actuator |
| 公司全局上下文 | HTTP `X-Request-Id` 或本地 UUID |

## 启动

需要 JDK 8 和 Maven：

```bash
cd server-demo
export JAVA_HOME="$(/usr/libexec/java_home -v 1.8)"
export PATH="$JAVA_HOME/bin:$PATH"
mvn spring-boot:run -pl server-demo-main -am
```

验证：

```bash
curl http://localhost:8080/order/hello
curl http://localhost:8080/order/getfromdb?id=demo
curl http://localhost:8080/order/getfromrpc?id=demo
curl http://localhost:8080/order/duccget
curl http://localhost:8080/actuator/health
```

数据库默认持久化到 `server-demo/data/joycue.mv.db`，首次启动由 `schema.sql` 和 `data.sql` 自动初始化。H2 控制台地址为 `http://localhost:8080/h2-console`，JDBC URL 使用 `jdbc:h2:file:./data/joycue`，用户名 `sa`，密码为空。

本地配置可直接编辑 `server-demo-main/src/main/resources/application.properties`。例如：

```bash
DEMO_MESSAGE="培训演示环境" mvn spring-boot:run -pl server-demo-main -am
```

执行全部测试：

```bash
mvn test
```

项目架构、数据契约与算法占位能力见根目录的 `ARCHITECTURE.md`、`docs/DATA_CONTRACTS.md`、`docs/ALGORITHM_REQUIREMENTS.md`。
