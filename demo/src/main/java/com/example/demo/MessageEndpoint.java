package com.example.demo;

import nl.martijndwars.webpush.Subscription;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MessageEndpoint {

  private MessageService messageService;

  public MessageEndpoint(MessageService messageService) {
    this.messageService = messageService;
  }

  public String getPublicKey() {
    return messageService.getPublicKey();
  }

  @CrossOrigin(origins = "http://localhost:8080")
  @PostMapping("/subscription")
  public Map<String, String> subscribe(@RequestBody Subscription userSubscrition) {
    return messageService.subscribe(userSubscrition);
  }

  @CrossOrigin(origins = "http://localhost:8080/")
  @GetMapping("/subscriptionNotify")
  public Map<String, String> subscriptionNotify() {
    HashMap<String, String> map = new HashMap<>();
    messageService.sendNotifications();
    map.put("", "");
    return map;
  }
  @CrossOrigin(origins = "http://localhost:8080/")
  @PostMapping("/unSubscribe")
  public Map<String, String> unsubscribe(@RequestBody Subscription userSubscrition) {
    HashMap<String, String> map = new HashMap<>();
    messageService.unsubscribe(userSubscrition.endpoint);
    map.put("", "");
    return map;
  }
}
