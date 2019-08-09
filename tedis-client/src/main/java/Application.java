import com.tedis.TedisClient;
import com.tedis.api.Tedis;

public class Application {
    public static void main(String[] args) {
        Tedis tedis = TedisClient.tedis();
        tedis.subscribe("news.it");
        tedis.set("a", "hello world");
        tedis.get("a");
    }
}
