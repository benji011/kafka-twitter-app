package constants;

import com.google.common.collect.Lists;

import java.util.List;

public final class Constants {
  private Constants() {}

  public static final String BOOTSTRAP_SERVER = "127.0.0.1:9092";
  public static final String GROUP_ID = "tweets-group";
  public static final String TOPIC = "tweets";

  // Twitter API credentials
  public static final String CONSUMER_API_KEY = System.getenv("CONSUMER_API_KEY");
  public static final String CONSUMER_API_SECRET = System.getenv("CONSUMER_API_SECRET");
  public static final String CONSUMER_BEARER_TOKEN = System.getenv("CONSUMER_BEARER_TOKEN");
  public static final String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");
  public static final String ACCESS_TOKEN_SECRET = System.getenv("ACCESS_TOKEN_SECRET");

  // Terms
  public static final List<String> TWITTER_TERMS = Lists.newArrayList("Brexit", "brexit");
}
