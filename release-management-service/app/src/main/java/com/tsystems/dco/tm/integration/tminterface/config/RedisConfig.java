package com.tsystems.dco.tm.integration.tminterface.config;

import com.tsystems.dco.redis.publisher.MessagePublisher;
import com.tsystems.dco.redis.publisher.Publisher;
import com.tsystems.dco.redis.subscriber.Subscriber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "redis-server")
public class RedisConfig {

  private String host;
  private int port;
  private String password;
  private String topic;

  @Bean
  LettuceConnectionFactory lettuceConnectionFactory() {
    var config = new RedisStandaloneConfiguration(host, port);
    config.setPassword(password);
    var factory = new LettuceConnectionFactory(config);
    return factory;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(lettuceConnectionFactory());
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
    template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
    return template;
  }

  @Bean
  MessageListenerAdapter messageListener(Subscriber subscriber) {
    return new MessageListenerAdapter(subscriber);
  }

  @Bean
  RedisMessageListenerContainer redisContainer(Subscriber subscriber) {
    final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(lettuceConnectionFactory());
    container.addMessageListener(messageListener(subscriber), topic());
    return container;
  }

  @Bean
  MessagePublisher redisPublisher() {
    return new Publisher(redisTemplate(), topic());
  }

  @Bean
  ChannelTopic topic() {
    return new ChannelTopic(topic);
  }
}
