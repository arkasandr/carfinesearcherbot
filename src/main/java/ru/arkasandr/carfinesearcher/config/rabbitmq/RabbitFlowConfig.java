package ru.arkasandr.carfinesearcher.config.rabbitmq;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.integration.amqp.dsl.Amqp;import org.springframework.integration.annotation.IntegrationComponentScan;import org.springframework.integration.dsl.IntegrationFlow;import org.springframework.integration.dsl.IntegrationFlows;import org.springframework.integration.dsl.Transformers;import ru.arkasandr.carfinesearcher.service.message.MessageGateway;@Slf4j@Configuration@RequiredArgsConstructor@IntegrationComponentScan(basePackageClasses = MessageGateway.class)public class RabbitFlowConfig {    private final RabbitMQGibddRequestMessageWithCarDataConfig gibddRequestMessageWithCarDataConfig;    private final RabbitMQCaptchaRequestMessageConfig captchaRequestMessageConfig;    private final RabbitMQGibddResponseMessageWithCarDataConfig gibddResponseMessageWithCarDataConfig;    private final RabbitMQCaptchaResponseMessageConfig captchaResponseMessageConfig;    private final RabbitMQCaptchaResponseWithValueMessageConfig captchaResponseWithValueMessageConfig;    private final RabbitMQCaptchaRequestWithValueMessageConfig captchaRequestWithValueMessageConfig;    private final RabbitMQAnswerToUserRequestMessageConfig answerToUserRequestMessageConfig;    private final RabbitMQAnswerToUserResponseMessageConfig answerToUserResponseMessageConfig;    @Bean    public IntegrationFlow outboundFlowSendGibddRequest() {        return IntegrationFlows.from("sendGibddCarDataChannel")                .log()                .transform(Transformers.toJson())                .log()                .handle(Amqp.outboundAdapter(gibddRequestMessageWithCarDataConfig.rabbitTemplate()))                .get();    }    @Bean    public IntegrationFlow inboundFlowReceiveGibddRequest() {        return IntegrationFlows.from(                Amqp.inboundAdapter(gibddResponseMessageWithCarDataConfig.gibddResponseListenerContainer()))                .log()                .handle("messageService", "receiveMessageFromQueueWithCarData")                .get();    }    @Bean    public IntegrationFlow outboundFlowSendCaptchaRequest() {        return IntegrationFlows.from("sendCaptchaRequestChannel")                .log()                .transform(Transformers.toJson())                .log()                .handle(Amqp.outboundAdapter(captchaRequestMessageConfig.captchaRequestTemplate()))                .get();    }    @Bean    public IntegrationFlow inboundFlowReceiveCaptchaRequest() {        return IntegrationFlows.from(                Amqp.inboundAdapter(captchaResponseMessageConfig.captchaResponseListenerContainer()))                .log()                .handle("messageService", "receiveMessageFromQueueWithCaptcha")                .get();    }    @Bean    public IntegrationFlow outboundFlowSendCaptchaResponseWithValue() {        return IntegrationFlows.from("sendCaptchaResponseWithValueChannel")                .log()                .transform(Transformers.toJson())                .log()                .handle(Amqp.outboundAdapter(captchaResponseWithValueMessageConfig.captchaResponseWithValueTemplate()))                .get();    }    @Bean    public IntegrationFlow inboundFlowReceiveCaptchaValue() {        return IntegrationFlows.from(                Amqp.inboundAdapter(captchaRequestWithValueMessageConfig.captchaRequestWithValueListenerContainer()))                .log()                .handle("messageService", "receiveMessageFromQueueWithCaptchaValue")                .get();    }    @Bean    public IntegrationFlow outboundFlowSendAnswerToUser() {        return IntegrationFlows.from("sendAnswerToUserChannel")                .log()                .transform(Transformers.toJson())                .log()                .handle(Amqp.outboundAdapter(answerToUserRequestMessageConfig.answerToUserTemplate()))                .get();    }    @Bean    public IntegrationFlow inboundFlowReceiveAnswerToUser() {        return IntegrationFlows.from(                Amqp.inboundAdapter(answerToUserResponseMessageConfig.answerToUserResponseListenerContainer()))                .log()                .handle("messageService", "receiveAnswerToUserFromQueue")                .get();    }}