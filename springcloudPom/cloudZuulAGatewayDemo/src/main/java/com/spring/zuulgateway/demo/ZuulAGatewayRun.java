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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableDiscoveryClient
@Controller
public class ZuulAGatewayRun {

    private final int max_thread = 400;

    private List<Map> list = new ArrayList<Map>();

	public static void main(String[] args) {
        SpringApplication.run(ZuulAGatewayRun.class, args);
    }

//    @GetMapping(value = "/hello")
//    public @ResponseBody List hi(String count,String url,String type) throws InterruptedException {
//        return httpReq(Integer.valueOf(count),new HashMap(),"http://localhost:8081"+url,type);
//    }
//    @GetMapping(value = "/hellob")
//    public @ResponseBody List hib(String count,String url,String type) throws InterruptedException {
//        return httpReq(Integer.valueOf(count),new HashMap(),"http://localhost:8082/"+url,type);
//    }

   @GetMapping(value = "/hello")
    public @ResponseBody List hi(String count,String url,String type) throws InterruptedException {
        return httpReq(Integer.valueOf(count),new HashMap(),"http://localhost:8040/api/b/"+url+"?name=zcm",type);
    }
    @GetMapping(value = "/hellob")
    public @ResponseBody List hib(String count,String url,String type) throws InterruptedException {
        return httpReq(Integer.valueOf(count),new HashMap(),"http://localhost:8050/"+url+"?name=zcm",type);
    }
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
}
class PerformDemo implements  Runnable {

    private CountDownLatch startLatch;

    private CountDownLatch endLatch;

    private String url;

    public PerformDemo(CountDownLatch startLatch,CountDownLatch endLatch,String url){
        this.startLatch = startLatch;
        this.endLatch = endLatch;
        this.url = url;
    }

    @Override
    public void run() {
        try {
            startLatch.await();
            demoReq(url);
        }catch (Exception e){
            //TODO
            throw new RuntimeException(e);
        }finally{
            endLatch.countDown();
        }
    }

    public static void demoReq(String url) {
        //1.使用默认的配置的httpclient
        CloseableHttpClient client = HttpClients.createDefault();
        //2.使用get方法
        HttpGet httpGet = new HttpGet(url);
        try {
            //3.执行请求，获取响应
            CloseableHttpResponse response = client.execute(httpGet);
        }catch(Exception e){
            //TODO
            throw new RuntimeException(e);
        }finally{

        }
    }
}