# apolloChanger
ApolloChanger是企业内部给非技术人员使用的配置管理工具，使用者得到登陆凭证后，选择对应的appid下的已授权的key进行更改，发布时会通过钉钉通知到应用负责人，并会对变更产生记录，并且支持配置定时发布

## 钉钉卡片回调处理服务模块

基于Spring Boot与钉钉Stream模式构建的卡片回调处理服务，用于接收并处理钉钉卡片事件（如审批通过/拒绝操作）。

### 功能特性

- 📡 钉钉Stream模式长连接管理
- 🎯 卡片回调事件监听与解析
- 🔄 多状态卡片数据动态更新
- 🔒 用户私有数据存储支持
- 🧩 可扩展的回调处理接口

### 钉钉应用配置

1. 登录[钉钉开放平台](https://open.dingtalk.com/)
2. 创建企业内部应用：
3. 记录以下关键信息：
   ```ini
   dingtalk.appKey = "your_app_key"
   dingtalk.appSecret = "your_app_secret"
4. 创建卡片模板
5. 实例化卡片（参考钉钉三方API调用模块）
6. 启用"卡片回调"权限
7. 运行项目监听&回调

### 关于钉钉回调模块的项目结构
    src/main/java/com/ncf/apolldemo
    ├── config               # 钉钉配置类
    │   └── DingTalkConfig.java
    ├── handler              # 回调处理器类
    │   ├── DefaultCallbackHandler.java
    │   └── CustomCallbackHandler.java
    ├── listener             # 回调监听器
    │   └── DingTalkCallbackListener.java
    ├── pojo
    │   └── model
    │       ├── request      # 请求相关的模型
    │       │   └── CardCallbackRequest.java
    │       └── response     # 响应相关的模型
    │           └── CardCallbackResponse.java
    └── ....(其余功能代码)

### 启动监听长连接
1. 在主启动类中完成钉钉长连接的初始化
    ```ini
    context.getBean(DingTalkConfig.class).initDingTalkConnection();
   
## 钉钉三方API调用模块

### 应用配置
1. 配置项
   ```ini
   dingtalk.cardTemplateId = "your_cardTemplateId"
   dingtalk.callbackType = "STREAM"
   dingtalk.robotCode = "your_robotCode"
   
2. 权限要求
   - 根据手机号获取成员基本信息权限
   - 钉钉群基础信息管理权限
   - 钉钉群基础信息读权限
   - 互动卡片实例写权限
### 相关接口使用方法
   - 相关接口使用方法可参考项目index页面，具体分为六部分这里不再过多赘述
   ![img.png](img.png)

### 关于钉钉API调用模块的项目结构
      src/main/java/com/ncf/apolldemo
      ├── controller               # 控制器层
      │   └── DingTalkController.java （仅建议DEV环境测试使用）
      ├── service                  # 服务层接口
      │   └── DingTalkService.java
      ├── service/impl             # 服务层实现
      │   └── DingTalkServiceImpl.java
      ├── feign                    # Feign 客户端
      │   ├── DingTalkClient.java
      │   └── InitCardInstanceClient.java
      ├── pojo/model               # 数据模型
      │   ├── request              # 请求相关的模型
      │   │   ├── DingTalkCreateChatRequest.java
      │   │   └── initcard
      │   │       ├── BaseDingTalkInitCardRequest.java
      │   │       ├── GroupCardInitRequest.java
      │   │       └── PrivateCardInitRequest.java
      │   └── response             # 响应相关的模型
      │       ├── AccessTokenResponse.java
      │       ├── CardInstanceResponse.java
      │       ├── DingTalkCreateChatResponse.java
      │       ├── DingTalkGetChatResponse.java
      │       └── DingTalkUserResponse.java
      └── ....(其余功能代码)
      ......
      └── resources
          └── static
              └── index.html  接口测试页面（DEV用）

