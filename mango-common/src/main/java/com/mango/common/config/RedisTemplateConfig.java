package com.mango.common.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置Key和HashKey的序列化器为StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 设置Value和HashValue的序列化器为GenericJackson2JsonRedisSerializer (JSON格式)，并配置返回序列化类型

        // 创建 ObjectMapper 并配置多态类型支持
        /**
         * 作用：
         * 这行代码配置 ObjectMapper 在序列化 JSON 时，自动嵌入类的类型信息。
         * 这样在反序列化时，Jackson 就能根据这些信息准确地还原成原始对象类型，而不是简单的 LinkedHashMap。
         */
        ObjectMapper objectMapper = new ObjectMapper();

        // 1. 注册 Java 8 时间模块
        objectMapper.registerModule(new JavaTimeModule());
        // 2. 禁用将日期写为时间戳，改为 ISO 字符串格式（如 "2025-11-14T14:33:10"）
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 创建更安全的类型验证器（白名单机制）
        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.mango.common.entity")   // 实体类包
                .allowIfSubType("com.mango.bkl.entity")
                .allowIfSubType("java.util.ArrayList")
                .allowIfSubType("java.util.HashMap")
                .allowIfSubType("java.time.LocalDateTime")   // 时间类型也需要允许
                .allowIfSubType("java.time.LocalDate")
                .build();

        objectMapper.activateDefaultTyping(
                typeValidator,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

}
