package my.kafka.streams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableKafka
//@EnableKafkaStreams
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
//    public KafkaStreamsConfiguration kStreamsConfig() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost.digicert.com:32771");
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
//        return new KafkaStreamsConfiguration(props);
//    }

//    @Bean
//    public KStream<String, String> kStream(StreamsBuilder kStreamBuilder) {
//        KStream<String, String> stream = kStreamBuilder.stream("word-count-input");
//        stream
//                .mapValues(text -> text.toLowerCase())
//                .flatMapValues(textLine -> Arrays.asList(textLine.split("\\W+")))
//                .selectKey((key, word) -> word)
//                .groupByKey()
//                .count(Materialized.as("Counts"))
//                .toStream()
//                .to("word-count-output", Produced.with(Serdes.String(), Serdes.Long()));
//        return stream;
//    }
}
