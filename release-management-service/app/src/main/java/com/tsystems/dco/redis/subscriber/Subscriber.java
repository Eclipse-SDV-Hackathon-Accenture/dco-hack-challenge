package com.tsystems.dco.redis.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.dco.redis.entity.RedisTransport;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@Data
@RequiredArgsConstructor
public class Subscriber implements MessageListener {
  private String workflowID;
  //private final WorkflowClient workflowClient;
  private final ObjectMapper mapper;


  @SneakyThrows
  @Override
  public void onMessage(Message message, byte[] pattern) {
    var redisTransport = mapper.readValue(message.toString(), RedisTransport.class);
   /*
    WorkflowOptions option = WorkflowOptions.newBuilder()
      .setTaskQueue("release-management-service").setWorkflowId(workflow.getWorkflowId())
      .build();
    WorkflowQualityGate workflow = workflowClient
      .newWorkflowStub(WorkflowQualityGate.class, option);

    workflow.updateApproval(String.valueOf(redisTransport));
    */log.info("Subscriber >>" + redisTransport);
  }
}
