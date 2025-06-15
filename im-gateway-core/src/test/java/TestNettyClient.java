import com.elephant.RequestMessage;
import com.elephant.handler.MyProtocolDecoder;
import com.elephant.handler.MyProtocolEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TestNettyClient {

    private final String host;
    private final int port;
    private Channel channel;
    private EventLoopGroup group;

    public TestNettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                 .channel(NioSocketChannel.class)
                 .handler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         ch.pipeline()
                          .addLast(new MyProtocolDecoder())  // 解码服务器响应
                          .addLast(new MyProtocolEncoder())  // 编码发送给服务器的请求
                          .addLast(new SimpleChannelInboundHandler<Object>() {
                              @Override
                              protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                                  System.out.println("客户端收到响应: " + msg);
                              }

                              @Override
                              public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                  cause.printStackTrace();
                                  ctx.close();
                              }
                          });
                     }
                 });

        ChannelFuture future = bootstrap.connect(host, port).sync();
        channel = future.channel();
    }

    public void send(RequestMessage request) {
        channel.writeAndFlush(request);
    }

    public void stop() {
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new TestNettyClient("127.0.0.1", 5555).start();
    }
}
