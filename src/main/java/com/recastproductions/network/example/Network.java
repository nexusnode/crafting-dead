package com.recastproductions.network.example;

import java.net.InetSocketAddress;

import com.recastproductions.network.client.NetworkClient;
import com.recastproductions.network.client.NetworkRegistryClient;
import com.recastproductions.network.example.client.NetClientHandlerTest;
import com.recastproductions.network.example.server.NetServerHandlerTest;
import com.recastproductions.network.server.NetworkRegistryServer;
import com.recastproductions.network.server.NetworkServer;

public class Network {
	
	public static void main(String[] args) {
		if(args.length > 0) {
			if(args[0].equals("client")) {
				testClient();
			} else if(args[0].equals("server")) {
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
