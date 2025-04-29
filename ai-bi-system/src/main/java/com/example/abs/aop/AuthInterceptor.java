package com.example.abs.aop;

import com.example.abs.annoation.AuthCheck;
import com.example.abs.common.ResponseCode;
import com.example.abs.exception.BusinessException;
import com.example.abs.pojo.vo.UserVo;
import com.example.abs.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    // https://t.zsxq.com/0emozsIJh

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        List<Integer> anyRole =  Arrays.stream(authCheck.anyRole()).boxed().toList();
        int mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        UserVo user = userService.getCurrentUser(request);
        // 拥有任意权限即通过
        if (!anyRole.isEmpty()) {
            Integer userRole = user.getUserRole();
            if (!anyRole.contains(userRole)) {
                throw new BusinessException(ResponseCode.FORBIDDEN);
            }
        }
        // 必须有所有权限才通过
        Integer userRole = user.getUserRole();
        if (userRole != mustRole) {
            throw new BusinessException(ResponseCode.FORBIDDEN);
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}