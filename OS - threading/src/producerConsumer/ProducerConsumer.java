package producerConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ProducerConsumer {
    public static int NUM_RUNS = 2;
    public static Semaphore bufferEmpty;
    public static Semaphore bufferLock;
    public static Semaphore items[];

    public static void main(String[] args) {
        int numConsumers = 5;
        init(100);
        Buffer buffer = new Buffer(numConsumers);
        Producer producer = new Producer(buffer);
        List<Consumer> consumers = new ArrayList<>();
        for (int i=0;i<numConsumers;i++){
            consumers.add(new Consumer(buffer,i));
        }
        producer.start();
        for (Consumer c:consumers){
            c.start();
        }

    }

    public static void init(int numConsumers){
        bufferEmpty = new Semaphore(1);
        bufferLock = new Semaphore(1);
        items = new Semaphore[numConsumers];

        for(int i=0;i<numConsumers;i++){
            items[i] = new Semaphore(0);
        }

    }
    static class Buffer {
        private int numberItems;
        private int numberConsumers;

        public Buffer(int numberConsumers) {
            this.numberConsumers = numberConsumers;
        }

        public void getItem(int id) {
            System.out.println("Item with id " + id + " taken");
        }

        public void decrementNumberOfItemsLeft() {
            if (!isBufferEmpty()) {
                numberItems--;
            }
        }
        public void fillBuffer() {
            if (isBufferEmpty()) {
                numberItems = numberConsumers;
                System.out.println("Filling the buffer.....\n");
            } else {
                throw new RuntimeException();
            }
        }

        public boolean isBufferEmpty() {
            return numberItems == 0;
        }

    }

   static class Consumer extends Thread {
        private Buffer buffer;
        private int id;

        public Consumer(Buffer buffer,int id) {
            this.buffer = buffer;
            this.id = id;
        }

        @Override
        public void run() {
            for(int i=0;i<NUM_RUNS;i++){
                try {
                    execute(id);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void execute(int id) throws InterruptedException {
            items[id].acquire();
            buffer.getItem(id);

            bufferLock.acquire();
            buffer.decrementNumberOfItemsLeft();

            if(buffer.isBufferEmpty()){
                bufferEmpty.release();
            }
            bufferLock.release();



        }

    }

    static class Producer extends Thread {
        private Buffer buffer;

        public Producer(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            for(int i=0;i<NUM_RUNS;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void execute() throws InterruptedException {
            bufferEmpty.acquire();

            bufferLock.acquire();
            buffer.fillBuffer();
            bufferLock.release();

            for(Semaphore s:items){
                s.release();
            }
        }
    }
}
