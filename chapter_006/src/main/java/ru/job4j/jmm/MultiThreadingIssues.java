package ru.job4j.jmm;

/**
 * MultiThreadingIssues
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class MultiThreadingIssues {

    private int num = 0; // volatile

    public class Increment implements Runnable {

        @Override
        public void run() {
            add();
        }
    }

    private void add() {  // synchronized
        num++;
    }

    public void showVisibilityOfSharedObjects() {
        for (int j = 0; j < 50000; j++) {
            this.num = 0;
            execute(new Increment(), new Increment());
            if (num < 2) {
                break;
            }
        }
        print("showVisibilityOfSharedObjects");
    }

    public void showRaceCondition() {
        for (int j = 0; j < 50000; j++) {
            execute(new Increment(), new Increment());
        }
        print("showRaceCondition");
    }

    private void execute(Runnable first, Runnable second) {
        Thread firstThread = new Thread(first);
        Thread secondThread = new Thread(second);
        firstThread.start();
        secondThread.start();
        try {
            firstThread.join();
            secondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void print(String name) {
        if (name != null) {
            System.out.println(name + ": " + this.num);
        }
    }

    public static void main(String[] args) {
        MultiThreadingIssues mti = new MultiThreadingIssues();
        mti.showVisibilityOfSharedObjects();
        mti.showRaceCondition();
    }
}
