package com.covid.support.deliveryservice.filters;


import com.covid.support.deliveryservice.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class RequestFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestId = httpRequest.getHeader("requestId");

        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }

        MDC.put("requestId", requestId);
        httpResponse.setHeader("requestId", requestId);
        Date requestStartTime = new Date();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);
        try {
            performRequestAudit(requestWrapper);
            chain.doFilter(requestWrapper, responseWrapper);
            performResponseAudit(responseWrapper);
            Date requestEndTime = new Date();
            log.info("api = {} request_start_at = {} request_end_at = {} request_complete_time = {} ms",MDC.get(Constants.MDC_PATH),requestStartTime.toString(),requestEndTime.toString(),(requestEndTime.getTime() - requestStartTime.getTime()));
        } finally {
            MDC.remove("requestId");
        }
    }

    private void performRequestAudit(ContentCachingRequestWrapper requestWrapper) {
        try {
            String apiPath = new ServletServerHttpRequest((HttpServletRequest) requestWrapper.getRequest()).getURI().getPath();
            MDC.put(Constants.MDC_PATH,apiPath);
            StringBuffer stringBuffer = new StringBuffer("HTTP Request : ");
            if (requestWrapper != null) {
                stringBuffer.append(new ServletServerHttpRequest((HttpServletRequest) requestWrapper.getRequest()).getMethod());
                stringBuffer.append(" Path : ").append(apiPath);
                stringBuffer.append(" Headers : ").append(new ServletServerHttpRequest((HttpServletRequest) requestWrapper.getRequest()).getHeaders());
                if (requestWrapper.getContentAsByteArray() != null && requestWrapper.getContentAsByteArray().length > 0) {
                    stringBuffer.append(" Entity : ").append(getPayLoadFromByteArray(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding()));
                }
            }
            log.info(stringBuffer.toString());
        } catch (Exception ex) {
            log.error("Exception happened = {}", ex);
        }
    }

    private void performResponseAudit(ContentCachingResponseWrapper responseWrapper) {
        try {
            StringBuffer stringBuffer = new StringBuffer("HTTP Response : ");
            String apiPath = MDC.get(Constants.MDC_PATH);
            if (responseWrapper != null) {
                stringBuffer.append("Path : ").append(apiPath);
                stringBuffer.append(" Status Code : ").append(responseWrapper.getStatus());
                stringBuffer.append(" Headers : ").append(new ServletServerHttpResponse((HttpServletResponse) responseWrapper.getResponse()).getHeaders());
                if (responseWrapper.getContentAsByteArray() != null
                        && responseWrapper.getContentAsByteArray().length > 0) {
                    stringBuffer.append(" Response Body : ").append(getPayLoadFromByteArray(responseWrapper.getContentAsByteArray(),
                            responseWrapper.getCharacterEncoding()));
                }
            }
            log.info(stringBuffer.toString());
            responseWrapper.copyBodyToResponse();
        } catch (Exception ex) {
            log.error("Exception happened = {}", ex);
        }
    }

    private String getPayLoadFromByteArray(byte[] requestBuffer, String charEncoding) {
        String payLoad = "";
        try {
            payLoad = new String(requestBuffer, charEncoding);
        } catch (UnsupportedEncodingException unex) {
            payLoad = "Unsupported-Encoding";
        }
        return payLoad;
    }
}
