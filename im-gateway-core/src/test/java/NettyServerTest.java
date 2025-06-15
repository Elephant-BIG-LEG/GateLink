import com.elephant.NettyServer;
import com.elephant.RequestMessage;
import com.elephant.message.RequestMessageHeader;
import org.junit.Test;

public class NettyServerTest {

    @Test
    public void testNettyServerAndClient() throws Exception {
        // 1. 先启动服务端（这里同步启动）
        new Thread(() -> {
            new NettyServer().start(5555);
        }).start();

        // 等待服务端启动完成
        Thread.sleep(3000);

        // 2. 启动客户端并发送请求
        TestNettyClient client = new TestNettyClient("localhost", 5555);
        client.start();

        // 构造请求消息，根据你自己的RequestMessage结构
        RequestMessage request = new RequestMessage();
        RequestMessageHeader requestMessageHeader = new RequestMessageHeader();
        requestMessageHeader.setSenderDid("123456");
        request.setRequestMessageHeader(requestMessageHeader);

        client.send(request);

        // 等待响应打印
        Thread.sleep(2000);

        client.stop();
    }
}
