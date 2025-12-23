package com.carumuch.capstone.estimate.infrastructure.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
	public static final String EXCHANGE = "damage-report-exchange";

	public static final String RK_ESTIMATE_REQUEST = "estimate.request";
	public static final String RK_ESTIMATE_RESULT  = "estimate.result";

	public static final String ESTIMATE_REQUEST_Q = "estimate.request.q";
	public static final String ESTIMATE_RESULT_Q  = "estimate.result.q";

	public static final String DLX_EXCHANGE = "damage-report-dlx";

	public static final String ESTIMATE_REQUEST_DLQ = "estimate.request.dlq";
	public static final String ESTIMATE_RESULT_DLQ = "estimate.result.dlq";

	public static final String RK_ESTIMATE_REQUEST_DLQ = "estimate.request.dlq";
	public static final String RK_ESTIMATE_RESULT_DLQ = "estimate.result.dlq";

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(EXCHANGE);
	}

	@Bean
	Queue estimateRequestQueue() {
		return QueueBuilder.durable(ESTIMATE_REQUEST_Q)
			.withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
			.withArgument("x-dead-letter-routing-key", RK_ESTIMATE_REQUEST_DLQ)
			.build();
	}

	@Bean
	Queue estimateResultQueue() {
		return QueueBuilder.durable(ESTIMATE_RESULT_Q)
			.withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
			.withArgument("x-dead-letter-routing-key", RK_ESTIMATE_RESULT_DLQ)
			.build();
	}

	@Bean
	Binding bindRequestQ(Queue estimateRequestQueue, TopicExchange exchange) {
		return BindingBuilder.bind(estimateRequestQueue).to(exchange).with(RK_ESTIMATE_REQUEST);
	}

	@Bean
	Binding bindResultQ(Queue estimateResultQueue, TopicExchange exchange) {
		return BindingBuilder.bind(estimateResultQueue).to(exchange).with(RK_ESTIMATE_RESULT);
	}

	@Bean
	TopicExchange dlx() {
		return new TopicExchange(DLX_EXCHANGE);
	}

	@Bean
	Queue estimateResultDlq() {
		return QueueBuilder.durable(ESTIMATE_RESULT_DLQ).build();
	}

	@Bean
	Binding bindResultDlq(Queue estimateResultDlq, TopicExchange dlx) {
		return BindingBuilder.bind(estimateResultDlq).to(dlx).with(RK_ESTIMATE_RESULT_DLQ);
	}

	@Bean
	Queue estimateRequestDlq() {
		return QueueBuilder.durable(ESTIMATE_REQUEST_DLQ).build();
	}

	@Bean
	Binding bindRequestDlq(Queue estimateRequestDlq, TopicExchange dlx) {
		return BindingBuilder.bind(estimateRequestDlq).to(dlx).with(RK_ESTIMATE_REQUEST_DLQ);
	}

	@Bean
	Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
		RabbitTemplate template = new RabbitTemplate(cf);
		template.setMessageConverter(converter);
		return template;
	}
}

