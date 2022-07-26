package ru.arkasandr.carfinesearcher.config.rabbitmq;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.amqp.rabbit.connection.ConnectionFactory;import org.springframework.amqp.rabbit.core.RabbitTemplate;import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.integration.amqp.dsl.Amqp;import org.springframework.integration.annotation.IntegrationComponentScan;import org.springframework.integration.dsl.IntegrationFlow;import org.springframework.integration.dsl.IntegrationFlows;import org.springframework.integration.dsl.Transformers;import ru.arkasandr.carfinesearcher.service.message.MessageGateway;@Slf4j@Configuration@RequiredArgsConstructor@IntegrationComponentScan(basePackageClasses = MessageGateway.class)public class RabbitInBound {    private final RabbitTemplate rabbitTemplate;    private final SimpleMessageListenerContainer workListenerContainer;    @Bean    public IntegrationFlow outboundFlowSendGibddRequest() {        return IntegrationFlows.from("sendGibddCarDataChannel")                .log()                .transform(Transformers.toJson())                .log()                .handle(Amqp.outboundAdapter(rabbitTemplate))                .get();    }    @Bean    public IntegrationFlow inboundFlowReceiveGibddRequest() {        return IntegrationFlows.from(                Amqp.inboundAdapter(workListenerContainer))                .log("LOGGING")                .log()                .handle("messageService", "receiveMessageFromQueueWithCarData")                .get();    }}