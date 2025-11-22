package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面 ， 实现公共字段填充
 */
@Component
@Slf4j
@Aspect
public class AutoFullAspect {

    //切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    //前置通知 ， 在通知中进行公共字段的赋值
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint jp) throws  Throwable{
        log.info("开始进行公共字段的自动填充。。。");

        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method= signature.getMethod();
        AutoFill autoFill= method.getAnnotation(AutoFill.class);
        OperationType oy = autoFill.value();

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = jp.getArgs();
        if(args ==null || args.length==0) return ;
        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.get();

        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        //entity.getClass().getDeclaredMethod("setupdateTime"); //这个获取的是没有参数的这个方法
        Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
        setUpdateTime.invoke(entity, now);
        // 使用Threadlocal来获取当前用户的id
        setUpdateUser.invoke(entity, BaseContext.get());
        if(oy == OperationType.INSERT){
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            setCreateUser.invoke(entity, BaseContext.get());
            setCreateTime.invoke(entity, now );
        }
    }
}
