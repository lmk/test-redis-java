package com.example.testredis;


public class Report {
    private static Report instance = null;

    private static volatile int refCount=0;

    private static volatile int sum =0;

    private static volatile int maxRefCount=0;

    private static volatile long start=0;

    private Report() {
    }

    public static Report getInstance() {
        if (instance == null) {
            instance=  new Report();
        }

        return instance;
    }

    public synchronized void Init() {
        refCount = 0;
        sum=0;
        maxRefCount=0;
        start=0;
    }

    public synchronized void Ref(int count) {
        refCount++;
        maxRefCount = refCount;
        sum += count;

        if (start == 0)
        {
            start = System.currentTimeMillis();
        }
    }

    public synchronized void UnRef() {
        refCount--;
    }

    public synchronized int Count() {
        return refCount;
    }

    public synchronized String Done() {

        String msg = "";

        if (refCount <= 0 ) {
            long duration = System.currentTimeMillis() - start;
            msg = String.format("duration: %d count: %d client: %d tps:%f", duration, sum, maxRefCount, (1000.0*sum)/duration);
        }

        return msg;
    }
}
