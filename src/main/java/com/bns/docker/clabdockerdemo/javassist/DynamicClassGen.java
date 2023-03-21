package com.bns.docker.clabdockerdemo.javassist;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

public class DynamicClassGen {

    private static int ONE_MB = 1024 * 1024;

    public void generateLoad(int genClassCount, int memToConsumeMb, boolean sleepThread) throws InterruptedException {

        // number of classes to generate and instantiate
        //int genClassCount = Integer.parseInt(System.getenv("DYN_CLASS_COUNT"));

        // memory to consume by each instantiated object
        //int memToConsumeMb  = Integer.parseInt(System.getenv("MEM_USAGE_PER_OBJECT_MB"));


        System.out.println("Generating and instantiating " + genClassCount + " classes.");
        System.out.println("Each instantiated class will consume " + memToConsumeMb + " MB of memory.");
        System.out.println("Total classes will consume " + genClassCount * memToConsumeMb + " MB of memory.");
        System.out.println("Available CPUs:" + Runtime.getRuntime().availableProcessors());

        getMemInfo();

        List<MemoryConsumer> memoryConsumers = new ArrayList<>();

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(DynamicClassGen.class));

        try {

            for (int i = 0; i < genClassCount; i++) {
                // generate a new class from DynClassBase
                String className = DynClassBase.class.getCanonicalName();
                CtClass cc = pool.get(className);
                System.out.println("Instantiated "+className+" class isFrozen2 "+cc.isFrozen());
                cc.setName(className + i);
                Class genClass = cc.toClass();
                Class<?> []ctorPrms = null;

                Constructor ctor = genClass.getConstructor(ctorPrms);

                MemoryConsumer memConsumer = (MemoryConsumer)ctor.newInstance(null);
                memoryConsumers.add(memConsumer);

                System.out.println("Instantiated class " + memConsumer.getClass().getSimpleName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // gradually increase memory consumption
        for (MemoryConsumer consumer: memoryConsumers) {
            int memToConsumeInBytes = memToConsumeMb * ONE_MB;
            consumer.consumeSomeMemory(memToConsumeInBytes);

            System.out.println(consumer.getClass().getSimpleName() + " instance consuming " + memToConsumeMb + "MB");
        }

        getMemInfo();
        System.out.println("Make thread sleep: " + sleepThread);
        if(sleepThread) {
            while(true){
                Thread.sleep(1000);
            }
        }

    }

    public static void getMemInfo() {

        Runtime rt = Runtime.getRuntime();

        long maxMemBytes = rt.maxMemory();
        long usedMemBytes = rt.totalMemory() - rt.freeMemory();
        long freeMemBytes = rt.maxMemory() - usedMemBytes;

        System.out.println("*******************************************");
        System.out.println("Used memory: " + usedMemBytes/ONE_MB + "MB");
        System.out.println("Max memory: " + maxMemBytes/ONE_MB + "MB");
        System.out.println("Free memory: " + freeMemBytes/ONE_MB + "MB");
        System.out.println("*******************************************");
    }

    public static void main(String args[]) throws InterruptedException {
        DynamicClassGen d = new DynamicClassGen();
        d.generateLoad(11, 150, true);
    }

}
