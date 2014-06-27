package at.metalab.slackomatic.rest;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;

import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.rest.impl.EndpointHandlerServlet;
import at.metalab.slackomatic.rest.impl.InvokerEndpointImpl;

public class RestAPI {

	private final String context;

	private final List<String> endpoints = new LinkedList<String>();

	private final HandlerCollection handlerCollection = new HandlerCollection();

	public RestAPI(String context) {
		this.context = context;
	}

	private void add(String path, IRestable restable) {
		RestBuilder restBuilder = new RestBuilder(path);
		restable.create(restBuilder);

		ServletContextHandler servletContextHandler = restBuilder
				.getServletContextHandler();
		addMappings(endpoints, servletContextHandler.getServletHandler());
		handlerCollection.addHandler(servletContextHandler);
	}

	public <E extends IRestable, IDevice> void addDevice(String deviceName,
			E restable) {
		add(String.format("%s/devices/%s", context, deviceName), restable);
	}

	public <E extends IRestable, IRoom> void addRoom(String roomName,
			IRestable restable) {
		add(String.format("%s/rooms/%s", context, roomName), restable);
	}

	public void addFunction(String functionName, IInvoker invoker) {
		String path = String.format("%s/functions/%s", context, functionName);

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServlet(new ServletHolder(new EndpointHandlerServlet(
				new InvokerEndpointImpl(invoker, path))));

		addMappings(endpoints, servletHandler);
		handlerCollection.addHandler(servletHandler);
	}

	public void addFunction(String functionName, ContextHandler contextHandler) {
		String path = String.format("%s/functions/%s", context, functionName);

		contextHandler.setContextPath(path);

		addMappings(endpoints, contextHandler);
		handlerCollection.addHandler(contextHandler);
	}

	public String dump() {
		StringBuilder stringBuilder = new StringBuilder(
				"All available endpoints:\n");
		for (String endpoint : endpoints) {
			stringBuilder.append(endpoint + "\n");
		}
		return stringBuilder.toString();
	}

	public HandlerCollection getHandlerCollection() {
		return handlerCollection;
	}

	private static void addMappings(List<String> endpoints,
			ServletHandler servletHandler) {
		for (ServletMapping m : servletHandler.getServletMappings()) {
			// String path = Arrays.toString(m.getPathSpecs());
			String path = m.getPathSpecs()[0];
			endpoints.add(String.format("%s", path, m.getServletName()));
		}
	}

	private static void addMappings(List<String> endpoints,
			ContextHandler contextHandler) {
		endpoints.add(String.format("%s",
				contextHandler.getContextPath(), contextHandler));
	}

}
