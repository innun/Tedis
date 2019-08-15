# Tedis 
```
 _____              _   _
|_   _|   ___    __| | (_)  ___
  | |    / _ \  / _` | | | / __|
  | |   |  __/ | (_| | | | \__ \
  |_|    \___|  \__,_| |_| |___/
```
Tedis is a redis client based on tcp & resp.

#### Features：

- [x] synchronous API
- [x] asynchronous API
- [x] connection pool
- [x] single node distributed lock
- [x] bloom-filter
- [x] validate conn activation & auto-retry
- [x] check issue permission(in annotation) using dynamic proxy
- [x] pipeline
- [x] subscribe/publish
- [x] Lua script
- [ ] SSL
- [ ] Redis Cluster

## How to use
* Request/Response mode
```java
public void pingTest() {
    Tedis tedis = TedisClient.tedis();
    tedis.ping();
    tedis.ping("HELLO");
    tedis.close();
}
```
* Pipeline
```java
public void pipelineTest() {
    Tedis tedis = TedisClient.tedis();
    tedis.setMode(TedisClient.PIPELINE);
    tedis.get("a");
    tedis.set("a", "1");
    tedis.get("a");
    Results r = tedis.submit().sync(); // nil, OK, 1
    tedis.close();
}
```
* SUB/PUB
```java
public void subpubTest() {
    Tedis tedis = TedisClient.tedis();
    tedis.subscribe("news");
    tedis.unsubscribe("news");
    tedis.psubscribe("n*");
    tedis.punsubscribe("n*");
    tedis.publish("news", "foobar");
    tedis.close();
}
```
* Lock
```java
public lockTest() {
    Tedis tedis = TedisClient.tedis();
    TedisLock lock = tedis.newLock();
    try {
       lock.lock();
       //...
    } finally {
        lock.unlock();
    }
    tedis.close();
}
```
* BloomFiter
```java
public bloomFilterTest() {
    Tedis tedis = TedisClient.tedis();
    // int a: insertions
    // double b: falseProbability
    BloomFilter bf = tedis.newBloomFilter(a, b);
    bf.add("str");
    assertTrue(bf.include("str"));
    tedis.close()
}
```

* More in Test Class

## Structure

![](/doc/structure.png)

