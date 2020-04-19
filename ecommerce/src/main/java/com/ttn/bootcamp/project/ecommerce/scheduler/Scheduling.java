package com.ttn.bootcamp.project.ecommerce.scheduler;


import com.ttn.bootcamp.project.ecommerce.models.Seller;
import com.ttn.bootcamp.project.ecommerce.repos.SellerRepo;
import com.ttn.bootcamp.project.ecommerce.services.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Iterator;

@EnableAsync
public class Scheduling {

    @Autowired
    SendEmail sendEmail;

    @Autowired
    SellerRepo sellerRepo;

    @Async
    @Scheduled(cron = "0 0 12 * * ?")
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        Iterable<Seller> sellers = sellerRepo.findAll();
        Iterator<Seller> sellerIterator = sellers.iterator();
        while (sellerIterator.hasNext()) {
            Seller seller = sellerIterator.next();
            sendEmail.sendEmail("Orders Rejected or Cancelled", "Following orders has been cancelled",
                    seller.getEmail());
        }
    }
}

