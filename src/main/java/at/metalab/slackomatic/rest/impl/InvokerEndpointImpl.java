package at.metalab.slackomatic.rest.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.metalab.slackomatic.api.IInvoker;

public class InvokerEndpointImpl extends AbstractEndpointImpl {

	private final IInvoker invoker;

	public InvokerEndpointImpl(IInvoker invoker, String path) {
		super(path);
		this.invoker = invoker;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		invoker.invoke();
	}

}
