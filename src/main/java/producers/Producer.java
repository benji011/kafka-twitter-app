package producers;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static constants.Constants.*;

public class Producer {
  Logger logger = LoggerFactory.getLogger(Producer.class.getName());

  public Producer() {
    /**/
  }

  public static void main(String[] args) {
    new Producer().run();
  }

  /** Run the Twitter client */
  public void run() {
    // Establish connection
    int capacity = 1000;
    BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(capacity);
    Client client = createClient(msgQueue);
    client.connect();

    // Kafka Producer
    KafkaProducer<String, String> producer = createKafkaProducer();

    // Shutdown hook
    shutdownHook(client, producer);

    // Loop to send tweets to Kafka
    while (!client.isDone()) {
      String msg = null;
      try {
        msg = msgQueue.poll(5, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
        client.stop();
      }
      if (msg != null) {
        // Create producer record
        ProducerRecord<String, String> record =
            new ProducerRecord<String, String>(TOPIC, null, msg);
        producer.send(
            record,
            new Callback() {
              @Override
              public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null) {
                  logger.error("An exception occurred: " + e);
                }
              }
            });
        producer.flush();
      }
      logger.info("Application has now finished execution.");
    }
  }

  /**
   * Create a Twitter client
   *
   * @param msgQueue
   * @return client instance
   */
  public Client createClient(BlockingQueue<String> msgQueue) {
    Hosts hbcHosts = new HttpHosts(Constants.STREAM_HOST);
    StatusesFilterEndpoint hbcEndpoint = new StatusesFilterEndpoint();

    hbcEndpoint.trackTerms(TWITTER_TERMS);
    Authentication hbcAuth =
        new OAuth1(CONSUMER_API_KEY, CONSUMER_API_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

    ClientBuilder builder =
        new ClientBuilder()
            .name("HBC-Client-01")
            .hosts(hbcHosts)
            .authentication(hbcAuth)
            .endpoint(hbcEndpoint)
            .processor(new StringDelimitedProcessor(msgQueue));

    return builder.build();
  }

  /**
   * Creates & returns a Kafka Producer instance.
   *
   * @return Kafka Producer instance
   */
  public KafkaProducer<String, String> createKafkaProducer() {

    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
    properties.setProperty(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    // Safe producer config
    properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
    properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
    // Compression config
    properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
    properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
    properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));
    return new KafkaProducer<String, String>(properties);
  }

  /**
   * A shutdown hook to send all messages left in memory to Kafka
   *
   * @param client
   * @param producer
   */
  public void shutdownHook(Client client, KafkaProducer<String, String> producer) {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  logger.info("Stopping application ...");
                  logger.info("Shutting down the client ...");
                  client.stop();
                  logger.info("Closing off the producer ...");
                  producer.close();
                  logger.info("Kafka app has completed shutdown.");
                }));
  }
}
