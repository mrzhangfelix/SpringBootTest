package com.felix;

import com.felix.bean.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot02AmqpApplicationTests {

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	AmqpAdmin amqpAdmin;

	@Test
	public void createExchange(){

		amqpAdmin.declareExchange(new DirectExchange("direct.exchange"));
//		amqpAdmin.declareExchange(new FanoutExchange("fanout.exchange"));
//		amqpAdmin.declareExchange(new TopicExchange("topic.exchange"));
		System.out.println("创建完成");

		amqpAdmin.declareQueue(new Queue("felix.queue1",true));
		amqpAdmin.declareQueue(new Queue("felix.queue2",true));
		amqpAdmin.declareQueue(new Queue("felix.queue3",true));
		amqpAdmin.declareQueue(new Queue("felix.queue4",true));
//		创建绑定规则

		amqpAdmin.declareBinding(new Binding("felix.queue1", Binding.DestinationType.QUEUE,"direct.exchange","felix.queue1",null));
		amqpAdmin.declareBinding(new Binding("felix.queue2", Binding.DestinationType.QUEUE,"direct.exchange","felix.queue",null));
		amqpAdmin.declareBinding(new Binding("felix.queue3", Binding.DestinationType.QUEUE,"direct.exchange","felix.queue",null));
		amqpAdmin.declareBinding(new Binding("felix.queue4", Binding.DestinationType.QUEUE,"direct.exchange","felix.queue",null));

		//amqpAdmin.de
	}

	/**
	 * 1、单播（点对点）
	 */
	@Test
	public void contextLoads() {
		//Message需要自己构造一个;定义消息体内容和消息头
		//rabbitTemplate.send(exchage,routeKey,message);

		//object默认当成消息体，只需要传入要发送的对象，自动序列化发送给rabbitmq；
		//rabbitTemplate.convertAndSend(exchage,routeKey,object);
		Map<String,Object> map = new HashMap<>();
		map.put("msg","这是第一个消息");
		map.put("data", Arrays.asList("helloworld",123,true));
		//对象被默认序列化以后发送出去
		rabbitTemplate.convertAndSend("direct.exchange","felix.queue1",new Book("西游记","吴承恩"));

	}

	//接受数据,如何将数据自动的转为json发送出去
	@Test
	public void receive(){
		Object o = rabbitTemplate.receiveAndConvert("felix.queue1");
		System.out.println(o.getClass());
		System.out.println(o);
	}

	/**
	 * 广播
	 */
	@Test
	public void sendMsg(){
		rabbitTemplate.convertAndSend("exchange.fanout","",new Book("红楼梦","曹雪芹"));
	}

}
