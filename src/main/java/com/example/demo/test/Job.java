//package com.example.demo.test;
//
//import org.apache.logging.log4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Profile;
//import org.springframework.context.annotation.Scope;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Component
///*
//@Scope("prototype")
//*/
//public class Job {
////    public  volatile int count = 0;
//
//    private AtomicInteger count = new AtomicInteger(0);
//        void counter() {
//       /*  try {
//             Thread.sleep(500);
//              System.out.println(Thread.currentThread().getName());
//          } catch (InterruptedException e) {
//              e.printStackTrace();
//          }*/
//          count.getAndIncrement();
//    }
//
//    public int getCount() {
//        return count.get();
//    }
//
//
//}
////
////
////class Does{
////    public static void main(String[] args) {
////        Job job= new Job();
////        new Thread(){
////            @Override
////            public void run() {
////
////
////
////                Job job = new Job();
////
////
////
////                for(int i =0;i<20;i++){
////
////
////                    job.counter();
////                    System.out.println("one "+job.getCount());
////                }
////
////            }
////        }.start();
////        new Thread(){
////            @Override
////            public void run() {
////
////
////                Job job = new Job();
////
////
////
////                for (int i = 0; i < 20; i++) {
////
////                    job.counter();
////                    System.out.println("two " + job.getCount());
////                }
////
////            }
////        }.start();
////    }
////}
