import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FutureTest {

    @Test
    public void CompletableFutureTest() {
//        CompletableFuture<String> f = new CompletableFuture<>();
//        Thread t = new Thread(() -> {
//            try {
//                Thread.sleep(1000);
//                f.completeExceptionally(new RequestErrorException());
//                f.complete("finish");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        t.start();
        CompletableFuture<Integer> f = CompletableFuture.supplyAsync(
                () -> 1);
//        f.whenComplete((v, e) -> e.printStackTrace());
        CompletableFuture<Integer> u = f.thenApply(v -> v + 1);
        System.out.println("do other thing");
        try {
            System.out.println(u.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
