# micro-base
微服务组件之一 基类

## 备注
- java 8
- maven 暂时推荐使用 3.8.X 以下，推荐 3.3.9，高版本 maven ，私服必须支持 https.

## 微服务

| 项目               | 类型                 | 端口   | 地址                                  |
|--------------------|----------------------|--------|---------------------------------------|
| micro-base        | 所有微服务·父工程        | -      | https://github.com/Rock-Ayl/micro-base |
| micro-user        | 用户微服务             | 50501  | https://github.com/Rock-Ayl/micro-user |
| micro-user-api    | 用户微服务·远程调用    | -      | https://github.com/Rock-Ayl/micro-user-api |
| micro-common      | 通用微服务             | 50502  | https://github.com/Rock-Ayl/micro-common |
| micro-common-api  | 通用微服务·远程调用    | -      | https://github.com/Rock-Ayl/micro-common-api |