package com.example.crawler

import com.github.dockerjava.api.command.CreateContainerCmd
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.apache.http.HttpHost
import org.elasticsearch.client.Request
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName
import java.util.function.Consumer

class EmbeddedElastic {

    private val client: RestHighLevelClient
    private val container: ElasticsearchContainer

    fun shutdown(){
        client.close()
        container.stop()
    }

    init {

        var hostPort = 9200
        var containerExposedPort = 9200
        var cmd: Consumer<CreateContainerCmd> =
            Consumer<CreateContainerCmd> { e ->
                e.withPortBindings(
                    PortBinding(
                        Ports.Binding.bindPort(hostPort),
                        ExposedPort(containerExposedPort)
                    )
                )
            }

        container = ElasticsearchContainer(
            DockerImageName
                .parse("docker.elastic.co/elasticsearch/elasticsearch-oss")
                .withTag("7.10.1")
        )
            .withExposedPorts(containerExposedPort).withCreateContainerCmdModifier(cmd)

        if(!container.isRunning){
            container.start()
        }
        var httpPort = container.firstMappedPort
        var restClientbuilder = RestClient.builder(HttpHost("localhost", httpPort, "http"))
        client = RestHighLevelClient(restClientbuilder)
        client.lowLevelClient.performRequest(Request("PUT", "/articles"))
    }
}