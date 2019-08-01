package com.visiativ.moovapps.keycloak.providers.events.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaEventListenerProvider implements EventListenerProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventListenerProvider.class);

  private final Producer<String, String> producer;

  public KafkaEventListenerProvider(
      Producer<String, String> producer) {
    this.producer = producer;
  }

  public void onEvent(Event event) {
    LOGGER.info("Event received {}", event);
    this.producer
        .send(new ProducerRecord<String, String>("users", event.getUserId(), event.getUserId()));
  }

  public void onEvent(AdminEvent adminEvent, boolean b) {
    LOGGER.info("AdminEvent received {}", adminEvent);
    this.producer.send(new ProducerRecord<String, String>("users", adminEvent.getRealmId(),
        adminEvent.getError()));
  }

  public void close() {

  }


}
