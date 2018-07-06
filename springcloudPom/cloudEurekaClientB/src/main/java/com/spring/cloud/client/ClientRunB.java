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
