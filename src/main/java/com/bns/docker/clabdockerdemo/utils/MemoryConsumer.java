package com.bns.docker.clabdockerdemo.utils;

import java.util.Vector;

public class MemoryConsumer {
    private static float CAP = 0.1f;  // 80%
    private static int ONE_MB = 1024 * 1024;

    private static Vector cache = new Vector();

    public static void getMemInfo() {
        Runtime rt = Runtime.getRuntime();

        long maxMemBytes = rt.maxMemory();
        long usedMemBytes = rt.totalMemory() - rt.freeMemory();
        long freeMemBytes = rt.maxMemory() - usedMemBytes;
        //System.out.println("-Max maxMemBytes: " + maxMemBytes/ONE_MB + "MB");
        //System.out.println("-Max freeMemory: " + rt.freeMemory()/ONE_MB + "MB");
        //System.out.println("-Max usedMemBytes: " + usedMemBytes/ONE_MB + "MB");
        //System.out.println("-Max freeMemBytes: " + freeMemBytes/ONE_MB + "MB");

        int allocBytes = Math.round(freeMemBytes * CAP);

        System.out.println("Initial total memory: " + rt.totalMemory()/ONE_MB + "MB");
        System.out.println("Initial used memory: " + usedMemBytes/ONE_MB + "MB");
        System.out.println("Initial free memory: " + freeMemBytes/ONE_MB + "MB");
        System.out.println("Max memory: " + maxMemBytes/ONE_MB + "MB");

        System.out.println("Reserve: " + allocBytes/ONE_MB + "MB");

        int i = 0;
        for (; i < allocBytes / ONE_MB; i++){
            //System.out.println("adding " + i);
            cache.add(new byte[ONE_MB]);
        }
        System.out.println("adding " + i);

        usedMemBytes = rt.totalMemory() - rt.freeMemory();
        freeMemBytes = rt.maxMemory() - usedMemBytes;

        System.out.println("Free memory: " + freeMemBytes/ONE_MB + "MB");
    }

    public static void clean() {
        Runtime rt = Runtime.getRuntime();
        cache.clear();
        rt.gc();
    }

    public static void main(String[] args) {
        MemoryConsumer.getMemInfo();
    }
}
