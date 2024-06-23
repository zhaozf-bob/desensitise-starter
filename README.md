# desensitise-starter

`在输出数据前进行数据脱敏操作。脱敏规则使用Hutool提供的DesensitizedUtil，只需要提供脱敏配置即可使用。`

`预想是引入依赖后，在业务工程中访问数据库的脱敏配置。`

`目的是让运维/运营人员可通过页面请求接口配置脱敏规则，而不用每次增加脱敏字段都需要开发。`

### 引入desensitise-starter

pom.xml引入依赖

```pom
<dependency>
       <groupId>cn.gzsendi</groupId>
       <artifactId>desensitise-starter</artifactId>
       <version>0.0.1-SNAPSHOT</version>
</dependency>
```

实现脱敏配置提供类

```java
@Service
public class DesensitiseConfigProviderImpl implements IDesensitiseConfigProvider {
    @Override
    public List<DesensitiseConfig> match(List<String> keys) {
        if (keys.contains("/user")) {
            ArrayList<DesensitiseConfig> list = new ArrayList<>();
            list.add(new DesensitiseConfig("/user", "name", "中文名"));
            list.add(new DesensitiseConfig("/user", "age", "number"));
//            list.add(new DesensitiseConfig("/user", "user.name", "中文名"));
            return list;
        }
        return null;
    }
}
```

安装starter：进入 `desensitise-starter` 目录执行

    mvn clean install

打包应用程序：进入 `web-run` 目录执行

    mvn clean package

执行应用程序：进入 `web-run/target/` 目录执行

    java -jar web-run-0.0.1-SNAPSHOT.jar

发起请求查看效果：`http://127.0.0.1:8080/user`

### 代码说明

脱敏配置提供类中的方法参数keys内容为请求uri

```java
    private String getUri() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String uri = request.getRequestURI();
            return uri;
        } catch (Exception e) {
            return null;
        }
    }
```

有时候导出文件接口实现脱敏，需要对获取数据的方法返回内容进行脱敏

可将keys的内容扩展为全限定类名，需要在切入点@PointCut中增加要脱敏的类

```java
 @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void point() {
    }
```

#### 支持的脱敏类型（Hutool的DesensitizedUtil）

```java
	public enum DesensitizedType {
		/**
		 * 用户id
		 */
		USER_ID,
		/**
		 * 中文名
		 */
		CHINESE_NAME,
		/**
		 * 身份证号
		 */
		ID_CARD,
		/**
		 * 座机号
		 */
		FIXED_PHONE,
		/**
		 * 手机号
		 */
		MOBILE_PHONE,
		/**
		 * 地址
		 */
		ADDRESS,
		/**
		 * 电子邮件
		 */
		EMAIL,
		/**
		 * 密码
		 */
		PASSWORD,
		/**
		 * 中国大陆车牌，包含普通车辆、新能源车辆
		 */
		CAR_LICENSE,
		/**
		 * 银行卡
		 */
		BANK_CARD,
		/**
		 * IPv4地址
		 */
		IPV4,
		/**
		 * IPv6地址
		 */
		IPV6,
		/**
		 * 定义了一个first_mask的规则，只显示第一个字符。
		 */
		FIRST_MASK,
		/**
		 * 清空为null
		 */
		CLEAR_TO_NULL,
		/**
		 * 清空为""
		 */
		CLEAR_TO_EMPTY
	}
```

#### 脱敏了一个字段，但是返回json却有多个相同字段脱敏？

由于通过反射获取值和设置值，当一个对象的某个字段脱敏之后，由于该对象有多次引用，所以转化成json字符串后，能看到多处都脱敏了。
