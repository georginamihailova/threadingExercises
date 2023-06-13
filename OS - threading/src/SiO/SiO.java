package SiO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


public class SiO {
    public static void main(String[] args) throws InterruptedException {
        List<Si> silicium = new ArrayList<>();
        List<O> oxygen = new ArrayList<>();
        init();
        for (int i = 0; i < 10; i++)
            silicium.add(new Si());

        for (int i = 0; i < 10; i++)
            oxygen.add(new O());
        for (Si s : silicium) {
            s.start();
        }
        for (O o : oxygen) {
            o.start();
        }
        for (Si s : silicium) {
            s.join();
        }
        for (O o : oxygen) {
            o.join();
        }


    }

    static Semaphore si;
    static Semaphore o;
    static Semaphore siHere;
    static Semaphore oHere;
    static Semaphore canBond;
    static Semaphore finishedBond;
    static int num;

    public static void init() {
        si = new Semaphore(1);
        o = new Semaphore(1);
        oHere = new Semaphore(0);
        siHere = new Semaphore(0);
        canBond = new Semaphore(0);
        finishedBond = new Semaphore(0);
        num = 10;
    }

    static class Si extends Thread {

        public void execute() throws InterruptedException {
            si.acquire();
            oHere.acquire();
            canBond.release();
            bond();
            finishedBond.acquire();

            si.release();

        }

        public void bond() {
            System.out.println("Si bonding..");
        }

        @Override
        public void run() {
            for (int i = 0; i < num; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class O extends Thread {
        public void execute() throws InterruptedException {
            o.acquire();
            oHere.release();
            canBond.acquire();
            bond();
            finishedBond.release();

            o.release();
        }

        @Override
        public void run() {
            for (int i = 0; i < num; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void bond() {
            System.out.println("O bonding..");
        }
    }
}
