//package com.kevin.growecom;
//
//import com.kevin.growecom.dto.order.CreateOrderRequest;
//import com.kevin.growecom.service.OrderService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@SpringBootTest
//public class OrderConcurrencyTest {
//    @Autowired
//    private OrderService orderService;
//
//
//    @Test
//    public void testConcurrentOrder() throws InterruptedException {
//        CreateOrderRequest request = new CreateOrderRequest();
//        request.setUserId(1L);
//
//        CreateOrderRequest.CartItemRequest item = new CreateOrderRequest.CartItemRequest();
//        item.setProductId(1L);
//        item.setQuantity(1);
//        request.setItems(List.of(item));
//
//        // manage threads
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        CountDownLatch latch = new CountDownLatch(1);
//
//        // count correctly in concurrent , int wrong bcs of race condition
//        AtomicInteger success = new AtomicInteger(0);
//        AtomicInteger failed = new AtomicInteger(0);
//
//        for (int i = 0; i < 2; i++) {
//            executor.submit(() -> {
//                try {
//                    latch.await();
//                    orderService.createOrder(request);
//                    success.incrementAndGet();
//                } catch (Exception e) {
//                    System.out.println("Error: " + e.getMessage());
//                    failed.incrementAndGet();
//                }
//            });
//        }
//        latch.countDown();
//        executor.awaitTermination(5, TimeUnit.SECONDS);
//        System.out.println("Success: " + success.get());
//        System.out.println("Failed: " + failed.get());
//
//        Assertions.assertEquals(1, success.get(), "Only one thread success");
//        Assertions.assertEquals(1, failed.get(), "One thread failed because of race condition");
//    }
//}
