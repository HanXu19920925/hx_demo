package com.example.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/***
 * redisson配置，根据不同环境读取不同redis配置
 * windows redis安装服务：
 * 进入cd C:\Software\redis目录，执行redis-server.exe --service-install redis.windows.conf --loglevel verbose命令
 * window redis卸载服务
 * 进入cd C:\Software\redis目录，执行redis-server.exe --service-uninstall命令
 */
@Configuration
public class RedissonConfig {

    @Value("${env}")
    private String env;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisNodes = this.getRedisNodes(env);
        if (env.equals("dev")) {
            config.useSingleServer().setAddress("redis://"+redisNodes.trim());
        } else if (env.equals("test")) {
            String[] redisNodeList = redisNodes.replace("[","").replace("]","").split(",");
            //集群部署方式
            ClusterServersConfig clusterServersConfig = config.useClusterServers()
                    //集群状态扫描间隔时间，单位是毫秒
                    .setScanInterval(2000);
            //添加节点
            for (String node : redisNodeList) {
                clusterServersConfig.addNodeAddress("redis://"+node.trim());
            }
        } else if (env.equals("prod")){
            String[] redisNodeList = redisNodes.replace("[","").replace("]","").split(",");
            //集群部署方式
            ClusterServersConfig clusterServersConfig = config.useClusterServers()
                    //集群状态扫描间隔时间，单位是毫秒
                    .setScanInterval(2000);
            //添加节点
            for (String node : redisNodeList) {
                clusterServersConfig.addNodeAddress("redis://"+node.trim());
            }
        }
        RedissonClient client = Redisson.create(config);
        return client;
    }

    /***
     * 读取test和prod的yml文件中的redis集群节点
     * @param env
     * @return
     */
    private String getRedisNodes(String env) {
        Yaml yaml = new Yaml();
        Map map = null;
        Resource resource = null;
        if (env.equals("dev")) {
            resource = new DefaultResourceLoader().getResource("classpath:application-dev.yml");
        } else if (env.equals("test")) {
            resource = new DefaultResourceLoader().getResource("classpath:application-test.yml");
        } else if (env.equals("prod")) {
            resource = new DefaultResourceLoader().getResource("classpath:application-prod.yml");
        }
        try {
            InputStream inputStream = resource.getInputStream();
            map = (Map) yaml.load(inputStream);
            String nodes = "";
            if (env.equals("dev")) {
                String host = ((Map)((Map) map.get("spring")).get("redis")).get("host").toString();
                String port = ((Map)((Map) map.get("spring")).get("redis")).get("port").toString();
                nodes = host+":"+port;
            }else{
                nodes = ((Map)((Map)((Map) map.get("spring")).get("redis")).get("cluster")).get("nodes").toString();
            }
            return nodes;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取test和prod的yml文件中的redis集群节点出现异常{}" + e.getMessage());
        }
        return null;
    }
}