package FinkiUpisi;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.concurrent.Semaphore;

public class FinkiUpisi {
    public static Semaphore slobodnoUpisnoMesto;
    public static Semaphore studentHere;
    public static Semaphore done;
    static Semaphore enter;

    public static void init() {
        slobodnoUpisnoMesto = new Semaphore(4);
        studentHere = new Semaphore(0);
        done = new Semaphore(0);
        enter = new Semaphore(0);
    }


    static class Komisija extends Thread {
        public void execute() throws InterruptedException {
            slobodnoUpisnoMesto.acquire();
            int i = 10;
            while (i > 0) {
                enter.release();
                studentHere.acquire();
                zapishi();
                done.release();
                i--;
            }
            slobodnoUpisnoMesto.release();
        }
        public void zapishi(){
            System.out.println("Zapishuvam student..");
        }

        @Override
        public void run() {
            super.run();
        }
    }

    static class Student {
        public void execute() throws InterruptedException {
            enter.acquire();
            ostaviDokumenti();
            studentHere.release();
            done.acquire();
        }

        public void ostaviDokumenti() {
            System.out.println("Ostavuvanje dokumenti.. ");
        }
    }
}
