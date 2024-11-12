package com.interview_master.config;

import static com.interview_master.common.constant.Constant.COLLECTION_GROUP_ID;
import static com.interview_master.common.constant.Constant.QUIZ_GROUP_ID;
import static com.interview_master.common.constant.Constant.USER_GROUP_ID;

import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

  @Value("${kafka.boostrap.servers")
  private String bootstrapServers;

  @Bean
  public ConsumerFactory<String, User> userDeleteConsumerFactory() {
    Map<String, Object> configProps = getConfigProps(USER_GROUP_ID);
    return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
        new JsonDeserializer<>(User.class));
  }

  @Bean
  public ConsumerFactory<String, Collection> collectionDeleteConsumerFactory() {
    Map<String, Object> configProps = getConfigProps(COLLECTION_GROUP_ID);
    return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
        new JsonDeserializer<>(Collection.class));
  }

  @Bean
  public ConsumerFactory<String, Quiz> quizDeleteConsumerFactory() {
    Map<String, Object> configProps = getConfigProps(QUIZ_GROUP_ID);
    return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
        new JsonDeserializer<>(Quiz.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, User> userDeleteKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, User> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(userDeleteConsumerFactory());

    return factory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Collection> collectionDeleteKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Collection> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(collectionDeleteConsumerFactory());

    return factory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Quiz> quizDeleteKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Quiz> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(quizDeleteConsumerFactory());

    return factory;
  }


  private Map<String, Object> getConfigProps(String groupId) {
    Map<String, Object> configProps = new HashMap<>();

    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        ErrorHandlingDeserializer.class);
    configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
    configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return configProps;
  }
}
