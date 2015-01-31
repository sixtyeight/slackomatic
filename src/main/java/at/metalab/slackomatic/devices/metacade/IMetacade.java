package at.metalab.slackomatic.devices.metacade;

import at.metalab.slackomatic.api.IDevice;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.IRestable;

public interface IMetacade extends IDevice, IRestable {
	IToggle power();
}
