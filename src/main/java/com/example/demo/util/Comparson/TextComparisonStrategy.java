package com.example.demo.util.Comparson;

import java.math.BigInteger;

public interface TextComparisonStrategy {
    public double getSemblance(BigInteger b1, BigInteger b2 );
    public BigInteger getHashOfStr(String str);
    public BigInteger getStrSimHash();
}
