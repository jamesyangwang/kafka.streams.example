package my.kafka.streams.runner;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
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

	private Topology createTopology() {
		
		log.info("Building output stream...");

		StreamsBuilder builder = new StreamsBuilder();
		
		// 1 - stream from Kafka
		KStream<String, String> textLines = builder.stream("word-count-input");

		KTable<String, Long> wordCounts = textLines
		
				// 2 - map values to lowercase
				// can be alternatively written as:
				// .mapValues(String::toLowerCase)
				.mapValues(textLine -> textLine.toLowerCase())
				
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

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost.digicert.com:32771");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        KafkaStreams streams = new KafkaStreams(createTopology(), config);
        streams.start();

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        // Update:
        // print the topology every 10 seconds for learning purposes
//        while(true){
//            streams.localThreadsMetadata().forEach(data -> System.out.println(data));
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                break;
//            }
//        }
        
		log.info("Exiting application...");
	}
}
