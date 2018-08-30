package com.kevinli.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

//Not Implemented Yet
//IDK How to do this
//Someone teach me :)

public class GameClient 
{
	//VARIABLE DECLARATION
	
	//Datagram socket object, handles the connections
	private DatagramSocket socket;
	
	//display name of user
	private String name;

//	Server IP
//	private final static String ADDRESS = "159.89.113.97";
	
	//local host | testing
	private final static String ADDRESS = "localhost";
	
	//Ip Address of server
	private static InetAddress IP;
	
	//port to connect to the server with
	private final int PORT = 40000;
	
	//username and password inputted on login screen
	String username,password;
	
	//thread to send messages
	private Thread send;
	
	//runtime id, is assigned when login is successful
	private int ID = -1;
	
	//END OF VARIABLE DECLARATION
	
	//Constructor
	public GameClient(String username, String password)
	{
		//assign username and password
		this.username = username;
		this.password = password;
		//initialize the socket and ip
		init();
		//verify the username and password with the server
		sendLoginInfo();
	}
	
	//send the username and password to get it verified
	public void sendLoginInfo()
	{
		send(("/c/"+username+"!"+password+"/e/").getBytes());
	}
	
	//initializes the server ip and the datagram socket
	private void init()
	{
		try {
			IP = InetAddress.getByName(ADDRESS);
			socket = new DatagramSocket();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	//receive a datagram packet from the server
	public DatagramPacket receive()
	{
		byte[] data = new byte[2048];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return packet;
	}
	
	//thread for sending data and information to the server
	public void send(final byte[] data) 
	{
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, IP, PORT);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	//is called when user closes program, sends disconnect packet to server
	public void disconnect()
	{
		send(("/d/"+ID+"/e/").getBytes());
	}
	
	//close the socket, send disconnect packet
	public void close() 
	{
		if(!(ID == -1))
		{
			disconnect();
			new Thread() {
				public void run() {
					synchronized (socket) {
						socket.close();
					}
				}
			}.start();
		}
	}
	
	//Getters and Setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public void setUsername(String name)
	{
		this.username = name;
	}
	
	public void setPassword(String pass)
	{
		this.password = pass;
	}
	
	//End of Getters and Setters
}
