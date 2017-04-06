package com.pratice.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
@ComponentScan("com.practice.*")
public class RedisConfiguration /* extends CachingConfigurerSupport */ {

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
    connectionFactory.setHostName("localhost");
    connectionFactory.setPort(6379);
    return connectionFactory;
  }

  /*  @Override
  @Bean
  public KeyGenerator keyGenerator() {

    return new KeyGenerator() {
      @Override
      public Object generate(Object o, Method method, Object... objects) {
        // This will generate a unique key of the class name, the method name,
        // and all method parameters appended.
        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getName());
        sb.append(method.getName());
        for (Object obj : objects) {
          sb.append(obj.toString());
        }
        return sb.toString();
      }
    };
  }*/

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    // redisTemplate.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
    return redisTemplate;
  }

  @Bean
  public CacheManager cacheManager() {
    RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
    cacheManager.setDefaultExpiration(5);
    return cacheManager;
  }

}
