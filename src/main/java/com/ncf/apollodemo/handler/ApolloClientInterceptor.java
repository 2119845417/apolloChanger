package com.ncf.apollodemo.handler;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ncf.apollodemo.config.ApolloClientRegistrar;
import com.ncf.apollodemo.service.impl.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class ApolloClientInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ApolloClientRegistrar beanRegistrar;
    @Autowired
    private ApplicationContext context;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 路径匹配检查
        String uri = request.getRequestURI().replaceFirst(request.getContextPath(), "");
        if (!uri.startsWith("/apollo/")) {
            return true; // 非Apollo接口跳过
        }

        // 2. 解析路径变量
        Map<String, String> pathVariables = getPathVariables(request);
        if (pathVariables == null || !pathVariables.containsKey("appId")) {
            response.sendError(400, "Missing appId in path");
            return false;
        }

        // 3. 初始化客户端
        String appId = pathVariables.get("appId");
        ApolloOpenApiClient client = getClient(appId);
        
        // 4. 存储客户端到请求域
        request.setAttribute("apolloClient", client);
        return true;
    }

    private Map<String, String> getPathVariables(HttpServletRequest request) {
        return (Map<String, String>) request.getAttribute(
            HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }


    public ApolloOpenApiClient getClient(String appId) {
        // 检查Bean是否已存在
        String beanName = "apolloClient_" + appId;
        if (!context.containsBean(beanName)) {
            String token = tokenService.getTokenByAppId(appId);
            beanRegistrar.registerClientBean(appId, token); // 动态注册
        }
        ApolloOpenApiClient client = context.getBean(beanName, ApolloOpenApiClient.class);
        return client;
    }
}