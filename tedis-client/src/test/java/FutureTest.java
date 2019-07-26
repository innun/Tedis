import com.tedis.client.TedisClient;
import com.tedis.client.TedisConfig;
import org.junit.jupiter.api.Test;

public class FutureTest {

    @Test
    public void CompletableFutureTest() throws InterruptedException {
        TedisClient client = TedisClient.create(TedisConfig.DEFAULT_CONFIG);
        TedisClient client1 = TedisClient.create(TedisConfig.DEFAULT_CONFIG);
        client.connect();
        client1.connect();
        System.out.println(client.getChannel().localAddress());
        System.out.println(client1.getChannel().localAddress());
    }
}
