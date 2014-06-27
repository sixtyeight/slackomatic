package at.metalab.slackomatic.test;

import at.metalab.slackomatic.devices.nec.INec;
import at.metalab.slackomatic.devices.nec.NecImpl;

public class TestNec {

	public static void main(String[] args) {
		final String device = "/dev/ttyUSB0";

		INec monitor1 = new NecImpl('A', device);

		monitor1.power().on();
		monitor1.display().input().hdmi().invoke();
		monitor1.pip().input().vga().invoke();
		monitor1.pip().toggle().on();
	}

}
