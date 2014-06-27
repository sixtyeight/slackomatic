package at.metalab.slackomatic.rest;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.impl.EndpointHandlerServlet;
import at.metalab.slackomatic.rest.impl.InvokerEndpointImpl;
import at.metalab.slackomatic.rest.impl.ToggleOffEndpointImpl;
import at.metalab.slackomatic.rest.impl.ToggleOnEndpointImpl;

public class RestBuilder {

	private ServletContextHandler servletContextHandler = new ServletContextHandler();

	private final String basePath;

	public RestBuilder(String basePath) {
		this.basePath = basePath;
	}

	public void add(IToggle toggle, String path) {
		add(new ToggleOnEndpointImpl(toggle, path));
		add(new ToggleOffEndpointImpl(toggle, path));
	}
	
	public void add(IInvoker invoker, String path) {
		add(new InvokerEndpointImpl(invoker, path));
	}

	public void add(IEndpoint endpoint) {
		servletContextHandler.addServlet(new ServletHolder(
				new EndpointHandlerServlet(endpoint)), String.format("%s/%s",
				basePath, endpoint.getPath()));
	}
	
	public ServletContextHandler getServletContextHandler() {
		return servletContextHandler;
	}
}
