package SiO2;

import java.util.concurrent.Semaphore;

public class SiO2 {
    public static int NUM_RUNS;
    static Semaphore si;
    static Semaphore o;
    static Semaphore siHere;
    static Semaphore oHere;
    static Semaphore canBond;
    static Semaphore canLeave;

    public static void init(){
        si = new Semaphore(1);
        o = new Semaphore(2);
        siHere = new Semaphore(0);
        oHere = new Semaphore(0);
        canBond = new Semaphore(0);
    }
    public static void main(String[] args) {

    }

    public class Si extends Thread{
        public void bond(){
            System.out.println("Si is bonding..");
        }

        @Override
        public void run() {
            for(int i = 0;i<NUM_RUNS;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void execute() throws InterruptedException {
            si.acquire();
            siHere.release(2);
            oHere.acquire(2);
            canBond.release(2);
            bond();
            si.release();

        }

    }
    public class O extends Thread{
        public void bond(){
            System.out.println("O is bonding..");
        }
        @Override
        public void run() {
            for(int i = 0;i<NUM_RUNS;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void execute() throws InterruptedException {
            o.acquire();
            oHere.release();
            siHere.acquire();
            canBond.acquire();
            bond();
            o.release();

        }
    }
}
