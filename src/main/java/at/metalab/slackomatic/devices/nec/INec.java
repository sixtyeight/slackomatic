package at.metalab.slackomatic.devices.nec;

import at.metalab.slackomatic.api.IDevice;
import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.IRestable;

public interface INec extends IDevice, IRestable {

	public interface IInput {
		IInvoker hdmi();

		IInvoker vga();

		IInvoker dvi();

		IInvoker video1();

		IInvoker video2();
	}

	public interface IDisplay {
		IInput input();
	}

	public interface IPip {
		IToggle toggle();

		IInput input();

		IMode mode();

		ISize size();

		public interface IMode {
			IInvoker pip();

			IInvoker pop();

			IInvoker sideBySideAspect();

			IInvoker sideBySideFull();

			IInvoker popAspectMain();

			IInvoker popAspectSub();
		}

		public interface ISize {
			IInvoker small();

			IInvoker medium();

			IInvoker large();
		}
	}

	IToggle power();

	IDisplay display();

	IPip pip();

	IToggle mute();

	IToggle signalInformation();

	void borderColor(int brightness);
}
