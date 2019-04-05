package my.kafka.streams.runner;

import org.springframework.boot.CommandLineRunner;

import lombok.extern.slf4j.Slf4j;

//https://docs.spring.io/spring-kafka/docs/current/reference/#kafka-streams

@Slf4j
//@Component
public class KafkaStreamsSpringRunner implements CommandLineRunner {

//	@Autowired
//	KStream<String, String> streams;

	@Override
	public void run(String... args) throws Exception {

		log.info("Starting the application...");

		log.info("Exiting application...");
	}
}
