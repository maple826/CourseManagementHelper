# 课程管理助手
## 开发环境
* JDK1.8
* IntelliJ IDEA 2021.3.1 (Ultimate Edition)
## 功能介绍
* 用户注册、登录，保存用户课程信息
* 每个用户登录后，按课程进行分类，管理每个课程的ddl、资料
## 其他
用户信息存储在`./data`下， 以用户user，课程coure为例：
* `./data/user/pwd.txt`存储用户密码信息
* `./data/user/course`存储课程course信息
    * `./data/user/course/ddl`存储ddl信息
    * `./data/user/course/资源`存储课程资源信息
