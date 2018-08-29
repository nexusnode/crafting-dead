package com.recastproductions.network.impl.example;

import com.recastproductions.network.NetworkClient;
import com.recastproductions.network.NetworkServer;
import com.recastproductions.network.impl.client.NetworkRegistryClient;
import com.recastproductions.network.impl.example.client.NetClientHandlerTest;
import com.recastproductions.network.impl.example.server.NetServerHandlerTest;
import com.recastproductions.network.impl.server.NetworkRegistryServer;

import java.net.InetSocketAddress;

public class Network {

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("client")) {
                testClient();
            } else if (args[0].equals("server")) {
                testServer();
            }
        }
    }

    public static void testClient() {
        NetworkRegistryClient client = new NetworkRegistryClient(new NetClientHandlerTest());

        NetworkClient netClient = new NetworkClient(client.getChannelInitializer());
        netClient.connect(new InetSocketAddress("127.0.0.1", 25545));
    }

    public static void testServer() {
        NetworkRegistryServer server = new NetworkRegistryServer();
        server.registerNetHandler(new NetServerHandlerTest());

        NetworkServer netServer = new NetworkServer(server.getChannelInitializer());
        netServer.bind(new InetSocketAddress("127.0.0.1", 25545));
    }

}
