package com.wjy.simple.bloomfilter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class SimpleBloomFilter {

    // bit数
    private int bitNum;
    // hash函数数量
    private int hashCodeFuncNum;
    // TODO 命中率期望
    private int hitRate;
    // bit数组
    private char[] bitArray;
    // 哈希函数选择
    private Function<Object, Integer>[] hashCodeFuncArray;
    // bit数组容量
    private AtomicInteger capture = new AtomicInteger();

    public SimpleBloomFilter() {
        this(DataScaleEnum.NORMAL);
    }

    public SimpleBloomFilter(DataScaleEnum dataScale) {
        this(dataScale.bitNum, dataScale.hashCodeFuncNum, dataScale.hitRate);
    }

    public SimpleBloomFilter(int bitNum, int hashCodeFuncNum, int hitRate) {
        this.bitNum = bitNum;
        this.hashCodeFuncNum = hashCodeFuncNum;
        this.hitRate = hitRate;
        init();
    }

    public boolean isExist(Object obj) {
        if (obj == null) {
            return false;
        }
        if (capture.get() == 0) {
            return true;
        }
        return compareAndSet(obj);
    }

    public void clean() {
        init();
    }

    private void init() {
        char[] newBitArray = new char[bitNum];
        for (int i = 0; i < bitNum; i++) {
            newBitArray[i] = '0';
        }
        bitArray = newBitArray;
        capture = new AtomicInteger(bitNum);
        // TODO 使用多组哈希函数
        Function[] newHashCodeFuncArray = new Function[hashCodeFuncNum];
        for (int i = 0; i < this.hashCodeFuncNum; i++) {
            newHashCodeFuncArray[i] = new SimpleHashCode(i);
        }
        hashCodeFuncArray = newHashCodeFuncArray;
    }

    // 按位比较并保存
    private boolean compareAndSet(Object obj) {
        boolean isExist = true;
        for (Function func : hashCodeFuncArray) {
            int index = (Integer) func.apply(obj);
            if (bitArray[index] == '0') {
                isExist = false;
                capture.decrementAndGet();
            }
            bitArray[index] = '1';
        }
        return isExist;
    }

    // 数据规模
    public enum DataScaleEnum {

        SMALL(4,1, 60), NORMAL(8,1, 60), MEDIUM(16,1, 60), HIGH(32,1, 60);

        // bit数
        private int bitNum;
        // hash函数数量
        private int hashCodeFuncNum;
        // TODO 命中率期望
        private int hitRate;

        DataScaleEnum(int bitNum, int hashCodeFuncNum, int hitRate) {
            this.bitNum = bitNum;
            this.hashCodeFuncNum = hashCodeFuncNum;
            this.hitRate = hitRate;
        }
    }

    private class SimpleHashCode implements Function<Object, Integer> {

        private int seed;

        SimpleHashCode(int seed) {
            this.seed = seed;
        }

        @Override
        public Integer apply(Object o) {
            return Math.abs(o.hashCode() % bitNum);
        }
    }

}
