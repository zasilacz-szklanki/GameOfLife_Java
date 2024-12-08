package org.example.model;

import org.junit.jupiter.api.Test;

public class DensityTest {
    @Test
    void test1() {
        Density d1=Density.LOW;
        Density d2=Density.MEDIUM;
        Density d3=Density.HIGH;

        assert(d1.getDensity()==10);
        assert(d2.getDensity()==30);
        assert(d3.getDensity()==50);

        assert(d1.getDensityDouble()==0.1);
        assert(d2.getDensityDouble()==0.3);
        assert(d3.getDensityDouble()==0.5);

        assert(d1.toString().equals("10 %"));
        assert(d2.toString().equals("30 %"));
        assert(d3.toString().equals("50 %"));
    }

    @Test
    void test2() {
        Density d1=Density.LOW;
        boolean[][] arr=d1.randomFill(10,10);
        int count=0;
        for (boolean[] booleans : arr) {
            for (boolean b : booleans) {
                if(b) {
                    count++;
                }
            }
        }
        System.out.println(count);
        System.out.println(d1);
        assert(count==d1.getDensity());
    }
    @Test
    void test3() {
        Density d1=Density.MEDIUM;
        boolean[][] arr=d1.randomFill(10,10);
        int count=0;
        for (boolean[] booleans : arr) {
            for (boolean b : booleans) {
                if(b) {
                    count++;
                }
            }
        }
        System.out.println(count);
        System.out.println(d1);
        assert(count==d1.getDensity());
    }
    @Test
    void test4() {
        Density d1=Density.HIGH;
        boolean[][] arr=d1.randomFill(10,10);
        int count=0;
        for (boolean[] booleans : arr) {
            for (boolean b : booleans) {
                if(b) {
                    count++;
                }
            }
        }
        System.out.println(count);
        System.out.println(d1);
        assert(count==d1.getDensity());
    }
}
