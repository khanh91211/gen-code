package com.fw.core.config.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpLoggingFilter extends OncePerRequestFilter {
    protected static final Logger loggerHttpLoggerFilter = LoggerFactory.getLogger(HttpLoggingFilter.class);
    private static final String LOG_TYPE = "logType";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        ThreadContext.put("uuid", uuid);
        ThreadContext.put("startTime", String.valueOf(start));
        ThreadContext.put("path", request.getRequestURI());
        ThreadContext.put(LOG_TYPE, "httprequest");
        boolean isWriteData = isWriteDataRequestAndResponse(request.getRequestURI());
        //get request
        final RequestWrapper requestWrapper = new RequestWrapper(uuid, start, request, isWriteData);
        final ResponseWrapper responseWrapper = new ResponseWrapper(uuid, response);
        Map<String, String> requestHeaders = getRequestHeaders(requestWrapper);
        ThreadContext.put("IpClient", requestHeaders.get("x-forwarded-for"));
        ThreadContext.put("Username", requestHeaders.get("username"));
        HttpInfo httpInfo = new HttpInfo();
        //write request info
        getHttpRequestInfo(requestWrapper, httpInfo);
        ObjectMapper objectMapper = new ObjectMapper();
        String indented = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(httpInfo);
        if (isWriteData) {
            loggerHttpLoggerFilter.info("Request Info : uri={}, id={}, body={}", request.getRequestURI(), uuid, indented);
        }
        filterChain.doFilter(requestWrapper, responseWrapper);

        //write response info
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        if (isLogResponse(request.getRequestURI())) {
            ThreadContext.put("responseCode", String.valueOf(responseWrapper.getStatus()));
            ThreadContext.put("duration", String.valueOf(timeElapsed));
            ThreadContext.put(LOG_TYPE, "httpresponse");
            getHttpResponseInfo(responseWrapper, httpInfo);
            httpInfo.setRequestId(uuid);
            objectMapper = new ObjectMapper();
            indented = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(httpInfo);
            loggerHttpLoggerFilter.info("ResponseInfo From SMService: id={}, body={}", uuid, indented);
        }
        clearThreadContext();
    }

    private boolean isLogResponse(String requestUri) {
        return isWriteDataRequestAndResponse(requestUri);
    }

    public void getHttpResponseInfo(ResponseWrapper responseWrapper, HttpInfo httpInfo) throws UnsupportedEncodingException {
        httpInfo.setResponseBody(getResponseBody(responseWrapper));
        httpInfo.setStatus(getHttpStatus(responseWrapper));
    }

    public void getHttpRequestInfo(RequestWrapper requestWrapper, HttpInfo httpInfo) {
        Map<String, String> requestHeaders = getRequestHeaders(requestWrapper);
        httpInfo.setUrl(getUrl(requestWrapper));
        httpInfo.setRequestBody(getRequestBody(requestWrapper));
        httpInfo.setUsername(requestHeaders.get("username"));
    }

    public Map<String, String> getRequestHeaders(RequestWrapper requestWrapper) {
        Map<String, String> headers = new HashMap<>();
        Collections.list(requestWrapper.getHeaderNames()).forEach(key -> headers.put(key, requestWrapper.getHeader(key)));
        return headers;
    }

    public String getUrl(RequestWrapper requestWrapper) {
        return requestWrapper.getRequestURL() + "?" + requestWrapper.getQueryString();
    }

    public String getRequestBody(RequestWrapper requestWrapper) {
        return requestWrapper.getBody();
    }

    public String getResponseBody(ResponseWrapper responseWrapper) throws UnsupportedEncodingException {
        return new String(responseWrapper.toByteArray(), responseWrapper.getCharacterEncoding());
    }

    private int getHttpStatus(ResponseWrapper responseWrapper) {
        return responseWrapper.getStatus();
    }

    private boolean isWriteDataRequestAndResponse(String url) {
        boolean isIgnore = true;
        if ("/actuator/prometheus".equals(url) ||
                "/actuator/health".equals(url) ||
                "/v1.0/ch/configuration/create".equals(url) ||
                "/v1.0/bl/document/upload".equals(url) ||
                "/v1.0/bl/document/approve".equals(url) ||
                "/v1.0/ch/document/upload".equals(url) ||
                "/v1.0/ch/hc-document/upload".equals(url)
        ) {
            isIgnore = false;
        }
        return isIgnore;
    }

    private void clearThreadContext() {
        ThreadContext.remove("uuid");
        ThreadContext.remove("startTime");
        ThreadContext.remove("path");
        ThreadContext.remove("responseCode");
        ThreadContext.remove("duration");
        ThreadContext.remove(LOG_TYPE);
        ThreadContext.remove("IpClient");
        ThreadContext.remove("Username");
    }
}