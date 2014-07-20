package at.metalab.slackomatic;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;

import at.metalab.slackomatic.devices.benq.IBenq;
import at.metalab.slackomatic.devices.benq.ShellBenqImpl;
import at.metalab.slackomatic.devices.killswitch.IKillswitch;
import at.metalab.slackomatic.devices.killswitch.KillswitchImpl;
import at.metalab.slackomatic.devices.nec.INec;
import at.metalab.slackomatic.devices.nec.NecImpl;
import at.metalab.slackomatic.devices.yamaha.IYamaha;
import at.metalab.slackomatic.devices.yamaha.ShellYamahaImpl;
import at.metalab.slackomatic.rest.RestAPI;
import at.metalab.slackomatic.rooms.lounge.LoungeImpl;

public class Slackomatic {
	private final static Logger LOG = Logger.getLogger(Slackomatic.class
			.getCanonicalName());

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// default location of the slackomatic directory
		File slackomaticHome = new File(System.getProperty("user.home"),
				"slackomatic");

		// lookup custom installation directory if provided
		if (System.getProperties().containsKey("slackomatic.home")) {
			slackomaticHome = new File(System.getProperty("slackomatic.home"));
		}
		LOG.info(String.format("using %s as slackomatic home directory",
				slackomaticHome.getAbsolutePath()));

		// lookup port
		int slackomaticPort = Integer.valueOf(System.getProperty(
				"slackomatic.port", "8080"));
		LOG.info(String.format("listening on port %d", slackomaticPort));

		final Server server = new Server(slackomaticPort);
		server.setStopAtShutdown(true);
		server.setGracefulShutdown(5000);

		// setup a resource handler to deliver the awesome slick web ui with its
		// great ux :D
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });
		resourceHandler.setBaseResource(Resource.newClassPathResource("ui"));

		HandlerCollection slackomaticApiHandler = new HandlerCollection();

		{
			// setup the objects which control the actual hardware
			INec nec = new NecImpl('A', new File(slackomaticHome, "nec"));

			IBenq benq = new ShellBenqImpl(new File(slackomaticHome, "benq"));

			IYamaha yamaha = new ShellYamahaImpl(new File(slackomaticHome,
					"yamaha"));

			IKillswitch killswitch = new KillswitchImpl(new File(
					slackomaticHome, "killswitch"));

			// create the handler which will receive all incoming requests
			ServletContextHandler slackomaticHandler = new ServletContextHandler();
			slackomaticHandler.setContextPath("/slackomatic");

			slackomaticHandler.addFilter(new FilterHolder(LoggingFilter.class),
					"/*", EnumSet.of(DispatcherType.REQUEST));

			// setup the rest api for the devices
			final RestAPI restAPI = new RestAPI(
					slackomaticHandler.getContextPath());

			// register the devices
			restAPI.addDevice("nec", nec);
			restAPI.addDevice("benq", benq);
			restAPI.addDevice("yamaha", yamaha);

			// and add the lounge room
			restAPI.addRoom("lounge", new LoungeImpl(benq, yamaha, nec,
					killswitch));

			// functions related to the slackomatic itself start here

			// terminate the jvm
			restAPI.addFunction("shutdown", new ContextHandler() {
				@Override
				public void doHandle(String arg0, Request arg1,
						HttpServletRequest arg2, HttpServletResponse arg3)
						throws IOException, ServletException {
					arg3.setStatus(200);
					arg3.setContentType("text/plain");
					arg3.getWriter().write("ok");
					arg3.flushBuffer();

					System.exit(0);
				}
			});

			// return all registered endpoints in the slackomatic
			restAPI.addFunction("layout", new ContextHandler() {
				@Override
				public void doHandle(String arg0, Request arg1,
						HttpServletRequest arg2, HttpServletResponse arg3)
						throws IOException, ServletException {
					arg3.setStatus(200);
					arg3.setContentType("text/html");

					StringBuilder sb = new StringBuilder(
							"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body>");
					for (String endpoint : restAPI.getEndpoints()) {
						sb.append("<a target='_new' href='" + endpoint + "'>"
								+ endpoint + "</a><br>");
					}
					sb.append("<br>");
					sb.append("<code>");
					for (String endpoint : restAPI.getEndpoints()) {
						sb.append(endpoint);
						sb.append("<br>");
					}
					sb.append("</code>");
					sb.append("</body></html>");

					arg3.getWriter().write(sb.toString());
					arg3.flushBuffer();
				}
			});
			slackomaticApiHandler.addHandler(restAPI.getHandlerCollection());
		}

		{
			HandlerList handlers = new HandlerList();
			handlers.setHandlers(new Handler[] { resourceHandler,
					slackomaticApiHandler });
			server.setHandler(handlers);
		}

		server.start();
		LOG.info("server started");

		try {
			server.join();
		} finally {
			LOG.info("server shutdown");
		}
	}
}
