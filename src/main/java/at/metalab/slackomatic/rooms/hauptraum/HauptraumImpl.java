package at.metalab.slackomatic.rooms.hauptraum;

import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.devices.infocus.IInfocus;
import at.metalab.slackomatic.devices.nec.INec;
import at.metalab.slackomatic.rest.RestBuilder;

public class HauptraumImpl implements IHauptraum {

	private final IInfocus infocus;

	private final INec nec;

	public HauptraumImpl(INec nec, IInfocus infocus) {
		this.infocus = infocus;
		this.nec = nec;
	}

	public void create(RestBuilder restBuilder) {
		restBuilder.add(power(), "power");
		restBuilder.add(nec.power(), "powersaving/tv/power");
		restBuilder.add(infocus.power(), "powersaving/projector/power");
	}

	public IToggle power() {
		return new IToggle() {

			public void on() {
				nec.power().on();
				infocus.power().on();
			}

			public void off() {
				nec.power().on();
				infocus.power().off();
			}
		};
	}

	public IPowerSaving powerSaving() {
		return new IPowerSaving() {

			public IToggle powerTv() {
				return nec.power();
			}

			public IToggle powerProjector() {
				return infocus.power();
			}
		};
	}

}
