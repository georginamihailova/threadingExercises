package Ca3N2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Ca3N2 {
    public static Semaphore caHere;
    public static Semaphore nHere;
    public static Semaphore c;
    public static Semaphore n;
    public static Semaphore lock;
    public static Semaphore canBond;
    public static Semaphore finishedBonding;
    public static int count;
    public static void init(){
        caHere = new Semaphore(0);
        nHere = new Semaphore(0);
        n = new Semaphore(2);
        c = new Semaphore(3);
        lock = new Semaphore(1);
        canBond = new Semaphore(0);
        finishedBonding = new Semaphore(0);
        count = 0;
    }

    static class C extends Thread{


        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        public void bond(){
            System.out.println("c is bonding.");
        }

        public void execute() throws InterruptedException {
         c.acquire();
         lock.acquire();
         count++;
         if(count == 3){
             count = 0;
             lock.release();
             nHere.acquire(2);
             canBond.release(3);
             bond();
             finishedBonding.acquire(3);
         }else{
             lock.release();
             canBond.acquire();
             bond();
             finishedBonding.release();
         }
         c.release();


        }
    }
    static class N extends Thread{


        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        public void bond(){
            System.out.println("n is bonding.");
        }
        public void execute() throws InterruptedException {
            n.acquire();
            nHere.release();
            canBond.acquire();
            bond();
            finishedBonding.release();

            n.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        init();
        List<C> c = new ArrayList<>();
        List<N> n = new ArrayList<>();
        for (int i=0;i<3;i++){
            c.add(new C());
        }
        for (int i=0;i<2;i++){
            n.add(new N());
        }
        for (C c1:c){
            c1.start();
        }
        for(N n1:n){
            n1.start();
        }
        for (C c1:c){
            c1.join();
        }
        for(N n1:n){
            n1.join();
        }



    }
}
