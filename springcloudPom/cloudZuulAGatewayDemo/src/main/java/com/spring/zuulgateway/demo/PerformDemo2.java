package com.spring.zuulgateway.demo;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class PerformDemo2 implements  Runnable {

    private AtomicInteger aInteger;

    private CountDownLatch endLatch;

    private String url;

    private boolean isTimeOver;

    public PerformDemo2(boolean isTimeOver,AtomicInteger aInteger, CountDownLatch endLatch, String url){
        this.isTimeOver = isTimeOver;
        this.aInteger = aInteger;
        this.endLatch = endLatch;
        this.url = url;
    }

    @Override
    public void run() {
        try {
            demoReq(url);
        }catch (Exception e){
            //TODO
            throw new RuntimeException(e);
        }finally{
            if (!this.isTimeOver) {
                this.aInteger.incrementAndGet();
            }
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
