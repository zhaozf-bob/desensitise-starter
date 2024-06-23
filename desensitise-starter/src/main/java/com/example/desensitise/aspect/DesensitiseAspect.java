package com.example.desensitise.aspect;

import com.example.desensitise.config.DesensitiseProperties;
import com.example.desensitise.service.IDesensitiseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Configuration
@Order(-1)
@Slf4j
public class DesensitiseAspect {

    @Autowired
    private IDesensitiseService desensitiseService;

    @Autowired
    private DesensitiseProperties desensitiseProperties;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void point() {
    }

    @Around("point()")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        // 获取访问uri
        String uri = getUri();
        // 获取方法全限定名称  todo 需要增加@PointCut才能使用
        String wholeName = getClassWholeName(pjp);

        // 执行业务代码
        Object result = pjp.proceed();

        if (desensitiseProperties.getEnabled()) {
            try {
                // 脱敏
                List<String> keys = Arrays.asList(uri, wholeName).stream().filter(str -> Strings.isNotBlank(str)).collect(Collectors.toList());
                result = desensitiseService.desensitise(keys, result);
            } catch (Exception e) {
                log.error("脱敏异常：{}", result, e);
            }
        }
        return result;
    }

    private String getClassWholeName(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        String wholeName = signature.getDeclaringTypeName() + "." + signature.getName();
        return wholeName;
    }

    private String getUri() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String uri = request.getRequestURI();
            return uri;
        } catch (Exception e) {
            return null;
        }
    }
}
