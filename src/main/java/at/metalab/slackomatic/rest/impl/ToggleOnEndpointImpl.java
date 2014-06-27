package at.metalab.slackomatic.rest.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.metalab.slackomatic.api.IToggle;

public class ToggleOnEndpointImpl extends AbstractEndpointImpl {

	private IToggle toggle;

	public ToggleOnEndpointImpl(IToggle toggle, String path) {
		super(String.format("%s/on", path));
		this.toggle = toggle;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		toggle.on();
	}

}
