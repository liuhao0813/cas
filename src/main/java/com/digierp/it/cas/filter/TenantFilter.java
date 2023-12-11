package com.digierp.it.cas.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * 自定义过滤器，根据请求头进行判断，是否存在租户信息
 *
 */
public class TenantFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String tenantId = request.getHeader("X-Tenant-Id");
        boolean hasAccess = isUseAllowed(tenantId);
        if(hasAccess){
            filterChain.doFilter(request,response);
            return;
        }
        throw new AccessDeniedException("TenantId Failed Access denied");
    }

    private boolean isUseAllowed(String tenantId) {
        return true;
    }
}
