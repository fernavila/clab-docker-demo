package com.bns.docker.clabdockerdemo.javassist;

/**
 * This class is used as base to generate new classes from.
 */
public class DynClassBase implements MemoryConsumer{
    private byte []data = null; // data stored in heap


    public void consumeSomeMemory(int amountInBytes) {
        data = new byte[amountInBytes];
    }
}
