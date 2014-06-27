package at.metalab.slackomatic.rest.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.metalab.slackomatic.rest.IEndpoint;

public abstract class AbstractEndpointImpl implements IEndpoint {

	private final String path;

	public AbstractEndpointImpl(String path) {
		this.path = path;
	}

	public abstract void handle(HttpServletRequest request,
			HttpServletResponse response);

	public String getPath() {
		return path;
	}

}
