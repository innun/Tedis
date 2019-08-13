import com.tedis.TedisClient;
import com.tedis.api.Tedis;

public class Application {
    public static void main(String[] args) {
        Tedis t = TedisClient.tedis();
        t.ping().sync();
        t.close();
    }
}
