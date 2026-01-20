package org.inventoryManagement.service;

import org.inventoryManagement.model.Product;
import org.inventoryManagement.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {
    InventoryService inventoryService;
    Product product;
    @BeforeEach
    void setUp(){
        inventoryService = new InventoryService(new InventoryRepository());
        product = new Product(1, "Table", 1800.0);
        inventoryService.addProduct(product);
    }
    @Test
    void testAddInventoryWithConcurrency() throws InterruptedException {
        inventoryService.addInventory(1, 100);
        int threads = 10;
        CountDownLatch countDownLatch= new CountDownLatch(threads);
        for(int i=0;i<threads;i++){

            int finalI = i;
            new Thread(()->{
                     try {
                         if(finalI %2 ==1){
                             inventoryService.addInventory(1, 20);
                         }else{
                             inventoryService.removeInventory(1,10);
                         }

                     }finally {
                         countDownLatch.countDown();
                     }
                }).start();
        }
        countDownLatch.await();
        assertEquals(150, inventoryService.getQuantity(1));
    }
}