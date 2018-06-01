# YBStore
## 贵州民族大学易班网薪商城
本商场为学习途中早期版本，用来学习框架、练习所用，后续将会重构此版本。
## 技术选型
- 基于 ssh 框架 
- 使用 redis 缓存
- 前端只用 jquery

## 说明
### 易班验证
当初比较傻，将认证方式使用为参数获取，然后拦截器拦截参数进行解析。建议的应该使用session进行存储。然后进行拦截判断即可
### 关于易班 API
注意我使用的易班开放解密的是存放我本地的 maven 仓库中，并不是通过引入的方式，不过后面我已经将解密方法提取出来，可以脱离易班API（如果不用他为你封装好的工具类的话，反正我没用），上传于 YBApi 中。
## 项目结构
YBStore <br />
├─src                       <br />
│  ├─main                   <br />
│  │  ├─java 源文件          <br />
│  │  │  ├─dao 数据库操作     <br />
│  │  │  │  ├─Base 基类      <br />
│  │  │  │  │  └─impl       <br />
│  │  │  │  └─impl          <br />
│  │  │  ├─entity 实体类     <br />
│  │  │  ├─service 服务类    <br />
│  │  │  │  └─impl 接口      <br />
│  │  │  ├─utils  工具类     <br />
│  │  │  │  └─redis 缓存     <br />
│  │  │  └─web  控制层       <br />
│  │  │      ├─action       <br />
│  │  │      └─interceptor 拦截器 <br />
│  │  ├─resources 配置文件          <br />
│  │  │  ├─properties       <br />
│  │  │  └─xml hibernate配置文件    <br />
│  │  └─webapp              <br />
│  │      ├─css             <br />
│  │      ├─img             <br />
│  │      │  └─products     <br />
│  │      ├─js              <br />
│  │      └─WEB-INF         <br />
│  │          └─lib         <br />
│  └─test                   <br />
│      ├─java               <br />
│      └─resources          <br />
