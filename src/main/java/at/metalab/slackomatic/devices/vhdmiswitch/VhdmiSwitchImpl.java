package at.metalab.slackomatic.devices.vhdmiswitch;

import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.devices.hdmiwhisperer.IHdmiWhisperer;
import at.metalab.slackomatic.devices.yamaha.IYamaha;
import at.metalab.slackomatic.rest.RestBuilder;

public class VhdmiSwitchImpl implements IVhdmiSwitch {

	private final IHdmiWhisperer hdmiWhisperer;

	private final IYamaha yamaha;

	public VhdmiSwitchImpl(IHdmiWhisperer hdmiWhisperer, IYamaha yamaha) {
		this.hdmiWhisperer = hdmiWhisperer;
		this.yamaha = yamaha;
	}

	private IInvoker withYamahaSwitchToHdmi1(final IInvoker invoker) {
		return new IInvoker() {

			public void invoke() {
				invoker.invoke();
				yamaha.getInput().hdmi1().invoke();
			}
		};
	}

	public IInvoker input1() {
		return yamaha.getInput().hdmi1();
	}

	public IInvoker input2() {
		return yamaha.getInput().hdmi2();
	}

	public IInvoker input3() {
		return yamaha.getInput().hdmi3();
	}

	public IInvoker input4() {
		return yamaha.getInput().hdmi4();
	}

	public IInvoker input5() {
		return yamaha.getInput().hdmi5();
	}

	public IInvoker input6() {
		return withYamahaSwitchToHdmi1(hdmiWhisperer.getInput1());
	}

	public IInvoker input7() {
		return withYamahaSwitchToHdmi1(hdmiWhisperer.getInput2());
	}

	public IInvoker input8() {
		return withYamahaSwitchToHdmi1(hdmiWhisperer.getInput3());
	}

	public IInvoker input9() {
		return withYamahaSwitchToHdmi1(hdmiWhisperer.getInput4());
	}

	public IInvoker input10() {
		return withYamahaSwitchToHdmi1(hdmiWhisperer.getInput5());
	}

	public IInvoker input11() {
		return withYamahaSwitchToHdmi1(hdmiWhisperer.getInput6());
	}

	public IInvoker input12() {
		return withYamahaSwitchToHdmi1(hdmiWhisperer.getInput7());
	}

	public IInvoker input13() {
		return withYamahaSwitchToHdmi1(hdmiWhisperer.getInput8());
	}

	public IInvoker input14() {
		return withYamahaSwitchToHdmi1(hdmiWhisperer.getInput9());
	}

	public void create(RestBuilder rest) {
		rest.add(input1(), "input1");
		rest.add(input2(), "input2");
		rest.add(input3(), "input3");
		rest.add(input4(), "input4");
		rest.add(input5(), "input5");
		rest.add(input6(), "input6");
		rest.add(input7(), "input7");
		rest.add(input8(), "input8");
		rest.add(input9(), "input9");
		rest.add(input10(), "input10");
		rest.add(input11(), "input11");
		rest.add(input12(), "input12");
		rest.add(input13(), "input13");
		rest.add(input14(), "input14");
	}

}
