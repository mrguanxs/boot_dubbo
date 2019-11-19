引入依赖
```
<properties>
		<java.version>1.8</java.version>
		<dubbo.version>2.7.4.1</dubbo.version>
</properties>
<dependencies>
    <!-- Dubbo Spring Boot Starter -->
    <dependency>
    	<groupId>org.apache.dubbo</groupId>
    	<artifactId>dubbo-spring-boot-starter</artifactId>
    	<version>${dubbo.version}</version>
    </dependency>
    <!-- Zookeeper dependencies -->
    <dependency>
    	<groupId>org.apache.dubbo</groupId>
    	<artifactId>dubbo-dependencies-zookeeper</artifactId>
    	<version>${dubbo.version}</version>
    	<type>pom</type>
    	<exclusions>
    		<exclusion>
    			<groupId>org.slf4j</groupId>
    			<artifactId>slf4j-log4j12</artifactId>
    		</exclusion>
    	</exclusions>
    </dependency>
</dependencies>
```
###### 1)方式一：注解配置
简介: 
- 在根目录下application.properties或dubbo.properties中配置dubbo属性
- 通过@Service注解暴露服务，@Reference注解引用服务  
- 在Application入口类配置@EnableDubbo注解开启基于注解的dubbo功能

(1)服务提供者
1. 配置dubbo基本信息
```
#在application.properties或dubbo.properties中填写以下信息(注意:文件要在根目录下)
server.port=8080

#dubbo
dubbo.application.name=provider
#注册中心
dubbo.registry.protocol=zookeeper
#dubbo.registry.address=47.94.102.199:9090
dubbo.registry.address=10.211.55.5:2181
#元数据(2.7开始配置，不然看不到元数据，即服务具体暴漏的方法名、参数和返回值)
dubbo.metadata-report.address=zookeeper://10.211.55.5:2181
#协议
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880
#连接监控中心
dubbo.monitor.protocol=registry
```
2. 开启Dubbo扫描
```
@EnableDubbo    //开启Dubbo服务
@SpringBootApplication
public class ProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}
}
```
3. 暴露服务
```
@Service    //Dubbo的Service注解
@Component
public class OrderServiceImpl implements OrderService {
    @Override
    public List<Order> getOrderList() {
        Order order = new Order();
        order.setId(1);
        order.setOrderNum("14s4df4s5d4fs5d4f5s45");
        order.setPrice(new BigDecimal(11));
        order.setDescription("描述信息");

        Order order2 = new Order();
        order2.setId(2);
        order2.setOrderNum("2dsf4s54ds5d4sd78s7d8f");
        order2.setPrice(new BigDecimal(22));
        order2.setDescription("这个12块");

        return Arrays.asList(order,order2);
    }
}
```
(2)服务消费者
1. 配置dubbo基本信息
```
#在application.properties或dubbo.properties中配置dubbo信息(注意:文件要在根目录下)
server.port=8081

#dubbo
dubbo.application.name=consumer
#注册中心
dubbo.registry.protocol=zookeeper
dubbo.registry.address=10.211.55.5:2181
#协议
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880
#连接监控中心
dubbo.monitor.protocol=registry
```
2. 开启Dubbo扫描
```
@EnableDubbo    //开启Dubbo服务
@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
}
```
3. 引用服务
```
@Controller
public class DemoController {

    @Reference  //引用服务，dubbo的Reference注解
    private OrderService orderService;

    @ResponseBody
    @RequestMapping("/hello")
    public List<Order> getOrderList(){
        return orderService.getOrderList();
    }
}
```
(3)调用hello接口测试

###### 2)方式二：XML配置
简介：
- 在xml文件中配置dubbo服务
- 在Application类入口导入配置信息
- 在xml中暴露服务

(1)服务提供者
1. 配置dubbo基本信息provider.xml，并暴露服务
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans-4.3.xsd        http://dubbo.apache.org/schema/dubbo        http://dubbo.apache.org/schema/dubbo/dubbo.xsd">

    <!-- 提供方应用信息，用于计算依赖关系 -->
    <dubbo:application name="provider-xml"  />

    <!-- 使用zookeeper广播注册中心暴露服务地址 -->
    <dubbo:registry address="zookeeper://10.211.55.5:2181" />

    <!--配置元数据-->
    <dubbo:metadata-report address="zookeeper://10.211.55.5:2181"/>

    <!-- 用dubbo协议在20880端口暴露服务 -->
    <dubbo:protocol name="dubbo" port="20880" />

    <!-- 声明需要暴露的服务接口 -->
    <dubbo:service interface="com.guan.order.OrderService" ref="orderService" />

    <!-- 和本地bean一样实现服务 -->
    <bean id="orderService" class="com.guan.provider.service.impl.OrderServiceImpl" />
