提供一些常用的工具类，基础服务：
aes加密
base64转码
日期处理工具类
文件处理工具类
HTTP服务
Email服务
Redis


发布命令：
mvn clean deploy -P release

发布注意：
maven配置setting.xml：
servers模块中添加

    <server>
      <id></id>
      <username></username>
      <password></password>
    </server>

其中username和password为jira的登录账号和密码；
id可以随意填写，但一定要与pom.xml中

    <distributionManagement>
        <snapshotRepository>
            <id></id>
            <url></url>
        </snapshotRepository>
        <repository>
            <id></id>
            <url></url>
        </repository>
    </distributionManagement>
    
里的id保持一致！！！
