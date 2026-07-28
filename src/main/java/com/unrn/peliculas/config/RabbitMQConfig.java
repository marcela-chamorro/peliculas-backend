package com.unrn.peliculas.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Value("${rabbitmq.event.compra.exchange.name:compra.exchange}")
    private String compraExchangeName;

    @Value("${rabbitmq.event.compra.queue.name:peliculas.stock.queue}")
    private String stockQueueName;

    @Value("${rabbitmq.event.compra.routing.key:compra.event}")
    private String compraRoutingKey;

    @Bean
    public TopicExchange compraExchange() {
        return new TopicExchange(compraExchangeName);
    }

    @Bean
    public org.springframework.amqp.core.Queue stockQueue() {
        return new org.springframework.amqp.core.Queue(stockQueueName, true);
    }

    @Bean
    public org.springframework.amqp.core.Binding stockBinding(
            org.springframework.amqp.core.Queue stockQueue,
            TopicExchange compraExchange) {
        return org.springframework.amqp.core.BindingBuilder
                .bind(stockQueue)
                .to(compraExchange)
                .with(compraRoutingKey);
    }
}

