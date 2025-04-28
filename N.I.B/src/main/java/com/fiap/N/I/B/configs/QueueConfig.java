package com.fiap.N.I.B.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

     @Bean
     public Queue usuarioQueue() {
         return QueueBuilder.durable("fila-usuario").build();
     }

     @Bean
     public Exchange usuarioExchange() {
         return ExchangeBuilder.directExchange("usuarioExchange").build();
     }

     @Bean
     public Binding usuarioBinding(Queue usuarioQueue, Exchange usuarioExchange) {
         return BindingBuilder.bind(usuarioQueue).to(usuarioExchange).with("routingKey").noargs();
     }

     @Bean
     public Queue profissionalQueue() {
         return QueueBuilder.durable("fila-profissional").build();
     }

     @Bean
     public Exchange profissionalExchange() {
         return ExchangeBuilder.directExchange("profissionalExchange").build();
     }

     @Bean
     public Binding profissionalBinding(Queue profissionalQueue, Exchange profissionalExchange) {
         return BindingBuilder.bind(profissionalQueue).to(profissionalExchange).with("routingKey").noargs();
     }

     @Bean
     public ObjectMapper objectMapper() {
         ObjectMapper objectMapper = new ObjectMapper();
         objectMapper.registerModule(new JavaTimeModule());
         objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
         return objectMapper;
     }

     @Bean
     public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
         return new Jackson2JsonMessageConverter(objectMapper);
     }

     @Bean
     public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                          Jackson2JsonMessageConverter messageConverter) {
         RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
         rabbitTemplate.setMessageConverter(messageConverter);
         return rabbitTemplate;
     }
}
