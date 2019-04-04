package my.kafka.streams.runner;

import java.util.Arrays;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaStreamsRunner implements CommandLineRunner {

//https://docs.spring.io/spring-kafka/docs/current/reference/#kafka-streams

	private Topology createTopology() {
		StreamsBuilder builder = new StreamsBuilder();
		// 1 - stream from Kafka

		KStream<String, String> textLines = builder.stream("word-count-input");
		KTable<String, Long> wordCounts = textLines
				// 2 - map values to lowercase
				.mapValues(textLine -> textLine.toLowerCase())
				// can be alternatively written as:
				// .mapValues(String::toLowerCase)
				// 3 - flatmap values split by space
				.flatMapValues(textLine -> Arrays.asList(textLine.split("\\W+")))
				// 4 - select key to apply a key (we discard the old key)
				.selectKey((key, word) -> word)
				// 5 - group by key before aggregation
				.groupByKey()
				// 6 - count occurences
				.count(Materialized.as("Counts"));

		// 7 - to in order to write the results back to kafka
		wordCounts.toStream().to("word-count-output", Produced.with(Serdes.String(), Serdes.Long()));

		return builder.build();
	}

	@Override
	public void run(String... args) throws Exception {

		log.info("Starting the application...");

		log.info("Exiting application...");
	}
}
