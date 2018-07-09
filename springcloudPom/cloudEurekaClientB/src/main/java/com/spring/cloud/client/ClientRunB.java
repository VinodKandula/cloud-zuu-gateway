package com.spring.cloud.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
@Controller
public class ClientRunB {

	public static void main(String[] args) {
        SpringApplication.run(ClientRunB.class, args);
    }

   @RequestMapping(value = "/hello")
   public @ResponseBody String hi(@RequestParam(required = false) String name){
	    return "hello i am "+name;
   }

    @RequestMapping(value = "/hello1")
    public @ResponseBody String hi1(@RequestParam(required = false) String name)throws InterruptedException {
        Thread.sleep(1000);
        return "hello i am "+name;
    }

    @RequestMapping(value = "/hello2")
    public @ResponseBody String hi2(@RequestParam(required = false) String name)throws InterruptedException {
        Thread.sleep(2000);
        return "hello i am "+name;
    }

    @RequestMapping(value = "/hello3")
    public @ResponseBody String hi3(@RequestParam(required = false) String name) throws InterruptedException {
	    Thread.sleep(3000);
        return "hello i am "+name;
    }

    @RequestMapping(value = "/hello4")
    public @ResponseBody String hi4(@RequestParam(required = false) String name) throws InterruptedException {
        Thread.sleep(30000);
        return "hello i am "+name;
    }

    @RequestMapping(value = "/slowRequest")
    public @ResponseBody Map slowRequest() throws InterruptedException {
        Thread.sleep(1000);
        Map map = new HashMap();
        map.put("status","ok");
        return map;
    }

    @RequestMapping(value = "/bigRequest")
    public String bigRequest(HashMap<String, Object> map){
	    map.put("hello","this is your home spring boot ");
        return "/index";
    }
 
}
