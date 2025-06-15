package com.elephant.util;

import com.elephant.HelloYrpc;
import com.elephant.ReferenceConfig;
import com.elephant.YrpcBootstrap;
import com.elephant.discovery.RegistryConfig;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/06/15/17:47
 * @Description: 接入层拿到客户端的代理对象
 */
public class RpcClientFactory {
    public static HelloYrpc createHelloYrpcProxy() {
        ReferenceConfig<HelloYrpc> reference = new ReferenceConfig<>();
        reference.setInterface(HelloYrpc.class);

        YrpcBootstrap.getInstance()
                .application("first-yrpc-consumer")
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .serialize("hessian")
                .compress("gzip")
                .group("primary")
                .reference(reference);

        return reference.get();
    }
}