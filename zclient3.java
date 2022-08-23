package com.myfirstjava.program;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class zclient3 {
	private static byte[] toByteArray (final String i) throws IOException {
		return i.getBytes();
		}
	public static void main(String[] args) throws Exception {
		final CountDownLatch connectedSignal = new CountDownLatch(1);
		ZooKeeper zoo = new ZooKeeper("localhost:2181",5000,null);
		String path = "/clients/client3";
		Scanner sc = new Scanner(System.in);
		System.out.println("write something here: ");
		String txt ;
		txt = sc.nextLine();
		byte[] ar = toByteArray(txt);
		zoo.create(path, ar, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		Watcher w = new Watcher() {
			public void process(WatchedEvent event) {
				
				try {
					for (String s: zoo.getChildren("/clients",this, null)) {
						s = "/clients" + "/"+ s;
						if (zoo.exists(s,false) != null) {
							System.out.println("this client there" + " " +  s);
						}else {
							System.out.println("showing the disconnected clients"+ " "  +  s);
						}
					}
					for (String s: zoo.getChildren("/clients",this, null)) {
						System.out.println(s);
						s = "/clients" + "/"+ s;
						byte[] t = zoo.getData(s,this, null);
						String dat = new String(t, "UTF-8");
						System.out.println(dat);
					}
						for(String c: zoo.getChildren("/", this,null)) {
							c = "/" + "/" + c;
							if(zoo.exists("/",false) == null) {
								System.out.println("root node does not have any nodes" + c);
							}else {
								System.out.println("root node is having nodes" + c);
							}
						}
						zoo.getChildren("/clients",this, null);
				}catch(Exception e) {	
			}
		}
	};
		
	zoo.getChildren("/clients",w, null);
	zoo.getChildren("/", w, null);	
	byte[] bn = zoo.getData("/clients/client3", w, null);
		String data = new String(bn, "UTF-8");
		System.out.println(data);
		int v = zoo.getSessionTimeout();
		for (String s: zoo.getChildren("/clients",w, null)) {
			if (v == 1000) {
				System.out.println("all clients" + " "+  s);
					}
		}
		connectedSignal.await();
	}
}
