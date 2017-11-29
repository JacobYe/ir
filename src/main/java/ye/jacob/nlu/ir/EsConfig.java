package ye.jacob.nlu.ir;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Configuration
public class EsConfig {

    @Value("${elasticsearch.cluster-name}")
    private String esClusterName;

    @Value("${elasticsearch.cluster-nodes}")
    private String esClusterNodes;

//    @Bean
//    public Client client() throws Exception {
//        Settings esSettings = Settings.settingsBuilder()
//                .put("cluster.name", "ir")
//                .build();
//        return TransportClient.builder()
//                .settings(esSettings)
//                .build()
//                .addTransportAddress(
//                        new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9400));
//    }
//
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
//        return new ElasticsearchTemplate(client());
//    }

    @Bean
    public TransportClient client() throws UnknownHostException {
        System.out.println("esCluster:" + esClusterName + " /// esNode:" + esClusterNodes);
        TransportAddress node = new TransportAddress(
                InetAddress.getByName("127.0.0.1"), 9300
        );

        Settings settings = Settings.builder()
                .put("cluster.name", "ir")
                .put("node.name", "jacob")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(node);
        return client;
    }

}
