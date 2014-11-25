ddth-redis
==========

DDTH's [Redis](http://redis.io/) Libraries and Utilities: simplify Redis's usage.

Project home:
[https://github.com/DDTH/ddth-redis](https://github.com/DDTH/ddth-redis)

OSGi environment: `ddth-redis` is packaged as an OSGi bundle.


## License ##

See LICENSE.txt for details. Copyright (c) 2014 Thanh Ba Nguyen.

Third party libraries are distributed under their own licenses.


## Installation #

Latest release version: `0.4.0`. See [RELEASE-NOTES.md](RELEASE-NOTES.md).

Maven dependency:

```xml
<dependency>
	<groupId>com.github.ddth</groupId>
	<artifactId>ddth-redis</artifactId>
	<version>0.4.0</version>
</dependency>
```


## Usage ##

```java
// obtain a redis client factory
RedisClientFactory factory = RedisClientFactory.newFactory();

// obtain a redis client object from pool
IRedisClient client = factory.getRedisClient("localhost");
IRedisClient client = factory.getRedisClient("localhost", 6379);
IRedisClient client = factory.getRedisClient("localhost", 6379, "user", "password");
IRedisClient client = factory.getRedisClient("localhost", 6379, "user", "password", poolConfig);

//do something with the client
client.set("key", "value", ttl);
...

client.close(); //close and return the client to pool when done

factory.destroy(); //destroy the factory when done
```


## Credits ##

- [Jedis](https://github.com/xetorthio/jedis) is the underlying Redis library. 
