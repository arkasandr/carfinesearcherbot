package ru.arkasandr.carfinesearcher.config.rabbitmq;import lombok.RequiredArgsConstructor;import org.aopalliance.aop.Advice;import org.springframework.amqp.core.*;import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;import org.springframework.amqp.rabbit.connection.ConnectionFactory;import org.springframework.amqp.rabbit.core.RabbitTemplate;import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;import org.springframework.amqp.support.converter.MessageConverter;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.integration.annotation.IntegrationComponentScan;import org.springframework.integration.config.EnableIntegration;import org.springframework.retry.interceptor.RetryOperationsInterceptor;import ru.arkasandr.carfinesearcher.config.props.RabbitMQProperties;@Configuration@RequiredArgsConstructor@IntegrationComponentScan@EnableIntegrationpublic class RabbitMQCaptchaRequestWithValueMessageConfig {    public static final String QUEUE_NAME = "captchaValueResponse";    private final ConnectionFactory connectionFactory;    private final MessageConverter jsonMessageConverter;    private final RabbitMQProperties rabbitMQProperties;    @Bean    Queue captchaRequestWithValueQueue() {        return new Queue(rabbitMQProperties.getQueues().get(QUEUE_NAME).getQueue(), true);    }    @Bean    Exchange captchaRequestWithValueExchange() {        return ExchangeBuilder.directExchange(rabbitMQProperties.getQueues().get(QUEUE_NAME).getExchange())                .durable(true)                .build();    }    @Bean    Binding captchaRequestWithValueBinding() {        return BindingBuilder                .bind(captchaRequestWithValueQueue())                .to(captchaRequestWithValueExchange())                .with(rabbitMQProperties.getQueues().get(QUEUE_NAME).getRoutingkey())                .noargs();    }    @Bean    public RabbitTemplate captchaRequestWithValueTemplate(ConnectionFactory connectionFactory) {        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);        rabbitTemplate.setMessageConverter(jsonMessageConverter);        return rabbitTemplate;    }    @Bean    public SimpleMessageListenerContainer captchaRequestWithValueListenerContainer() {        SimpleMessageListenerContainer container =                new SimpleMessageListenerContainer(connectionFactory);        container.setQueues(captchaRequestWithValueQueue());        container.setConcurrentConsumers(2);        container.setDefaultRequeueRejected(false);        container.setAdviceChain(new Advice[]{captchaRequestWithValueInterceptor()});        return container;    }    @Bean    RetryOperationsInterceptor captchaRequestWithValueInterceptor() {        return RetryInterceptorBuilder.stateless()                .maxAttempts(1)                .backOffOptions(1000, 1, 60000)                .recoverer(new RejectAndDontRequeueRecoverer())                .build();    }}