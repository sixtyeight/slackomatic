package at.metalab.slackomatic.devices.loungelights;

import at.metalab.slackomatic.api.IDevice;
import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.rest.IRestable;

public interface ILoungeLights extends IDevice, IRestable {

	interface IPower {
		IInvoker off();

		IInvoker whiteOff();

		IInvoker amberOff();

		IInvoker rgbOff();
	}
	
	void setWhite(String value);
	
	void setAmber(String value);
	
	void setRGB(String r, String g, String b);

	IPower power();

}
