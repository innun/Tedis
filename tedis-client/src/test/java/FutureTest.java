import org.junit.jupiter.api.Test;

public class FutureTest {

    @Test
    public void CompletableFutureTest() throws InterruptedException {
        class MyTask implements Runnable{
            int cnt = 1;
            @Override
            public void run() {
                while(true) {
                    System.out.println(cnt);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        MyTask task = new MyTask();
        Thread thread = new Thread(task);
        thread.start();
        task.cnt = 2;
        thread.join();
    }
}
