package com.example.demo.util.jwt;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.enums.UserRole;
import com.example.demo.util.MyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.*;

/**
 * @author admin
 */
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws MyException {

        String token;
        List<String> list=new ArrayList<>();
        Collections.addAll(list,request.getRequestURL().toString().split("/"));
        if (list.contains("webSocket")) {
            if (StringUtils.isEmpty(request.getHeader("Sec-WebSocket-Protocol"))) {
                throw new MyException("token不能为空");
            }
            token = request.getHeader("Sec-WebSocket-Protocol").split(" ")[1];
        }
        else {
            if (StringUtils.isEmpty(request.getHeader("authorization"))) {
                throw new MyException("token不能为空");
            }
            token = request.getHeader("authorization").split(" ")[1];
        }

        List<String> task4AuftraList=new ArrayList<>();
        List<String> task4WorkerList=new ArrayList<>();
        List<String> task4AdminList=new ArrayList<>();
        List<String> report4WorkerList=new ArrayList<>();
        String[] task4Auftra = new String[]{"creatTask", "deleteTask", "upload"};
        Collections.addAll(task4AuftraList, task4Auftra);

        String[] task4Worker = new String[]{"select", "cancelSelect"};
        Collections.addAll(task4WorkerList, task4Worker);

        String[] task4Admin = new String[]{"setRule", "currentRules", "createRule", "allRule", "deleteRule"};
        Collections.addAll(task4AdminList, task4Admin);

        String[] report4Worker = new String[]{"create", "forkReport", "mine", "picture", "rate", "similar", "myRating"};
        Collections.addAll(report4WorkerList, report4Worker);
        if(StringUtils.isEmpty(token)){
            throw new MyException("token不能为空");
        }
        try {
            String url=request.getRequestURL().toString();
            List<String> urls=new ArrayList<>();
            Collections.addAll(urls,url.split("/"));

            DecodedJWT decodedJWT=JWTUtil.verify(token);
            UserRole userRole=UserRole.valueOf(decodedJWT.getClaim("userRole").asString());
            if (urls.contains("task")){
                for (String s : task4AdminList) {
                    if (urls.contains(s) && userRole != UserRole.ADMIN) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return false;
                    }
                }
                for (String s : task4WorkerList) {
                    if (urls.contains(s) && userRole != UserRole.TESTER) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return false;
                    }
                }
                for (String s : task4AuftraList) {
                    if (urls.contains(s) && userRole != UserRole.AUFTRA) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return false;
                    }
                }
            }
            if (urls.contains("report")){
                for (String s : report4WorkerList) {
                    if (urls.contains(s) && userRole != UserRole.TESTER) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return false;
                    }
                }
            }
        } catch (SignatureVerificationException e) {
            log.error("无效签名！ 错误 ->", e);
            return false;
        } catch (TokenExpiredException e) {
            log.error("token过期！ 错误 ->", e);
            return false;
        } catch (AlgorithmMismatchException e) {
            log.error("token算法不一致！ 错误 ->", e);
            return false;
        } catch (Exception e) {
            log.error("token无效！ 错误 ->", e);
            return false;
        }
        return true;
    }
}
