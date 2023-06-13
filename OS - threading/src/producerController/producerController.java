package producerController;

import java.util.concurrent.Semaphore;

import static producerController.producerController.Buffer.count;

public class producerController {
    public static int NUM_RUNS = 10;
    public static Semaphore canCheck;
    public static Semaphore accessBuffer;
    public static Semaphore lock;

    public static void init(){
        canCheck = new Semaphore(10);
        accessBuffer = new Semaphore(1);
        lock = new Semaphore(1);
    }
    public static void main(String[] args) {

    }
    static class Buffer{
        public static int count = 0;

        int numberOfItems;

        public Buffer(int numberOfItems) {
            this.numberOfItems = numberOfItems;

        }
        public void produce(){
            System.out.println("Producing..");
            numberOfItems++;
        }
        public void check(){
            System.out.println("Checking..");
        }
    }
    static class Producer extends Thread{
        private Buffer buffer;

        public Producer(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            for (int i=0;i<NUM_RUNS;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        public void execute() throws InterruptedException {
            accessBuffer.acquire();
            buffer.produce();
            accessBuffer.release();

        }
    }
    static class Controller extends Thread{
        private Buffer buffer;
//        private static int count = 0;

        public Controller(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            for (int i=0;i<NUM_RUNS;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        public void execute() throws InterruptedException {
            lock.acquire();
            if (count == 0) {
                accessBuffer.acquire();
            }
            count++;
            lock.release();
            canCheck.acquire();
            buffer.check();
            lock.acquire();
            count--;
            canCheck.release();
            if (count==0) {
                accessBuffer.release();
            }
            lock.release();




        }
    }


}
