package com.example.demo;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class MessageService {

  private String publicKey="BFFebg8NwkRm9gyZadmhiybdUPYwecvviguATSAPqHmhTdpiIVTm0fFyqK62tKwSG4EG2iSuD0Fg7pquO0yP8Hs";
  private String privateKey="68RRYaKN1kv01G19F6mnZPdRPzMjagC3zosOqGjalM4";

  private PushService pushService;
  private List<Subscription> subscriptions = new ArrayList<>();

  @PostConstruct
  private void init() throws GeneralSecurityException {
    Security.addProvider(new BouncyCastleProvider());
    pushService = new PushService(publicKey, privateKey);
  }

  public String getPublicKey() {
    return publicKey;
  }

  public Map<String, String> subscribe(Subscription subscription) {
    System.out.println("Subscribed to " + subscription.endpoint);
    this.subscriptions.add(subscription);
    HashMap<String, String> map = new HashMap<>();
    map.put("id", subscription.endpoint);
    return map;
  }

  public void unsubscribe(String endpoint) {
    System.out.println("Unsubscribed from " + endpoint);
    subscriptions = subscriptions.stream().filter(s -> !endpoint.equals(s.endpoint)).collect(Collectors.toList());
  }

  public void sendNotification(Subscription subscription, String messageJson) {
    try {
      pushService.send(new Notification(subscription, messageJson));
    } catch (GeneralSecurityException | IOException | JoseException | ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void sendNotifications() {
    System.out.println("Sending notifications to all subscribers");
    JSONObject obj = new JSONObject();

    obj.put("title", "New Product Available ");
    obj.put("text", "HEY! Take a look at this brand new t-shirt!");
    obj.put("image", "/images/jason-leung-HM6TMmevbZQ-unsplash.jpg");
    obj.put("tag", "new-product");
    obj.put("url", "/new-product-jason-leung-HM6TMmevbZQ-unsplash.html");


    subscriptions.forEach(subscription -> {
      sendNotification(subscription, obj.toString());
    });
  }

  @Scheduled(fixedRate = 15000)
  private void sendNotifications2() {
    System.out.println("Sending notifications to all subscribers");
    JSONObject obj = new JSONObject();

    obj.put("title", "New Product Available ");
    obj.put("text", "HEY! Take a look at this brand new t-shirt!");
    obj.put("image", "/images/jason-leung-HM6TMmevbZQ-unsplash.jpg");
    obj.put("tag", "new-product");
    obj.put("url", "/new-product-jason-leung-HM6TMmevbZQ-unsplash.html");


    subscriptions.forEach(subscription -> {
      sendNotification(subscription, obj.toString());
    });
  }
}
