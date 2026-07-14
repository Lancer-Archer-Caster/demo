# 本地开发环境

该后端项目的源码和 Lombok 配置面向 **JDK 8**。Maven 仅通过 `source/target=1.8` 指定字节码版本，不能把正在运行 Maven 的 JDK 26 自动降级为 JDK 8，因此启动前必须显式设置 `JAVA_HOME`。

macOS：

```bash
export JAVA_HOME="$(/usr/libexec/java_home -v 1.8)"
export PATH="$JAVA_HOME/bin:$PATH"
java -version
mvn test
```

依赖由 Maven 管理：声明在各模块的 `pom.xml`，全部来自 Maven Central，下载缓存位于 `~/.m2/repository`。这不是项目虚拟环境；缓存可被多个 Java 项目共享。团队或 CI 应使用固定 JDK 8 镜像/工具链。

启动后端：

```bash
mvn spring-boot:run -pl server-demo-main -am
```

默认使用 `server-demo/data/` 下的 H2 文件库；该目录不提交版本库。

前端使用 Node 的项目级依赖：

```bash
cd ../UI2Code
nvm use       # 使用 .nvmrc 指定的 Node 22.12.0
cp .env.example .env
npm ci
npm run dev
```

`node_modules` 不提交；`package-lock.json` 需要提交，以保证所有机器使用相同的前端依赖版本。
