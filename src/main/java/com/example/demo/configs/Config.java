package com.example.demo.configs;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableElasticsearchRepositories(basePackages = {"com.example.demo.repo"})
public class Config {

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        RestHighLevelClient client = null;
        try {
            final CredentialsProvider credentialsProvider =
                    new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials("elastic", "djlwNA92ONuXs2PF1mfb3CY0"));
            SSLContextBuilder sslBuilder = null;
            sslBuilder = SSLContexts.custom()
                    .loadTrustMaterial(null, (x509Certificates, s) -> true);

            final SSLContext sslContext = sslBuilder.build();
            client = new RestHighLevelClient(RestClient
                    .builder(new HttpHost("c72e04bca1d149c2985874711a867da4.us-west-1.aws.found.io", 9243, "https"))
                    .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                        @Override
                        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                            return httpClientBuilder
                                    .setSSLContext(sslContext)
                                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                    .setDefaultCredentialsProvider(credentialsProvider);
                        }
                    })
                    .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                        @Override
                        public RequestConfig.Builder customizeRequestConfig(
                                RequestConfig.Builder requestConfigBuilder) {
                            return requestConfigBuilder.setConnectTimeout(5000)
                                    .setSocketTimeout(120000);
                        }
                    }));
            System.out.println("elasticsearch client created");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }


}