package at.metalab.slackomatic.rest.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.metalab.slackomatic.rest.IEndpoint;

public class EndpointHandlerServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final IEndpoint endpoint;

	public EndpointHandlerServlet(IEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		endpoint.handle((HttpServletRequest) req, (HttpServletResponse) res);
	}

}
