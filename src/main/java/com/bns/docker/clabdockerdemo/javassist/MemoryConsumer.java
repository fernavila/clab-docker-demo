package com.bns.docker.clabdockerdemo.javassist;

public interface MemoryConsumer {
    /**
     * Allocate the given memory amount in heap
     * @param amountInBytes to be occupied in heap
     */
    void consumeSomeMemory(int amountInBytes);
}
