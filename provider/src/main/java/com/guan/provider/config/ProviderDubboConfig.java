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
