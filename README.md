# Crawler2.0
Getting started:

- run a docker elastic container:
  - docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.12.0
- start the application