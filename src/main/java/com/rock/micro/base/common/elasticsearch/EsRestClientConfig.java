package com.rock.micro.base.common.elasticsearch;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import org.apache.http.HttpConnection;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 本配置目的是 解决 长时间不调用ES,连接超时被重置的问题(也就是说,每天都会报一次es连接重置的错误)
 */
@Configuration
public class EsRestClientConfig {

    private static final Logger LOG = LoggerFactory.getLogger(EsRestClientConfig.class);

    @ApiModelProperty("地址")
    @Value("${spring.elasticsearch.uris}")
    private String uris;

    @ApiModelProperty("用户名")
    @Value("${spring.elasticsearch.username}")
    private String username;

    @ApiModelProperty("密码")
    @Value("${spring.elasticsearch.password}")
    private String password;

    /**
     * 创建 RestHighLevelClient Bean
     *
     * @return RestHighLevelClient Elasticsearch 高级客户端实例，用于执行各种 ES 操作
     */
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        //创建地址列表
        List<HttpHost> hostList = new ArrayList<>();
        //循环uri
        for (String uri : this.uris.split(",")) {
            //加入
            hostList.add(HttpHost.create(uri));
        }

        //创建 RestClientBuilder
        RestClientBuilder restClientBuilder = RestClient.builder(hostList.toArray(new HttpHost[]{}));

        //配置请求相关的参数：连接超时、读取超时等
        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            //Socket 读超时，单位毫秒，30秒
            requestConfigBuilder.setSocketTimeout(30000);
            //连接超时，单位毫秒，30秒
            requestConfigBuilder.setConnectTimeout(30000);
            //从连接池获取连接的超时，单位毫秒，30秒
            requestConfigBuilder.setConnectionRequestTimeout(30000);
            //返回
            return requestConfigBuilder;
        });

        //配置 HTTP 客户端相关参数
        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {

            //设置最大连接数，防止连接数过多造成资源耗尽
            httpClientBuilder.setMaxConnTotal(30);

            //自定义连接保持（Keep-Alive）策略，定义每个连接存活时间
            httpClientBuilder.setKeepAliveStrategy((response, context) -> {
                //这里打印当前使用的连接，方便调试和监控
                HttpConnection conn = (HttpConnection) context.getAttribute("http.connection");
                LOG.info("Used Elastic Search Conn:{}", conn);
                //返回连接保持时间，1分钟（单位毫秒）
                return TimeUnit.MINUTES.toMillis(1L);
            });

            //如果配置了用户名和密码，设置基本认证（Basic Auth）
            if (ObjectUtil.isNotEmpty(this.username) && ObjectUtil.isNotEmpty(this.password)) {
                //初始化basic对象
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                //设置任意认证范围都使用该凭证
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.username, this.password));
                //设置默认的认证提供者
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }

            //返回配置
            return httpClientBuilder;
        });

        //返回基于以上配置构建的 RestHighLevelClient
        return new RestHighLevelClient(restClientBuilder);
    }

    /**
     * ElasticsearchRestTemplate 模板类构造器
     */
    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate(RestHighLevelClient client) {
        return new ElasticsearchRestTemplate(client);
    }

}