package t1.exercises.exercise.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("@annotation(t1.exercises.exercise.annotations.Loggable)")
    public void logging(JoinPoint joinPoint) {
        log.info("The method {} of class {} was called",
                joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getName());
    }
    @AfterThrowing(pointcut = "@annotation(t1.exercises.exercise.annotations.ExceptionHandler)",
    throwing = "exception")
    public void throwing(JoinPoint joinPoint, Exception exception) {
        log.error("{} caught exception in method {} of class {}",
                exception.getClass().getName(),
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getName());
    }
    @AfterReturning("@annotation(t1.exercises.exercise.annotations.ReturningHandler)")
    public void returning(JoinPoint joinPoint) {
        log.info("Method {} in class {} return successfully",
                joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getName());
    }

    @Around("@annotation(t1.exercises.exercise.annotations.TimeRecorder)")
    public Object timer(ProceedingJoinPoint point){
        Object proceed = null;
        long start = System.currentTimeMillis();
        try {
            proceed = point.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        long elapsed = end - start;
        log.info("Method {} in class {} completed in: {} ms",
                point.getSignature().getName(),
                point.getTarget().getClass().getName(),
                elapsed);
        return proceed;
    }

}
