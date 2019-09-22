<h1>Disk LRU Cache</h1>
<br/>
<p>Disk LRU Cache is a simple spring boot app, which supports creation of a disk based cache with LRU eviction policy.</p>
<h2>Features!</h2>
<br/>
<ul>
  <li> Create multiple caches as per the need. </li>
  <li> Put elements into the create cache. </li>
  <li> Update elements into the cache created. </li>
  <li> Remove elements from the cache. </li>
  <li> Delete the cache. </li>
  <li> Handles the size when the cache size exceeds the limit with LRU eviction policy. </li>
  <li> Handles concurrency issues. </li>
</ul>
<br/>
<h2>Future Development</h2>
<br/>
<ul>
  <li>Junits and coverage</li>
  <li>Improve the use of files in the cache.</li>
  <li>Integration tests.</li>
  <li>Fine Tune the application.</li>
</ul>
<h2>Tech</h2>
<p>Spring Boot - runs stand-alone, production-grade Spring based Applications</p>

<h2>Installation</h2>
<p>FileDownnloder requires Java 8+ to run.

Install the dependencies and devDependencies and start the server.

$ cd filedownloader
$ mvn clean install
$ cd target/
$ java -jar *.jar

The Server is configured to start at 9000 port and contextPath fileDownloader.

http://{server:9000}/diskLru</p>

<h2>API Documentation</h2>
<ul>
  <li> <h4>Create Cache</h4>
  POST /diskLru/createCache HTTP/1.1
Host: localhost:9000
Content-Type: application/json
User-Agent: PostmanRuntime/7.15.2
Accept: */*
Cache-Control: no-cache
Postman-Token: 7758e8e0-59b5-4bfc-a70b-b0cdd5aa444a,30710de9-c188-48ca-bb8b-facf4d5d930e
Host: localhost:9000
Cookie: XSRF-TOKEN=e7eacb17-be50-4c1d-8de4-8249f195d559
Accept-Encoding: gzip, deflate
Content-Length: 67
Connection: keep-alive
cache-control: no-cache

{
	"fileLocation":"/Users/paig/Documents/diskCache",
	"maxSize":10
}
  </li>
  
  <li>
  <h4>Add entry into file</h4>
  POST /diskLru/putIntoCache?cacheId=1 HTTP/1.1
Host: localhost:9000
Content-Type: application/json
User-Agent: PostmanRuntime/7.15.2
Accept: */*
Cache-Control: no-cache
Postman-Token: 4ff5b9f1-3930-40b1-841e-ce36e49480f1,3a47a4f7-d10c-42bc-b963-445f554fe855
Host: localhost:9000
Cookie: XSRF-TOKEN=e7eacb17-be50-4c1d-8de4-8249f195d559
Accept-Encoding: gzip, deflate
Content-Length: 45
Connection: keep-alive
cache-control: no-cache

{
	"key":"random2",
	"value":"Random Value"
}
  </li>
  
  <li>
  <h4>Get entry from cache</h4>
  GET /diskLru/getFromCache?cacheId=1&amp; key=random3 HTTP/1.1
Host: localhost:9000
Content-Type: application/json
User-Agent: PostmanRuntime/7.15.2
Accept: */*
Cache-Control: no-cache
Postman-Token: 05ff00ff-cdf6-4ddb-80fd-a215fbaa0770,848b833a-bb80-4de0-85c9-9ffb4ca05df3
Host: localhost:9000
Cookie: XSRF-TOKEN=e7eacb17-be50-4c1d-8de4-8249f195d559
Accept-Encoding: gzip, deflate
Content-Length: 68
Connection: keep-alive
cache-control: no-cache
  </li>
  
  <li>
  <h4>Delete entry</h4>
  DELETE /diskLru/deleteFromCache?cacheId=1&amp; key=random2 HTTP/1.1
Host: localhost:9000
Content-Type: application/json
User-Agent: PostmanRuntime/7.15.2
Accept: */*
Cache-Control: no-cache
Postman-Token: 3992925b-f46f-41a0-8dfe-fffd4a7629c2,742374a7-e6e3-41dd-8d9c-879dfca384a6
Host: localhost:9000
Cookie: XSRF-TOKEN=e7eacb17-be50-4c1d-8de4-8249f195d559
Accept-Encoding: gzip, deflate
Content-Length: 68
Connection: keep-alive
cache-control: no-cache
  </li>
  
  <li>
  <h4>Delete cache</h4>
  DELETE /diskLru/deleteCache?filePath=/Users/paig/Documents/diskCache HTTP/1.1
Host: localhost:9000
Content-Type: application/json
User-Agent: PostmanRuntime/7.15.2
Accept: */*
Cache-Control: no-cache
Postman-Token: 70078605-f506-40bb-8a97-5ade20bb03e8,cc46ea2a-eb21-49fb-9a82-25acc3232d20
Host: localhost:9000
Cookie: XSRF-TOKEN=e7eacb17-be50-4c1d-8de4-8249f195d559
Accept-Encoding: gzip, deflate
Content-Length: 67
Connection: keep-alive
cache-control: no-cache
  </li>
</ul>  
