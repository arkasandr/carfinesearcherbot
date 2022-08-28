package ru.arkasandr.carfinesearcher.config.rabbitmq;import lombok.RequiredArgsConstructor;import org.springframework.amqp.core.AmqpAdmin;import org.springframework.amqp.core.Queue;import org.springframework.amqp.rabbit.connection.ConnectionFactory;import org.springframework.amqp.rabbit.core.RabbitAdmin;import org.springframework.amqp.rabbit.core.RabbitTemplate;import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;import org.springframework.amqp.support.converter.MessageConverter;import org.springframework.beans.factory.annotation.Value;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.integration.annotation.IntegrationComponentScan;import org.springframework.integration.config.EnableIntegration;@Configuration@RequiredArgsConstructor@IntegrationComponentScan@EnableIntegrationpublic class RabbitMQGibddRequestMessageWithCarDataConfig {    @Value("${spring.rabbitmq.queue_client}")    private String queue;    @Value("${spring.rabbitmq.exchange_client}")    private String exchange;    @Value("${spring.rabbitmq.routingkey_client}")    private String routingKey;    private final ConnectionFactory connectionFactory;    @Bean    public AmqpAdmin amqpAdmin() {        return new RabbitAdmin(connectionFactory);    }    @Bean    public RabbitTemplate rabbitTemplate() {        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);        rabbitTemplate.setExchange(exchange);        rabbitTemplate.setRoutingKey(routingKey);        rabbitTemplate.setConnectionFactory(connectionFactory);        rabbitTemplate.setMessageConverter(jsonMessageConverter());        return rabbitTemplate;    }    @Bean    Queue queue() {        return new Queue(queue, true);    }    @Bean    public MessageConverter jsonMessageConverter() {        return new Jackson2JsonMessageConverter();    }}