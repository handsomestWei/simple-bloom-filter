package com.wjy.simple.bloomfilter;

public class SimpleBloomFilterTest {

    public static void main(String[] args) {
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(SimpleBloomFilter.DataScaleEnum.SMALL);
        String data = "";
        for (int i = 0; i < 6; i++) {
            System.out.println(">>>>>>>>>>>>>>> " + "loop " + i + " >>>>>>>>>>>>>>>");
            data = "test" + i;
            System.out.println(simpleBloomFilter.isExist(data));
            System.out.println(simpleBloomFilter.isExist(data));
        }
        simpleBloomFilter.clean();
        System.out.println(">>>>>>>>>>>>>>> " + "clean" +" >>>>>>>>>>>>>>>");
        System.out.println(simpleBloomFilter.isExist(data));
    }
}
