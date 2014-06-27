package at.metalab.slackomatic;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LoggingFilter implements Filter {

	private volatile int reqNum = 0;

	private final static Logger LOG = Logger.getLogger(LoggingFilter.class
			.getCanonicalName());

	public void destroy() {
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;

		String desc = null;

		synchronized (this) {
			reqNum++;
			desc = String.format("#%d, %s -> %s", reqNum, arg0.getRemoteAddr(),
					request.getRequestURL());
		}

		long ts = System.currentTimeMillis();

		try {
			LOG.info(String.format("incoming request: %s", desc));
			arg2.doFilter(arg0, arg1);
		} finally {
			LOG.info(String.format("processed request: %s (took %d milliseconds)", desc,
					System.currentTimeMillis() - ts));
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
