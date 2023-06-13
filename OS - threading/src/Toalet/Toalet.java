package Toalet;

import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Toalet {
    static Semaphore women;
    static Semaphore men;
    static Semaphore toiletSemaphore;
    static int countMen = 0;
    static int countWomen = 0;
    public static void init(){
        women = new Semaphore(1);
        men = new Semaphore(1);
        toiletSemaphore = new Semaphore(1);
    }
    public void vlezi(){
        System.out.println("Vleguva..");
    }
    public void izlezi(){
        System.out.println("Izleguva..");
    }
    public static void zena_vleguva(){
        System.out.println("Zena vleguva..");
    }
    public static void zena_izleguva(){
        System.out.println("Zena izleguva..");

    }
    public static void maz_vleguva(){
        System.out.println("Maz vleguva..");

    }
    public static void maz_izleguva(){
        System.out.println("Maz izleguva..");

    }

    static class Men extends Thread{
        private Toalet toalet;

        public Men(Toalet toalet) {
            this.toalet = toalet;
        }
        public void enter(){

        }
        public void exit(){

        }

//        public void execute() throws InterruptedException {
//            men.acquire();
//            if(countMen == 0){
//                lock.acquire();
//            }
//            men.release();
//            toalet.maz_vleguva();
//            men.acquire();
//            countMen--;
//            men.release();
//            toalet.maz_izleguva();
//            if(countMen == 0){
//                lock.release();
//            }
//
//        }
    }
    static class Women extends Thread{
        private Toalet toalet;

        public Women(Toalet toalet) {
            this.toalet = toalet;
        }
        public void enter() throws InterruptedException {
            men.acquire();
            if(countMen == 0){
                toiletSemaphore.acquire();
            }
            countMen++;
            toalet.vlezi();
            men.release();

        }
        public void exit() throws InterruptedException {
            men.acquire();
            countMen--;
            toalet.izlezi();
            if (countMen == 0){
                toiletSemaphore.release();
            }
            men.release();
        }

//        public void execute() throws InterruptedException {
//            women.acquire();
//            if(countWomen == 0){
//                lock.acquire();
//            }
//            women.release();
//            toalet.zena_vleguva();
//            women.acquire();
//            countWomen--;
//            women.release();
//            zena_izleguva();
//            if(countWomen == 0){
//                lock.release();
//            }
//        }
    }
}
