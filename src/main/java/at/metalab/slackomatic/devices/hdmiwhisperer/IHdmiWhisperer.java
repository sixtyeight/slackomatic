package at.metalab.slackomatic.devices.hdmiwhisperer;

import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.rest.IRestable;

public interface IHdmiWhisperer extends IRestable {
	IInvoker getInput1();

	IInvoker getInput2();

	IInvoker getInput3();

	IInvoker getInput4();

	IInvoker getInput5();

	IInvoker getInput6();

	IInvoker getInput7();

	IInvoker getInput8();

	IInvoker getInput9();
}
