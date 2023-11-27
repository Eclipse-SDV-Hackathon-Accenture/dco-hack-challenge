package com.tsystems.dco.redis.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.dco.redis.entity.RedisTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component

public class Publisher implements MessagePublisher {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private ChannelTopic topic;

  @Autowired
  private ObjectMapper mapper;

  public Publisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
    this.redisTemplate = redisTemplate;
    this.topic = topic;
  }

  public Publisher() {
  }

  @Override
  public void publisher(RedisTransport redisTransport) throws JsonProcessingException {
    var writer = mapper.writer();
    var redisTransportString = writer.writeValueAsString(redisTransport);
    redisTemplate.convertAndSend(topic.getTopic(), redisTransportString);
  }
}
