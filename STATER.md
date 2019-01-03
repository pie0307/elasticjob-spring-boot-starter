#Spring Boot Starter 理解

可以认为starter是一种服务（和JS的插件类似）,使得使用某个功能的开发者不需要关注各种依赖库的处理，
不需要具体的配置信息，由Spring Boot自动通过classpath路径下的类发现需要的Bean，并织入bean


#原理

首先，SpringBoot 在启动时会去依赖的starter包中寻找 resources/META-INF/spring.factories 文件，
    然后根据文件中配置的Jar包去扫描项目所依赖的Jar包。

第二步，根据 spring.factories配置加载AutoConfigure类。

最后，根据 @Conditional注解的条件，进行自动配置并将Bean注入Spring Context 上下文当中。

#编写自己的starter

1、首先创建一个maven项目
2、项目命名方式为[name]-spring-boot-starter (官方命名方式 spring-boot-starter-[name])
3、在pom.xml中添加starter所需要的依赖
4、创建starter相关类（至少有一个自动配置类）
5、在resource文件夹下创建META-INF文件夹 (srping.factories)

#注

@ConditionalOnBean:当容器中有指定的Bean的条件下  
@ConditionalOnClass：当类路径下有指定的类的条件下  
@ConditionalOnExpression:基于SpEL表达式作为判断条件  
@ConditionalOnJava:基于JVM版本作为判断条件  
@ConditionalOnJndi:在JNDI存在的条件下查找指定的位置  
@ConditionalOnMissingBean:当容器中没有指定Bean的情况下  
@ConditionalOnMissingClass:当类路径下没有指定的类的条件下  
@ConditionalOnNotWebApplication:当前项目不是Web项目的条件下  
@ConditionalOnProperty:指定的属性是否有指定的值  
@ConditionalOnResource:类路径下是否有指定的资源  
@ConditionalOnWebApplication:当前项目是Web项目的条件下  