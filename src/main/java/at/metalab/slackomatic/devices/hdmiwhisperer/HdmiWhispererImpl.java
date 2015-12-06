package at.metalab.slackomatic.devices.hdmiwhisperer;

import java.io.File;

import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.devices.hdmiwhisperer.Sindax.Channel;
import at.metalab.slackomatic.devices.hdmiwhisperer.Sindax.Input;
import at.metalab.slackomatic.rest.RestBuilder;

public class HdmiWhispererImpl implements IHdmiWhisperer {

	private final Sindax sindax1;

	private final Sindax sindax2;

	public HdmiWhispererImpl(File hdmiWhispererDir, String pcmDeviceName) {
		sindax1 = new Sindax(hdmiWhispererDir, pcmDeviceName, Channel.LEFT);

		sindax2 = new Sindax(hdmiWhispererDir, pcmDeviceName, Channel.RIGHT);
	}

	private void sindax1Passthrough() {
		sindax1.selectInput(Input.INPUT5);
	}

	public IInvoker getInput1() {
		return new IInvoker() {

			public void invoke() {
				sindax1.selectInput(Input.INPUT1);
			}
		};
	}

	public IInvoker getInput2() {
		return new IInvoker() {

			public void invoke() {
				sindax1.selectInput(Input.INPUT2);
			}
		};
	}

	public IInvoker getInput3() {
		return new IInvoker() {

			public void invoke() {
				sindax1.selectInput(Input.INPUT3);
			}
		};
	}

	public IInvoker getInput4() {
		return new IInvoker() {

			public void invoke() {
				sindax1.selectInput(Input.INPUT4);
			}
		};
	}

	public IInvoker getInput5() {
		return new IInvoker() {

			public void invoke() {
				sindax1Passthrough();
				sindax2.selectInput(Input.INPUT1);
			}
		};
	}

	public IInvoker getInput6() {
		return new IInvoker() {

			public void invoke() {
				sindax1Passthrough();
				sindax2.selectInput(Input.INPUT2);
			}
		};
	}

	public IInvoker getInput7() {
		return new IInvoker() {

			public void invoke() {
				sindax1Passthrough();
				sindax2.selectInput(Input.INPUT3);
			}
		};
	}

	public IInvoker getInput8() {
		return new IInvoker() {

			public void invoke() {
				sindax1Passthrough();
				sindax2.selectInput(Input.INPUT4);
			}
		};
	}

	public IInvoker getInput9() {
		return new IInvoker() {

			public void invoke() {
				sindax1Passthrough();
				sindax2.selectInput(Input.INPUT5);
			}
		};
	}

	public void create(RestBuilder rest) {
		rest.add(getInput1(), "input1");
		rest.add(getInput2(), "input2");
		rest.add(getInput3(), "input3");
		rest.add(getInput4(), "input4");
		rest.add(getInput5(), "input5");
		rest.add(getInput6(), "input6");
		rest.add(getInput7(), "input7");
		rest.add(getInput8(), "input8");
		rest.add(getInput9(), "input9");
	}
}
