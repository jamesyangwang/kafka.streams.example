#!/bin/bash

cd $KAFKA_HOME/bin

# create input topic with two partitions
# $ZK = localhost.digicert.com
kafka-topics.sh --create --zookeeper $ZK --replication-factor 3 --partitions 3 --topic word-count-input

# create output topic
kafka-topics.sh --create --zookeeper $ZK --replication-factor 3 --partitions 3 --topic word-count-output

# launch a Kafka consumer
kafka-console-consumer.sh --bootstrap-server localhost.digicert.com:32771 \
    --topic word-count-output \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

# launch the streams application

# then produce data to it
kafka-console-producer.sh --broker-list localhost.digicert.com:32771 --topic word-count-input

# package your application as a fat jar
mvn clean package

# run your fat jar
java -jar <your jar here>.jar

# list all topics that we have in Kafka (so we can observe the internal topics)
kafka-topics.sh --list --zookeeper localhost.digicert.com:2181
