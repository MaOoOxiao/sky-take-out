package com.sky.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建RedisTemplate对象");
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置redis key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//@Slf4j
//public class RedisConfiguration {
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        log.info("开始创建RedisTemplate对象");
//
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        // key使用String序列化
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//
//        // value使用JSON序列化
//        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//        redisTemplate.setValueSerializer(jsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
//
//        // 启用事务支持（可选）
//        // redisTemplate.setEnableTransactionSupport(true);
//
//        redisTemplate.afterPropertiesSet();
//        log.info("RedisTemplate对象创建完成");
//        return redisTemplate;
//    }
//}