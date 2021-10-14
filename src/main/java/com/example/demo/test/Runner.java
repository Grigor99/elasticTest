//package com.example.demo.test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Runner {
//
//    @Autowired
//    private Job job;
//
//    @Async("taskScheduler")
//    @Scheduled(fixedDelay = 1500)
//    void count() {
//        this.job.counter();
//        System.out.println("### Runner: count: " + this.job.getCount());
//    }
//}
