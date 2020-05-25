package io.dotanuki.fixtures;

public class Starter {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(Long.valueOf(args[0]));
        System.out.println("Done");
    }
}
