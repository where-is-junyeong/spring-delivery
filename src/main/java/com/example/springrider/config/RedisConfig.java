//package com.example.springrider.config;
//
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableCaching
//public class RedisConfig {
//
//
//    @Value("${spring.data.redis.host}")
//    private String redisHost;
//
//    @Value("${spring.data.redis.port}")
//    private int redisPort;
//
//    // Redis 연결 팩토리
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(redisHost, redisPort);
//    }
//
//    // ObjectMapper 설정
//    private ObjectMapper redisObjectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.activateDefaultTyping(
//                BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build(),
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
//        return objectMapper;
//    }
//
//    // RedisTemplate (복합 객체용)
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);
//
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper()));
//        return redisTemplate;
//    }
//
//    // String 전용 RedisTemplate
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
//        StringRedisTemplate template = new StringRedisTemplate();
//        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//        return template;
//    }
//
//    // Redis 캐시 설정 (검색 캐시와 로그 TTL 구분)
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(redisObjectMapper());
//
//        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
//                .entryTtl(Duration.ofMinutes(5)); // 기본 TTL
//
//        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
//        cacheConfigs.put("searchKeyword", defaultConfig.entryTtl(Duration.ofMinutes(10))); // 검색 캐시 10분
//        cacheConfigs.put("searchCacheLog", defaultConfig.entryTtl(Duration.ofMinutes(1))); // 로그용 캐시 1분
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(defaultConfig)
//                .withInitialCacheConfigurations(cacheConfigs)
//                .build();
//    }
//}
