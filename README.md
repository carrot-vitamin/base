提供一些常用的工具类，基础服务：
aes加密
base64转码
日期处理工具类
文件处理工具类
HTTP服务
Email服务
Redis


部署命令：
mvn clean deploy -P sonatype-oss-release -Darguments="你的GPG密码"

发布注意：
maven配置setting.xml：
servers模块中添加

    <server>
        <id>sonatype-nexus-snapshots</id> 
        <username>username</username>
        <password>password</password>
    </server> 
    <server>
        <id>sonatype-nexus-staging</id>
        <username>username</username>
        <password>password</password>
    </server>

其中username和password为jira的登录账号和密码；
