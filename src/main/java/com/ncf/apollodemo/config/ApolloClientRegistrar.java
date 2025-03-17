package com.ncf.apollodemo.config;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApolloClientRegistrar  implements BeanDefinitionRegistryPostProcessor {
    private final Map<String, BeanDefinition> dynamicBeans = new ConcurrentHashMap<>();
    private BeanDefinitionRegistry registry;
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        // 动态注册入口
        this.registry = registry;
    }

    // 动态注册新Bean的核心方法
    public void registerClientBean(String appId, String token) {
        String beanName = "apolloClient_" + appId;
        if (!registry.containsBeanDefinition(beanName)) {
            // 获取或创建 RequestConfig 实例
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .build();

            BeanDefinition definition = BeanDefinitionBuilder
                    .genericBeanDefinition(ApolloOpenApiClient.class)
                    .addConstructorArgValue("https://apollo.portal.url")
                    .addConstructorArgValue(token)
                    .addConstructorArgValue(config) // 明确指定第三个参数
                    .setScope(BeanDefinition.SCOPE_SINGLETON)
                    .getBeanDefinition();
            registry.registerBeanDefinition(beanName, definition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}