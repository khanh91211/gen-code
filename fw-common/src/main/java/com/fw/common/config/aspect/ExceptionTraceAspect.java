package com.fw.common.config.aspect;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import lombok.extern.log4j.Log4j2;

@Configuration
@Aspect
@Log4j2
public class ExceptionTraceAspect {
  @AfterThrowing(pointcut = "execution(* com.fw..ctrl..*(..))", throwing = "ex")
  public void logError(JoinPoint pjp, Throwable ex) {
    process(pjp, ex);
  }

  @AfterThrowing(throwing = "ex", pointcut = "@annotation(com.fw.core.config.aspect.ExceptionTrace)")
  public void processExceptionLoggerPointCut(JoinPoint pjp, Throwable ex) {
    process(pjp, ex);
  }

  private void process(JoinPoint pjp, Throwable ex) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    String stackTrace = sw.toString();

    String methodName = pjp.getSignature().getName();
    String className = pjp.getSignature().getDeclaringTypeName();
    String message = ex.getMessage();

    ThreadContext.put("methodName", methodName);
    ThreadContext.put("className", className);
    ThreadContext.put("stackTrace", stackTrace);
    log.error(message);
  }
}
