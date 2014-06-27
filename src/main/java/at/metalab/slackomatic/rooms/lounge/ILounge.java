package at.metalab.slackomatic.rooms.lounge;

import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IRoom;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.IRestable;

public interface ILounge extends IRoom, IRestable {
	public interface IDevices {
		IInvoker screeninvader();

		IInvoker screeninvaderStable();

		IInvoker ps2();

		IInvoker ps3();

		IInvoker wii();

		IInvoker ownHDMI();

		IInvoker ownAudioKlinke();

		IInvoker bt();
	}

	public interface IPowerSaving {
		IToggle blankProjector();

		IToggle powerProjector();

		IToggle powerTv();

		IToggle powerYamaha();

		IInvoker resetKillswitch();
	}

	IToggle power();

	IDevices devices();

	IPowerSaving powerSaving();
}
