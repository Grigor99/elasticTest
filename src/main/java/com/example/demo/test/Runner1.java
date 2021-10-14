//package com.example.demo.test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Runner1 {
//
//    @Autowired
//    private Job job;
//    @Async("taskScheduler2")
//    @Scheduled(fixedDelay = 1000)
//    void count() {
//        this.job.counter();
//        System.out.println("### RunnerOne: count: " + this.job.getCount());
//    }
//
//}
