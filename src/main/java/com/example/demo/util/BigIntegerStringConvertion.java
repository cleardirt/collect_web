package com.example.demo.util;

import java.math.BigInteger;

public class BigIntegerStringConvertion {
    public static String bigInteger2String(BigInteger bigInteger){
        return bigInteger.toString();
    }

    public static BigInteger string2BigInteger(String str){
        return new BigInteger(str);
    }
}
