package com.tsystems.dco;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = AppDatabase.class)
class AppDatabaseTest {

  @Autowired
  Environment environment;

  @Test
  void appDatabaseShouldStart() {
    Assertions.assertNotNull(environment);
  }
}
