### 1.介绍
**st-encrypt-sdk** 

Spring Boot接口加密，可以对返回值、参数值通过注解的方式自动加解密


调用端发送的请求信息
```
{
  sign:md5hex(随机AES_KEY###时间搓t###appId###授权auth),使用###分隔，其中appId,授权auth可以为null
  key:RSA公钥加密（随机AES_KEY###时间搓t###appId###授权auth）,使用###分隔，其中appId,授权auth可以为null
  data:AES 加密（data数据）
}
```

----
- 1.AES加密内容效率远高于RSA,并且拥有一定的安全性。
- 2.rsa公钥加密了key，劫持者无法获取key和相关授权信息，既保证保障data的安全和又保证了授权信息不会被盗用。
- 3.有appId和auth的存在，保障了劫持者无法伪造内容。

服务器端  
使用RSA私钥解密得到AES的KEY、授权Auth和appId ，可以验证本次请求是否有效

通过解密的到AES_key解密加密的data,获取到数据-->处理结果-->使用AES_key的key加密响应结果，并使用md5hex(data)签名内容，响应调用端
```
{
   data:AES_key加密（data数据）
   sign：md5hex(data)
}

```

- 1.每次请求的AES_KEY不同，AES_KEY保存浏览器内存，并且RSA_公钥加密传输，劫持者从始至终都不知道AES_KEY，保证了响应内容，劫持者且伪造服务器的响应结果


安全是相对的，只看价值和付出

### 2.使用方法
**Maven**
```
<dependency>
    <groupId>vip.ylove</groupId>
    <artifactId>st-encrypt-sdk</artifactId>
    <version>1.0</version>
</dependency>
或者
<dependency>
    <groupId>vip.ylove</groupId>
    <artifactId>st-encrypt-sdk-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

**Gradle Groovy DSL**
```
implementation 'vip.ylove:st-encrypt-sdk:1.0'
```
- **以springboot以Maven为例，在pom.xml中引入依赖**  
```
<dependency>
    <groupId>vip.ylove</groupId>
    <artifactId>st-encrypt-sdk-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```
- **启动类Application中添加@StEnableSecurity注解**

```
@SpringBootApplication
@StEnableSecurity
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
- **在application.yml添加RSA公钥及私钥**

```
st:
  encrypt:
    privateKey: '' #rsa私钥 作为服务端时需要配置
    publicKey: ''  #rsa私钥 作为客户端时需要配置 
    appId: ''      #appId 授权id 作为客户端时需要配置 也可以为空
    auth: ''       #auth  授权秘钥 作为客户端时需要配置 也可以为空
```

- **对请求进行解密，响应结果进行加密**

```
@StEncrypt
@PostMapping("/encrypt")
public BaseResult encrypt(@RequestBody Object form){
    log.info("{}",form);
    return BaseResult.success_(form);
}
```
- **若需要验证授权信息,实现接口就可以**
```
@Service
public class StAuth implements StAbstractAuth {
    @Override
    public boolean auth(String appId, String auth,long t) {
        //模拟进行授权验证
        log.info("appId:{}--auth:{}",appId,auth);
        if("123456".equals(appId) && "123456".equals(auth)){
            return true;
        }
        return false;
    }
}
```

- **第三方调用接口**
```
 //加密请求参数
StDencryptBody encrypt = StUtil.encrypt(stConfig.getPublicKey(),
                                        System.currentTimeMillis(),
                                        stConfig.getAppId(),
                                        stConfig.getAppAuth(), form);
//发送请求
EncryptResult stEncryptBody = serverService.encrypt(encrypt);

Object result = null;
if( stEncryptBody.success()){
    //解密数据
     result = StUtil.dencrypt(stConfig.getPublicKey(), stEncryptBody);
}else{
    result = stEncryptBody;
}

```
可以参考提供的demo实现客户端和第三方调用








