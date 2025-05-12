package kr.hhplus.be.server.common.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class LockKeyGenerator {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    public String generateKey(ProceedingJoinPoint joinPoint, String prefix, String keyExpression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        String[] paramNames = discoverer.getParameterNames(method);
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        String dynamicKey = parser.parseExpression(keyExpression).getValue(context, String.class);
        return prefix + ":" + dynamicKey;
    }
}
