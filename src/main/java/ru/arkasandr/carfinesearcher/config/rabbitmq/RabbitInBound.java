package ru.arkasandr.carfinesearcher.config.rabbitmq;import lombok.AllArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.amqp.rabbit.connection.ConnectionFactory;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.integration.amqp.dsl.Amqp;import org.springframework.integration.annotation.IntegrationComponentScan;import org.springframework.integration.config.EnableIntegration;import org.springframework.integration.dsl.IntegrationFlow;import org.springframework.integration.dsl.IntegrationFlows;@Slf4j@Configuration@AllArgsConstructor@IntegrationComponentScan@EnableIntegrationpublic class RabbitInBound {    private final RabbitMQRequestMessageConfig rabbitMQRequestMessageConfig;//    private final RabbitMQResponseMessageConfig rabbitServerConfig;////    private final RabbitMQStatusMessageConfig rabbitStatusConfig;    private final ConnectionFactory connectionFactory;    @Bean    public IntegrationFlow outboundFlow() {        return IntegrationFlows.from("clientChannel")                .log()                .handle(Amqp.outboundAdapter(rabbitMQRequestMessageConfig.rabbitTemplate()))                .log()                .get();    }//    @Bean//    public IntegrationFlow inboundFlowGetStatus() {//        return IntegrationFlows.from(//                Amqp.inboundAdapter(rabbitStatusConfig.statusWorkListenerContainer()))//                .log()//                .handle("messageService", "getRequestStatus")//                .get();//    }////    @Bean//    public IntegrationFlow inboundFlow() {//        return IntegrationFlows.from(//                Amqp.inboundAdapter(rabbitServerConfig.workListenerContainer()))//                .log()//                .handle("messageService", "getResponseFromQueue")//                .get();//    }}