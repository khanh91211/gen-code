package com.fw.core.config.aspect;

import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import lombok.extern.log4j.Log4j2;

@Aspect
@Configuration
@Log4j2
public class MethodProfileAspect {
  @Around("execution(* com.fw..service..*(..))")
  public Object profile(ProceedingJoinPoint pjp) throws Throwable {
    return process(pjp);
  }

  @Around("@annotation(com.fw.core.config.aspect.MethodProfile)")
  public Object processLogExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
    return process(pjp);
  }

  private Object process(ProceedingJoinPoint pjp) throws Throwable {
    long start = System.currentTimeMillis();
    Object output = pjp.proceed();
    String methodName = pjp.getSignature().getName();
    String className = pjp.getSignature().getDeclaringTypeName();
    ThreadContext.put("methodName", methodName);
    ThreadContext.put("className", className);
    long elapsedTime = System.currentTimeMillis() - start;
    ThreadContext.put("duration", "" + elapsedTime);
    if(output!=null) {
      log.debug(output.getClass().getCanonicalName());
    }else{
      log.debug("null");
    }
    return output;
  }
}
