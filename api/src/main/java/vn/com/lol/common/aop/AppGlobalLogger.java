package vn.com.lol.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.com.lol.common.utils.JsonUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Aspect
@Slf4j
public class AppGlobalLogger {

    private final HttpServletRequest httpServletRequest;

    public AppGlobalLogger(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
    }


    @Pointcut("within(vn.com.lol.*) " +
            "&& (@annotation(org.springframework.web.bind.annotation.GetMapping)) " +
            "|| (@annotation(org.springframework.web.bind.annotation.PostMapping)) " +
            "|| (@annotation(org.springframework.web.bind.annotation.PutMapping)) " +
            "|| (@annotation(org.springframework.web.bind.annotation.DeleteMapping)) ")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void logMethod(JoinPoint joinPoint) {
        Optional<RequestMapping> mapping = getRequestMapping(joinPoint);
        if (mapping.isEmpty()) return;
        RequestMapping requestMapping = mapping.get();
        Map<String, Object> parameters = getParameters(joinPoint);
        log.info("==> path(s): {}, method(s): {}, arguments: {} ",
                getFullURL(httpServletRequest), requestMapping.method(), JsonUtil.stringify(parameters));
    }

    @AfterReturning(pointcut = "pointcut()", returning = "entity")
    public void logMethodAfter(JoinPoint joinPoint, ResponseEntity<?> entity) {
        Optional<RequestMapping> mapping = getRequestMapping(joinPoint);
        if (mapping.isEmpty()) return;
        RequestMapping requestMapping = mapping.get();
        log.info("<== path(s): {}, method(s): {}, returning: {}",
                getFullURL(httpServletRequest), requestMapping.method(), JsonUtil.stringify(entity));
    }

    private Optional<RequestMapping> getRequestMapping(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return Arrays.stream(signature.getMethod().getAnnotations())
                .map(ano -> ano.annotationType().getAnnotation(RequestMapping.class))
                .filter(Objects::nonNull)
                .findAny();
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        HashMap<String, Object> map = new HashMap<>();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }
        return map;
    }

    @Around("controllerPointcut()")
    public Object controllerAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();

        final String args = Arrays.toString(joinPoint.getArgs());

        log.info("Entered:{}() with argument[s] = {}",
                joinPoint.getSignature().getName(), args);

        try {
            Object result = joinPoint.proceed();
            log.info("Exit:{}() Elapsed time: {}ms with result = {}",
                    joinPoint.getSignature().getName(), getExecutionTime(start), JsonUtil.stringify(result));
            return result;
        } catch (Exception e) {
            log.error("Exception: {} in {}() Elapsed time: {}ms", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getName(), getExecutionTime(start));
            throw e;
        }
    }

    private long getExecutionTime(Instant start) {
        Instant end = Instant.now();
        return Duration.between(start, end).toMillis();
    }
}