</beans>
```
2. 引入dubbo配置信息
```
@ImportResource(locations = "classpath:spring/provider.xml")	//引入dubbo配置
@SpringBootApplication
public class ProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}
}
```
3. 声明要暴露的服务并注入IOC(已在1.中声明)

(2)服务消费者
1. 配置dubbo基本信息consumer.xml，并引用服务
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans-4.3.xsd        http://dubbo.apache.org/schema/dubbo        http://dubbo.apache.org/schema/dubbo/dubbo.xsd">

    <!-- 提供方应用信息，用于计算依赖关系 -->
    <dubbo:application name="consumer-xml"  />

    <!-- 使用zookeeper广播注册中心暴露服务地址 -->
    <dubbo:registry address="zookeeper://10.211.55.5:2181" />

    <!-- 生成远程服务代理，可以和本地bean一样使用demoService -->
    <dubbo:reference id="orderService" interface="com.guan.order.OrderService" />
</beans>
```
2. 引入dubbo配置信息
```
@ImportResource(locations = "classpath:spring/consumer.xml")	//引入dubbo配置
@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
}
```
3. 正常调用Service即可
```
@Controller
public class DemoController {

    @Autowired  //正常引用Service
    private OrderService orderService;

    @ResponseBody
    @RequestMapping("/hello")
    public List<Order> getOrderList(){
        return orderService.getOrderList();
    }
}
```
###### 3)方式三：API配置
简介：
- 在Config类中配置dubbo服务
- 在Application类入口配置扫码路径
- 通过@Service注解暴露服务，@Reference注解引用服务

(1)服务提供者
1. 配置dubbo基本信息ProviderDubboConfig
```
package com.guan.provider.config;

import com.guan.order.OrderService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.MetadataReportConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.MonitorConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.Guan
 * @since 2019/11/19
 */
@Configuration
public class ProviderDubboConfig {

    @Bean
    public ApplicationConfig applicationConfig(){
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("provider-api");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig(){
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper");
        registryConfig.setAddress("10.211.55.5:2181");
        return registryConfig;
    }

    @Bean
    public MetadataReportConfig metadataReportConfig(){
        MetadataReportConfig metadataReportConfig = new MetadataReportConfig();
        metadataReportConfig.setAddress("zookeeper://10.211.55.5:2181");
        return metadataReportConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20880);
        return protocolConfig;
    }

    //配置监控中心
    @Bean
    public MonitorConfig monitorConfig(){
        MonitorConfig monitorConfig = new MonitorConfig();
        monitorConfig.setProtocol("registry");
        return monitorConfig;
    }

    //全局配置
    @Bean
    public ProviderConfig providerConfig(){
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(1000);
        providerConfig.setRetries(1);
        return providerConfig;
    }

    @Bean
    public ServiceBean<OrderService> orderServiceServiceBean(OrderService orderService){
        ServiceBean<OrderService> orderServiceServiceBean = new ServiceBean<>();
        orderServiceServiceBean.setInterface(OrderService.class);
        //设置要引用的服务(为IOC的bean生成代理)
        orderServiceServiceBean.setRef(orderService);

        List<MethodConfig> methodConfigList = new ArrayList<>();
        //方法级别配置
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("getOrderList");
        methodConfig.setTimeout(3000);
        methodConfig.setRetries(3);
        methodConfigList.add(methodConfig);
        orderServiceServiceBean.setMethods(methodConfigList);

        return orderServiceServiceBean;
    }
}
```
2. 配置扫描路径
```
@EnableDubbo(scanBasePackages = "com.guan")
@SpringBootApplication
public class ProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}
}
```
3. 暴露服务
```
@Service
@Component
public class OrderServiceImpl implements OrderService {
    @Override
    public List<Order> getOrderList() {
        Order order = new Order();
        order.setId(1);
        order.setOrderNum("14s4df4s5d4fs5d4f5s45");
        order.setPrice(new BigDecimal(11));
        order.setDescription("描述信息");

        Order order2 = new Order();
        order2.setId(2);
        order2.setOrderNum("2dsf4s54ds5d4sd78s7d8f");
        order2.setPrice(new BigDecimal(22));
        order2.setDescription("这个12块");

        return Arrays.asList(order,order2);
    }
}
```
(2)服务消费者
1. 配置dubbo基本信息
```
package com.guan.consumer.config;

import com.guan.order.OrderService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.MetadataReportConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.MonitorConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.Guan
 * @since 2019/11/19
 */
@Configuration
public class ConsumerDubboConfig {

    @Bean
    public ApplicationConfig applicationConfig(){
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("consumer-api");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig(){
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper");
        registryConfig.setAddress("10.211.55.5:2181");
        return registryConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20880);
        return protocolConfig;
    }

    //配置监控中心
    @Bean
    public MonitorConfig monitorConfig(){
        MonitorConfig monitorConfig = new MonitorConfig();
        monitorConfig.setProtocol("registry");
        return monitorConfig;
    }

}
```
2. 开启Dubbo扫描
```
@EnableDubbo(scanBasePackages = "com.guan")
@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
}
```
3. 引用服务
```
@Controller
public class DemoController {

    @Reference  //引用服务，dubbo的Reference注解
    private OrderService orderService;

    @ResponseBody
    @RequestMapping("/hello")
    public List<Order> getOrderList(){
        return orderService.getOrderList();
    }
}
```
(3)调用hello接口测试

