package com.visiativ.moovapps.keycloak.providers.events.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.keycloak.Config.Scope;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

public class KafkaEventListenerProviderFactory implements EventListenerProviderFactory,
    ServerInfoAwareProviderFactory {

  private Producer<String, String> producer;

  public EventListenerProvider create(KeycloakSession keycloakSession) {
    return new KafkaEventListenerProvider(this.producer);
  }

  public void init(Scope scope) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "kafka:9092");
    props.put("acks", "all");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(null);
    this.producer = new KafkaProducer<String, String>(props);
    Thread.currentThread().setContextClassLoader(classLoader);
  }

  public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

  }

  public void close() {
    this.producer.close();
  }

  public String getId() {
    return "moovapps-kafka";
  }

  public Map<String, String> getOperationalInfo() {
    Map<String, String> ret = new HashMap<String, String>();
    ret.put("kafka-server", "kafka:9092");
    return ret;
  }
}
