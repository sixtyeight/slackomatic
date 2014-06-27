package at.metalab.slackomatic.rest.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.metalab.slackomatic.api.IToggle;

public class ToggleOffEndpointImpl extends AbstractEndpointImpl {

	private IToggle toggle;

	public ToggleOffEndpointImpl(IToggle toggle, String path) {
		super(String.format("%s/off", path));
		this.toggle = toggle;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		toggle.off();
	}

}
