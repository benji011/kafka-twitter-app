### About

A Proof Of Concept Kafka app that uses [Hosebird Client](https://github.com/twitter/hbc) to fetch Tweets from the Twitter API in the producer and then sends messages to the consumer in Kafka.

### Prerequisites
Set up your environment variables by renaming `.env.example` to `.env`

Obtain your consumer + access keys/secrets from [Twitter Developers](https://developer.twitter.com/)

```bash
CONSUMER_API_KEY=
CONSUMER_API_SECRET=
CONSUMER_BEARER_TOKEN=
ACCESS_TOKEN=
ACCESS_TOKEN_SECRET=
```


### Creating the Tweet topic

```bash
❯ kafka-topics --zookeeper localhost:2181 --create --topic tweets --partitions 6 --replication-factor 1
Created topic tweets.
❯ kafka-topics --zookeeper localhost:2181 --list
__consumer_offsets
...
tweets
❯ kafka-topics --zookeeper localhost:2181 --topic tweets --describe
Topic: tweets	TopicId: THMmg-WPSu-hTWZAXkhR2A	PartitionCount: 6	ReplicationFactor: 1	Configs:
	Topic: tweets	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
	Topic: tweets	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
	Topic: tweets	Partition: 2	Leader: 0	Replicas: 0	Isr: 0
	Topic: tweets	Partition: 3	Leader: 0	Replicas: 0	Isr: 0
	Topic: tweets	Partition: 4	Leader: 0	Replicas: 0	Isr: 0
	Topic: tweets	Partition: 5	Leader: 0	Replicas: 0	Isr: 0
```

### Run the producer

```bash
[main] INFO com.twitter.hbc.httpclient.BasicClient - New connection executed: HBC-Client-01, endpoint: /1.1/statuses/filter.json?delimited=length&stall_warnings=true
[hosebird-client-io-thread-0] INFO com.twitter.hbc.httpclient.ClientBase - HBC-Client-01 Establishing a connection
[main] INFO org.apache.kafka.clients.producer.ProducerConfig - ProducerConfig values: 
	acks = 1
	batch.size = 16384
	bootstrap.servers = [127.0.0.1:9092]
# ...
	transactional.id = null
	value.serializer = class org.apache.kafka.common.serialization.StringSerializer

[main] INFO org.apache.kafka.common.utils.AppInfoParser - Kafka version: 2.8.0
[main] INFO org.apache.kafka.common.utils.AppInfoParser - Kafka commitId: ebb1d6e21cc92130
[main] INFO org.apache.kafka.common.utils.AppInfoParser - Kafka startTimeMs: 1620788794216
[hosebird-client-io-thread-0] INFO com.twitter.hbc.httpclient.ClientBase - HBC-Client-01 Processing connection data
[kafka-producer-network-thread | producer-1] INFO org.apache.kafka.clients.Metadata - [Producer clientId=producer-1] Cluster ID: GtjjG4gXSp6ABvtgsd5A5A
[main] INFO producers.Producer - Application has now finished execution.
```

### Run a consumer following the `tweets` topic

Then see some messages containing tweets appear from the producer connected using HBC


```bash
❯ kafka-console-consumer --bootstrap-server localhost:9092 --topic tweets
{"created_at":"Wed May 12 03:04:42 +0000 2021","id":1392314765326888963,"id_str":"1392314765326888963","text":"RT @matheuslaneri: Zulu soltou um CONTINUA PORRA ali pra todo mundo que absolutamente todo mundo continuou andando sem nem responder #NoLim\u2026","source":"\u003ca href=\"https:\/\/mobile.twitter.com\" rel=\"nofollow\"\u003eTwitter Web App\u003c\/a\u003e","truncated":false,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":915737817812267008,"id_str":"915737817812267008","name":"Octa campe\u00e3o CARALHOOO","screen_name":"Winxyzinha","location":null,"url":null,"description":"Um doce carioca \u264c\n#flamengo ra\u00e7a, amor e paix\u00e3o\nEla\/Dela","translator_type":"none","protected":false,"verified":false,"followers_count":114,"friends_count":172,"listed_count":1,"favourites_count":19384,"statuses_count":6548,"created_at":"Thu Oct 05 00:38:04 +0000 2017","utc_offset":null,"time_zone":null,"geo_enabled":false,"lang":null,"contributors_enabled":false,"is_translator":false,"profile_background_color":"F5F8FA","profile_background_image_url":"","profile_background_image_url_https":"","profile_background_tile":false,"profile_link_color":"1DA1F2","profile_sidebar_border_color":"C0DEED","profile_sidebar_fill_color":"DDEEF6","profile_text_color":"333333","profile_use_background_image":true,"profile_image_url":"http:\/\/pbs.twimg.com\/profile_images\/1364057604230635524\/D4L1Rndz_normal.jpg","profile_image_url_https":"https:\/\/pbs.twimg.com\/profile_images\/1364057604230635524\/D4L1Rndz_normal.jpg","profile_banner_url":"https:\/\/pbs.twimg.com\/profile_banners\/915737817812267008\/1614051654","default_profile":true,"default_profile_image":false,"following":null,"follow_request_sent":null,"notifications":null,"withheld_in_countries":[]},"geo":null,"coordinates":null,"place":null,"contributors":null,"retweeted_status":{"created_at":"Wed May 12 02:26:54 +0000 2021","id":1392305251315769348,"id_str":"1392305251315769348","text":"Zulu soltou um CONTINUA PORRA ali pra todo mundo que absolutamente todo mundo continuou andando sem nem responder #NoLimite","source":"\u003ca href=\"http:\/\/twitter.com\/download\/android\" rel=\"nofollow\"\u003eTwitter for Android\u003c\/a\u003e","truncated":false,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":155263312,"id_str":"155263312","name":"DISTRIBUIDOR+GERADOR DE MEMES e AGITADOR CULTURAL","screen_name":"matheuslaneri","location":"S\u00e3o Paulo, Brasil","url":"https:\/\/www.obrasilquedeucerto.com.br","description":"O PERFIL DO DIBRE. Fa\u00e7o o @obqdc  @manchetejornabr e outras bobagens \n\nContato: matheuslaneri@gmail.com","translator_type":"none","protected":false,"verified":true,"followers_count":132894,"friends_count":1428,"listed_count":181,"favourites_count":3325,"statuses_count":112153,"created_at":"Sun Jun 13 16:43:09 +0000 2010","utc_offset":null,"time_zone":null,"geo_enabled":true,"lang":null,"contributors_enabled":false,"is_translator":false,"profile_background_color":"FFFFFF","profile_background_image_url":"http:\/\/abs.twimg.com\/images\/themes\/theme1\/bg.png","profile_background_image_url_https":"https:\/\/abs.twimg.com\/images\/themes\/theme1\/bg.png","profile_background_tile":true,"profile_link_color":"19CF86","profile_sidebar_border_color":"000000","profile_sidebar_fill_color":"FFD900","profile_text_color":"000000","profile_use_background_image":true,"profile_image_url":"http:\/\/pbs.twimg.com\/profile_images\/1297714122474377216\/HVprB1Lh_normal.jpg","profile_image_url_https":"https:\/\/pbs.twimg.com\/profile_images\/1297714122474377216\/HVprB1Lh_normal.jpg","profile_banner_url":"https:\/\/pbs.twimg.com\/profile_banners\/155263312\/1597352889","default_profile":false,"default_profile_image":false,"following":null,"follow_request_sent":null,"notifications":null,"withheld_in_countries":[]},"geo":null,"coordinates":null,"place":null,"contributors":null,"is_quote_status":false,"quote_count":51,"reply_count":39,"retweet_count":213,"favorite_count":1645,"entities":{"hashtags":[{"text":"NoLimite","indices":[114,123]}],"urls":[],"user_mentions":[],"symbols":[]},"favorited":false,"retweeted":false,"filter_level":"low","lang":"pt"},"is_quote_status":false,"quote_count":0,"reply_count":0,"retweet_count":0,"favorite_count":0,"entities":{"hashtags":[],"urls":[],"user_mentions":[{"screen_name":"matheuslaneri","name":"DISTRIBUIDOR+GERADOR DE MEMES e AGITADOR CULTURAL","id":155263312,"id_str":"155263312","indices":[3,17]}],"symbols":[]},"favorited":false,"retweeted":false,"filter_level":"low","lang":"pt","timestamp_ms":"1620788682668"}
{"created_at":"Wed May 12 03:06:32 +0000 2021","id":1392315224582234116,"id_str":"1392315224582234116","text":"n creio ja to arrependida\nnem tanto mas to kkjjjjjjjj","source":"\u003ca href=\"http:\/\/twitter.com\/download\/android\" rel=\"nofollow\"\u003eTwitter for Android\u003c\/a\u003e","truncated":false,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":3001191492,"id_str":"3001191492","name":"caba\u00e7a","screen_name":"euamopaodialho","location":"Los Angeles, CA","url":null,"description":"de saco t\u00e3o cheio quanto a gorda da sua m\u00e3e","translator_type":"none","protected":false,"verified":false,"followers_count":54,"friends_count":119,"listed_count":0,"favourites_count":430,"statuses_count":1928,"created_at":"Thu Jan 29 12:34:13 +0000 2015","utc_offset":null,"time_zone":null,"geo_enabled":false,"lang":null,"contributors_enabled":false,"is_translator":false,"profile_background_color":"C0DEED","profile_background_image_url":"http:\/\/abs.twimg.com\/images\/themes\/theme1\/bg.png","profile_background_image_url_https":"https:\/\/abs.twimg.com\/images\/themes\/theme1\/bg.png","profile_background_tile":false,"profile_link_color":"1DA1F2","profile_sidebar_border_color":"C0DEED","profile_sidebar_fill_color":"DDEEF6","profile_text_color":"333333","profile_use_background_image":true,"profile_image_url":"http:\/\/pbs.twimg.com\/profile_images\/1387610398132686852\/hiRxtedB_normal.jpg","profile_image_url_https":"https:\/\/pbs.twimg.com\/profile_images\/1387610398132686852\/hiRxtedB_normal.jpg","profile_banner_url":"https:\/\/pbs.twimg.com\/profile_banners\/3001191492\/1619745288","default_profile":true,"default_profile_image":false,"following":null,"follow_request_sent":null,"notifications":null,"withheld_in_countries":[]},"geo":null,"coordinates":null,"place":null,"contributors":null,"is_quote_status":false,"quote_count":0,"reply_count":0,"retweet_count":0,"favorite_count":0,"entities":{"hashtags":[],"urls":[],"user_mentions":[],"symbols":[]},"favorited":false,"retweeted":false,"filter_level":"low","lang":"pt","timestamp_ms":"1620788792163"}
# ...
```

### Optional: Checking your own tweets appear as a message

1. Tweet something from your account.

<p align="center">
 <img src="./sample-tweet.png"/>
</p>

2. See the data appear as a message in the consumer

```bash
❯ kafka-console-consumer --bootstrap-server localhost:9092 --topic tweets
# ...
{"created_at":"Wed May 12 03:10:05 +0000 2021","id":1392316121047465989,"id_str":"1392316121047465989","text":"Playing around with Kafka. It's pretty nice","source":"\u003ca href=\"https:\/\/mobile.twitter.com\" rel=\"nofollow\"\u003eTwitter Web App\u003c\/a\u003e","truncated":false,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":1361260709573578753,"id_str":"1361260709573578753","name":"Ben | Senior Software Engineer","screen_name":"benji0118","location":null,"url":"https:\/\/benjaminlo.io","description":"I write code, I eat pizza and I lift weights","translator_type":"none","protected":false,"verified":false,"followers_count":8,"friends_count":20,"listed_count":0,"favourites_count":17,"statuses_count":62,"created_at":"Mon Feb 15 10:27:29 +0000 2021","utc_offset":null,"time_zone":null,"geo_enabled":false,"lang":null,"contributors_enabled":false,"is_translator":false,"profile_background_color":"F5F8FA","profile_background_image_url":"","profile_background_image_url_https":"","profile_background_tile":false,"profile_link_color":"1DA1F2","profile_sidebar_border_color":"C0DEED","profile_sidebar_fill_color":"DDEEF6","profile_text_color":"333333","profile_use_background_image":true,"profile_image_url":"http:\/\/pbs.twimg.com\/profile_images\/1361261053586202624\/cZsy0mw1_normal.jpg","profile_image_url_https":"https:\/\/pbs.twimg.com\/profile_images\/1361261053586202624\/cZsy0mw1_normal.jpg","profile_banner_url":"https:\/\/pbs.twimg.com\/profile_banners\/1361260709573578753\/1614737916","default_profile":true,"default_profile_image":false,"following":null,"follow_request_sent":null,"notifications":null,"withheld_in_countries":[]},"geo":null,"coordinates":null,"place":null,"contributors":null,"is_quote_status":false,"quote_count":0,"reply_count":0,"retweet_count":0,"favorite_count":0,"entities":{"hashtags":[],"urls":[],"user_mentions":[],"symbols":[]},"favorited":false,"retweeted":false,"filter_level":"low","lang":"en","timestamp_ms":"1620789005897"}
# ...
```