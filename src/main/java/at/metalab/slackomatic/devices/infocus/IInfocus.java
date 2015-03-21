package at.metalab.slackomatic.devices.infocus;

import at.metalab.slackomatic.api.IDevice;
import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.IRestable;

public interface IInfocus extends IDevice, IRestable {

	IToggle power();

	IDisplay display();

	public interface IDisplay {

		IInput input();

		public interface IInput {

			IInvoker vga();

			IInvoker hdmi();
		}
	}
}
