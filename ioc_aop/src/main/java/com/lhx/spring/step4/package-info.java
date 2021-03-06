/**
 * AOP 源码讲解
 * <p>1：{@link @EnableAspectJAutoProxy} @import 了AspectJAutoProxyRegistrar类， 此类是一个ImportBeanDefinitionRegistrar注册器，注册了一个
 * beanname 为 org.springframework.aop.config.internalAutoProxyCreator=AnnotationAwareAspectJAutoProxyCreator RootBeanDefinition
 * <p>2：IOC容器启动时调用registerBeanPostProcessors 注册系统的所有BeanPostProcessors,在注册bean时从beanFactory中去获取bean的实例，
 * 如果不存在则创建bean.
 * AnnotationAwareAspectJAutoProxyCreator 的父类AbstractAutoProxyCreator 实现了 SmartInstantiationAwareBeanPostProcessor bean处理器
 * <p>3:当创建{@link com.lhx.spring.step4.aop.Calculator } 单实例bean流程如下：
 * finishBeanFactoryInitialization() 创建单实例bean
 * AbstractBeanFactory.preInstantiateSingletons()--AbstractBeanFactory.getBean()-->AbstractBeanFactory.doCreateBean()--->
 * AbstractAutowireCapableBeanFactory.createBean()--->AbstractAutowireCapableBeanFactory.doCreateBean()--->
 * AbstractAutowireCapableBeanFactory.createBeanInstance() beanWrapper对象被创建--->
 * AbstractAutowireCapableBeanFactory.populateBean()bean属性赋值--->AbstractAutowireCapableBeanFactory.initializeBean()方法初始化并
 * --->applyBeanPostProcessorsBeforeInitialization() 前置处理 执行initMethod方法--->applyBeanPostProcessorsAfterInitialization() 后置处理
 * <p>4：由于Calculator 的bean被AOP 代理了，所以代理的代码主要在applyBeanPostProcessorsAfterInitialization()中，
 * 该方法实际是从beanFactory中获取所有的BeanPostProcessors并执行后置处理方法，AnnotationAwareAspectJAutoProxyCreator父类
 * AbstractAutoProxyCreator.postProcessAfterInitialization() 方法，该方法中调用wrapIfNecessary()
 * 通过ProxyFactory.getProxy()去获取CglibAopProxy.getProxy()代理的bean
 *
 * <p>5:当执行{@link com.lhx.spring.step4.aop.Calculator#div(int, int)} 方法时会执行{@link org.springframework.aop.framework.CglibAopProxy}
 * 内部类{DynamicAdvisedInterceptor#interceptor()}方法,执行this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass)去获取代理链，
 * （其实链是一个List ，里面的内容根据Advisor切面，去生成对应的MethodInterceptor对象 如：
 * {@link org.springframework.aop.aspectj.AspectJAfterThrowingAdvice
 * ,org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor
 * ,org.springframework.aop.interceptor.ExposeInvocationInterceptor
 * ,org.springframework.aop.aspectj.AspectJAfterAdvice
 * ,org.springframework.aop.aspectj.AspectJAroundAdvice,org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor
 * }并放入到list 链中）然后retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, list, methodProxy).proceed();
 * 执行Calculator Asepects 切面的具体代码
 *
 * Created by lihuaixin on 2019/7/16 14:06
 *
 * @see @Before 运行之前 前置通知
 * @see @After  运行之后 后置通知
 * @see @AfterReturning 返回之前 后置返回通知
 * @see @AfterThrowing 异常发生后 异常通知
 * @see @Around 运行执行前 before之前 环绕通知
 * @see @Pointcut("execution(public int com.lhx.spring.step4.aop.Calculator.*(..))") 切点
 */
package com.lhx.spring.step4;