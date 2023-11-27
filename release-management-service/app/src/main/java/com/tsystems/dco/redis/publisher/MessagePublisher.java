package com.tsystems.dco.redis.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tsystems.dco.redis.entity.RedisTransport;
import org.springframework.stereotype.Component;

@Component
public interface MessagePublisher {
  void publisher(RedisTransport releaseEntity) throws JsonProcessingException;
}
