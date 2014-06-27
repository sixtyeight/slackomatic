package at.metalab.slackomatic.devices.benq;

import at.metalab.slackomatic.api.IDevice;
import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.IRestable;

public interface IBenq extends IDevice, IRestable {

	IToggle power();

	IDisplay display();

	public interface IDisplay {

		IToggle brilliant();

		IToggle blank();

		IInput input();

		public interface IInput {

			IInvoker vga1();

			IInvoker vga2();

			IInvoker hdmi();

			IInvoker video();
		}
	}
}
