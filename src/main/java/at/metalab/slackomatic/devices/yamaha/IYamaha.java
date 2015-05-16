package at.metalab.slackomatic.devices.yamaha;

import at.metalab.slackomatic.api.IDevice;
import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.IRestable;

public interface IYamaha extends IDevice, IRestable {

	public interface IVolume {
		IInvoker low();

		IInvoker medium();

		IInvoker high();
		
		IInvoker increase();
		
		IInvoker decrease();
	}

	public interface IInput {
		IInvoker vAux();
		
		IInvoker av1();

		IInvoker av2();

		IInvoker av3();

		IInvoker av4();

		IInvoker av5();

		IInvoker av6();

		IInvoker hdmi1();

		IInvoker hdmi2();

		IInvoker hdmi3();

		IInvoker hdmi4();

		IInvoker hdmi5();

		IInvoker audio1();

		IInvoker audio2();

		IInvoker audio3();

		IInvoker audio4();

		IInvoker dock();
	}

	IVolume volume();
	
	IInput getInput();

	IToggle power();

	IToggle mute();
}
