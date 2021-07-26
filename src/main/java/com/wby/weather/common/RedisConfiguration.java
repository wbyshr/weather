package com.wby.weather.common;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
    @Bean
    @Qualifier("redis6399db0Template")
    public StringRedisTemplate redis6399db0Template(
            @Value("${redis6399db0.host}") String host,
            @Value("${redis6399db0.port}") int port,
            @Value("${redis6399db0.password}") String password,
            @Value("${redis6399db0.index}") int index) {
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(
                connectionFactory(host, port, password, index));
        return temple;
    }

    public RedisConnectionFactory connectionFactory(
            String host, int port, String password, int index) {
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(host);
        jedis.setPort(port);
        if (!Strings.isNullOrEmpty(password)) {
            jedis.setPassword(password);
        }
        if (index != 0) {
            jedis.setDatabase(index);
        }
        jedis.setPoolConfig(poolConfig());
        jedis.afterPropertiesSet();
        return jedis;
    }

    public JedisPoolConfig poolConfig() {
        return new JedisPoolConfig();
    }
}
