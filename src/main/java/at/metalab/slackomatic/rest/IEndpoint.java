package at.metalab.slackomatic.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IEndpoint {
	void handle(HttpServletRequest request, HttpServletResponse response);

	String getPath();
}
