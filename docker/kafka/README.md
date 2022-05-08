# Kafka Streams Application
The project illustrates how to develop Kafka Streams applications running Kafka in docker.

## Kafka in docker
In the ./docker/kafka directory,

1. To start Kafka

`docker-compose up -d`

2. To stop Kafka

`docker compose down`

3. To connect to the running broker

`docker exec -it kafkastreams-broker1-container /bin/bash`

## kcat tool
See https://github.com/edenhill/kcat for more information

To install on Mac OS: `brew install kcat`

### Commands
1. To list topics

`kcat -L -b localhost:9092`

2. To send transactions one at a time

a. start the console producer

`kcat -b localhost:9092 -t transactions-input-topic -K: -P << EOF`

b. send a messages using the format below. Terminate messages with an EOF token

`kcat -b localhost:9092 -t song-input-topic -K: -P << EOF
1:0snQkGI5qnAmohLE7jTsTn
EOF`

3. To send messages from a file

`kcat -P -b localhost:9092 -t song-input-topic -K: -l data/sample-songs.txt`

4. To read messages from a topic

`kcat -C -b localhost:9092 -t song-output-topic -f 'Topic %t [%p] at offset %o: key %k: %s\n' -q`
## kafka-producer-perf-test tool
1. To connect to broker
```
docker exec -it kafkastreams-broker1-container /bin/bash
```

2. Send random messages to a topic
```
kafka-producer-perf-test --producer-props bootstrap.servers=broker1:9092 --topic orders --throughput 1 --record-size 100 --num-records 10
```

3. Send messages from a file to a topic
```
kafka-producer-perf-test --producer-props bootstrap.servers=broker1:9092 --topic orders --throughput 1  --num-records 10 --payload-file /opt/kafka-data/sample-transactions.txt
```

## Steps to run the kafka streams demo application
1. Start kafka
2. Start the kafka streams application
3. Start a consumer to print messages in the result topic
4. Start a producer to produce messages to the input topic
5. Stop all producers and consumers and exit containers
6. Shutdown all containers



