package Si2O3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Si2O3 {
    public static int num = 10;
    static Semaphore si;
    static Semaphore o;
    static Semaphore siHere;
    static Semaphore oHere;
    static Semaphore lock;
    static Semaphore ready;
    static Semaphore finishedBonding;
    static Semaphore canLeave;
    static int count;

    public static void init() {
        si = new Semaphore(2);
        o = new Semaphore(3);
        siHere = new Semaphore(0);
        oHere = new Semaphore(0);
        lock = new Semaphore(1);
        ready = new Semaphore(0);
        finishedBonding = new Semaphore(0);
        count = 0;
        canLeave = new Semaphore(0);
    }

    public static void main(String[] args) throws InterruptedException {
        init();
        List<Si> silicium = new ArrayList<>();
        List<O> oxygen = new ArrayList<>();
        for (int i = 0;i<400;i++){
            silicium.add(new Si());
        }
        for (int i = 0;i<600;i++){
            oxygen.add(new O());
        }
        for (Si s:silicium){
            s.start();
        }
        for (O o:oxygen){
            o.start();
        }
        for (Si s:silicium){
            s.join();
        }
        for (O o:oxygen){
            o.join();
        }




    }

    static class O extends Thread {
        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void execute() throws InterruptedException {
            o.acquire();
            siHere.acquire();
            oHere.release();
            ready.acquire();
            bond();
            finishedBonding.release();
            canLeave.acquire();

            o.release();

        }
        public void bond(){
            System.out.println("O bonding");
        }
    }

    static class Si extends Thread {
        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        public void execute() throws InterruptedException {
            si.acquire();
            lock.acquire();

            if (count == 0) {
                count++;
                lock.release();

                siHere.release(3);
                oHere.acquire(3);
                ready.release(4);
                bond();
                finishedBonding.acquire(4);
                canLeave.release(4);
                count--;
                System.out.println("aa");

            } else {
                lock.release();
                ready.acquire();
                bond();
                finishedBonding.release();
                canLeave.acquire();
            }
            si.release();

        }

        public void bond() {
            System.out.println("Si bonding");
        }
    }
}
