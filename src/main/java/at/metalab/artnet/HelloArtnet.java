package at.metalab.artnet;

import org.apache.commons.lang3.ArrayUtils;

import artnet4j.ArtNet;
import artnet4j.packets.ArtDmxPacket;

public class HelloArtnet {

	public static void main(String[] args) throws Exception {
		new HelloArtnet().go(args);
	}

	public void go(String[] args) throws Exception {
		ArtNet artnet = new ArtNet();

		artnet.start();
		
		int leds = 170;
		
		byte[] white = new byte[] {};
		for(int i = 0; i < leds; i++) {
			white = ArrayUtils.add(white, (byte) 255);
			white = ArrayUtils.add(white, (byte) 255);
			white = ArrayUtils.add(white, (byte) 255);
		}

		byte[] medium = new byte[] {};
		for(int i = 0; i < leds; i++) {
			medium = ArrayUtils.add(medium, (byte) 125);
			medium = ArrayUtils.add(medium, (byte) 125);
			medium = ArrayUtils.add(medium, (byte) 125);
		}

		byte[] black = new byte[] {};
		for(int i = 0; i < leds; i++) {
			black = ArrayUtils.add(black, (byte) 0);
			black = ArrayUtils.add(black, (byte) 0);
			black = ArrayUtils.add(black, (byte) 0);
		}

		int universe = 0; // 0 -> tunnel, 2 -> lounge
		String ip = "192.168.88.255";
		
		ArtDmxPacket packetBlack= new ArtDmxPacket();
		packetBlack.setUniverse(0, universe);
		packetBlack.setSequenceID(0);
		packetBlack.setDMX(black, leds * 3);
		
		ArtDmxPacket packetWhite = new ArtDmxPacket();
		packetWhite.setUniverse(0, universe);
		packetWhite.setSequenceID(0);
		packetWhite.setDMX(white, leds * 3);

		ArtDmxPacket packetMedium = new ArtDmxPacket();
		packetMedium.setUniverse(0, universe);
		packetMedium.setSequenceID(0);
		packetMedium.setDMX(medium, leds * 3);
		
		for (;;) {
			Thread.sleep(1000);
			artnet.unicastPacket(packetBlack, ip);
			System.out.println("black");
			
			Thread.sleep(1000);
			artnet.unicastPacket(packetWhite, ip);
			System.out.println("white");
		}
	}

}
