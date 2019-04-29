package gov.cpsc.itds.common.audit;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * This class handles auditing of execution times for all rest services.
 */
@Aspect
@Component
public class Auditor {

	Logger auditLog = LoggerFactory.getLogger("audit.performance");
	Logger logger = LoggerFactory.getLogger(Auditor.class);

	@Pointcut("within(@org.springframework.stereotype.Controller *)")
	public void controller() {
	}

	@Pointcut("execution(* *.*(..))")
	protected void allMethod() {
	}

	@Pointcut("execution(public * *(..))")
	protected void loggingPublicOperation() {
	}

	@Pointcut("execution(* *.*(..))")
	protected void loggingAllOperation() {
	}

	@Pointcut("within(org.learn.log..*)")
	private void logAnyFunctionWithinResource() {
	}
	
	// before -> Any resource annotated with @Controller annotation
	// and all method and function taking HttpServletRequest as first parameter
	@Before("controller() && allMethod() && args(..,request)")
	public void logBefore(JoinPoint joinPoint, HttpServletRequest request) {
	}

	// After -> All method within resource annotated with @Controller annotation
	// and return a value
	@AfterReturning(pointcut = "controller() && allMethod()", returning = "result")
	public void logAfter(JoinPoint joinPoint, Object result) {
	}

	// After -> Any method within resource annotated with @Controller annotation
	// throws an exception ...Log it
	@AfterThrowing(pointcut = "controller() && allMethod()", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
		logger.error("Exception ->>",exception);
	}

	// Around -> Any method within resource annotated with @Controller annotation
	@Around("controller() && allMethod()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object result = joinPoint.proceed();
			long elapsedTime = System.currentTimeMillis() - start;
			auditLog.info("Method " + className + "." + methodName + " ()" + " execution time: " + elapsedTime + " ms");

			return result;
		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
			+ joinPoint.getSignature().getName() + "()");
			throw e;
		}
	}
	
}
