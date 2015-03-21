package at.metalab.slackomatic.rooms.hauptraum;

import at.metalab.slackomatic.api.IRoom;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.IRestable;

public interface IHauptraum extends IRoom, IRestable {

	public interface IPowerSaving {
		IToggle powerProjector();

		IToggle powerTv();
	}

	IToggle power();

	IPowerSaving powerSaving();
}
