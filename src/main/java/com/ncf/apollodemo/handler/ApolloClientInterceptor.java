package com.ncf.apollodemo.handler;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ncf.apollodemo.config.ApolloClientRegistrar;
import com.ncf.apollodemo.manager.service.impl.TokenService;
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
        // 获取路径变量
        Map<String, String> pathVariables = getPathVariables(request);

        // 仅当路径变量中存在 appId 时进行处理
        if (pathVariables != null && pathVariables.containsKey("appId")) {
            String uri = request.getRequestURI().replaceFirst(request.getContextPath(), "");

            //  验证路径是否以 /apollo/ 开头
            if (uri.startsWith("/apollo/")) {
                String appId = pathVariables.get("appId");
                ApolloOpenApiClient client = getClient(appId);
                request.setAttribute("apolloClient", client);
            }
        }

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