package com.spring.zuulgateway.demo;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableDiscoveryClient
@Controller
public class ZuulAGatewayRun {

    private final int max_thread = 100;

    volatile boolean isTimeOver = false;

    private List<Map> list = new ArrayList<Map>();

   // public static void main(String[] args) throws InterruptedException { test();}
	public static void main(String[] args) {SpringApplication.run(ZuulAGatewayRun.class, args); }

    @GetMapping(value = "/hello")
    public @ResponseBody List hi(String count,String url,String type) throws InterruptedException {
        return httpReq(Integer.valueOf(count),new HashMap(),"http://localhost:8040/api/b/"+url+"?name=zcm",type);
    }
    @GetMapping(value = "/hellob")
    public @ResponseBody List hib(String count,String url,String type) throws InterruptedException {
        return httpReq(Integer.valueOf(count),new HashMap(),"http://localhost:8050/"+url+"?name=zcm",type);
    }

//   @GetMapping(value = "/hello")
//    public @ResponseBody List hi(String count,String url,String type) throws InterruptedException {
//        return httpReq2(Integer.valueOf(count),new HashMap(),"http://127.0.0.1:8040/api/b/"+url+"?name=zcm",type);
//    }
//    @GetMapping(value = "/hellob")
//    public @ResponseBody List hib(String count,String url,String type) throws InterruptedException {
//        return httpReq2(Integer.valueOf(count),new HashMap(),"http://127.0.0.1:8050/"+url+"?name=zcm",type);
//    }
    @RequestMapping(value = "/index")
    public String bigRequest(HashMap<String, Object> map,String type){
        map.put("hello","this is your home spring boot ");
        return "/demo";
    }
   private List httpReq(int count ,Map map,String url,String type) throws InterruptedException {
       //启动多个线程同时发起请求 设置100个线程
       ExecutorService executorService = Executors.newFixedThreadPool(max_thread);
       final CountDownLatch startLatch = new CountDownLatch(1);
       final CountDownLatch endLatch = new CountDownLatch(count);
       IntStream.range(0, count).forEach(i ->
               executorService.execute(new PerformDemo(startLatch, endLatch, url))
       );
       startLatch.countDown();
       long starttime = System.currentTimeMillis();
       endLatch.await();
       long endtime = System.currentTimeMillis();
       executorService.shutdownNow();
       while(!executorService.isTerminated()){
           //TODO
       }
       map.put("type","1".equals(type)?"zull":"gateway");
       map.put("time", endtime - starttime);
       list.add(map);
       return list;
   }

   public List httpReq2(int count ,Map map,String url,String type) throws InterruptedException {
       AtomicInteger executorCount = new AtomicInteger(0);
       ExecutorService executorService = Executors.newFixedThreadPool(max_thread);
       final CountDownLatch endLatch = new CountDownLatch(count);
       ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
       scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
           @Override
           public void run() {
               System.out.println("the time is "+new Date()+"  type is "+("1".equals(type)?"zull":"gateway"));
               IntStream.range(0, 600).forEach(i ->
                       executorService.execute(new PerformDemo2(isTimeOver,executorCount,endLatch, url))
               );
           }
       },1,100,TimeUnit.MILLISECONDS);
       scheduledExecutorService.awaitTermination(count+1,TimeUnit.SECONDS);
       isTimeOver = true;
       scheduledExecutorService.shutdownNow();
       while(!scheduledExecutorService.isTerminated()){
           //TODO
       }
       executorService.shutdownNow();
       while(!executorService.isTerminated()){
           //TODO
       }
       map.put("type","1".equals(type)?"zull":"gateway");
       map.put("count",executorCount.intValue());
       list.add(map);
       isTimeOver = false;
       return list;
   }

    public static void test() throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("the time is "+new Date());
            }
        },1,1,TimeUnit.SECONDS);
        scheduledExecutorService.awaitTermination(10,TimeUnit.SECONDS);
        scheduledExecutorService.shutdownNow();
    }

}
