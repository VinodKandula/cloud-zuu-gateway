package com.spring.zuulgateway.demo;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.concurrent.CountDownLatch;

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
           System.out.println(e.getMessage());
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
