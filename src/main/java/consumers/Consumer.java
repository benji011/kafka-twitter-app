package consumers;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static constants.Constants.*;

public class Consumer {
  /**
   * Create ElasticSearch client to use ES
   *
   * @return RestHighLevelClient instance
   */
  public static RestHighLevelClient createClient() {

    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(
        AuthScope.ANY,
        new UsernamePasswordCredentials(ELASTIC_SEARCH_USERNAME, ELASTIC_SEARCH_PASSWORD));

    RestClientBuilder builder =
        RestClient.builder(new HttpHost(ELASTIC_SEARCH_HOSTNAME, 443, "https"))
            .setHttpClientConfigCallback(
                httpClientBuilder ->
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
    return new RestHighLevelClient(builder);
  }

  public static void main(String[] args) throws IOException {
    Logger logger = LoggerFactory.getLogger(Consumer.class.getName());
    String jsonStringPayload = "{\"hogehoge\": \"foobar\"}";
    RestHighLevelClient client = createClient();
    IndexRequest request =
        new IndexRequest("twitter", "tweets").source(jsonStringPayload, XContentType.JSON);

    IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    String id = response.getId();
    logger.info(id);
    client.close();
  }
}
