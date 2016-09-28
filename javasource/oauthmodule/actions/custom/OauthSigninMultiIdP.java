package oauthmodule.actions.custom;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import oauthmodule.proxies.constants.Constants;

import org.apache.commons.lang3.StringUtils;

import com.mendix.core.Core;
import com.mendix.externalinterface.connector.RequestHandler;
import com.mendix.m2ee.api.IMxRuntimeRequest;
import com.mendix.m2ee.api.IMxRuntimeResponse;
/**
 * This class will handle the request as a requesthandler by redirecting the user to the selected
 * Oauth provider. The provider is determined from the URI path parameters
 *
 *@author Erwin 't Hoen
 *@version 1.0
 *@since 2014-10-02
 */
public class OauthSigninMultiIdP extends RequestHandler {

	/*
	 * Contextpath is not used in this function but is needed for adding the requesthandler.
	 */
	@SuppressWarnings("unused")
	private String contextPath;
	public OauthSigninMultiIdP(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * This method will start processing the incoming request on the handler
	 */
	@Override
	protected void processRequest(IMxRuntimeRequest request,IMxRuntimeResponse response, String path) throws Exception {

		HttpServletRequest servletRequest = request.getHttpServletRequest();
		Core.getLogger("OAuthSignin").trace("Received process request event");
		try {
			Core.getLogger("OAuthSignin").trace("Request URI: "+ servletRequest.getRequestURI());
			OauthLogin(request, response,servletRequest.getRequestURI());
		} catch (Exception ex) {
			Core.getLogger("OAuthSignin").error("Exception occurred while processing request "+ex);
			response.sendError("Exception occurred while processing request");
		}
	}

	/**
	 * This method will process the request to the signin handler
	 * Add a cookie with a generated state parameter
	 * Redirect to the Oauth provider based on the URI path parameters
	 * @param request
	 * @param response
	 * @param requestURI
	 * @throws ServletException
	 * @throws IOException
	 */
	private void OauthLogin(IMxRuntimeRequest request, IMxRuntimeResponse response, String requestURI) throws ServletException, IOException {
		String UUIDstate = createUUID();
		String cookieName = Constants.getCookieName(); 
		Core.getLogger("OAuthSignin").trace("Name of the cookie is: "+cookieName);
		Cookie userCookie = new Cookie(cookieName, UUIDstate);
		userCookie.setMaxAge(60*2); //Store cookie for 2 minutes
		userCookie.setPath("/");
		HttpServletResponse servletResponse =  response.getHttpServletResponse();
		servletResponse.addCookie(userCookie);

		String[] pathParameters = StringUtils.split(requestURI, '/');
		for(int i =0; i < pathParameters.length; i++){
			Core.getLogger("OAuthSignin").trace(pathParameters[i]+" "+i);
		}
		if (pathParameters.length > 0) {
			if(pathParameters[1].equals("google")){
				new GetAccessCodeGoogle().getCode(UUIDstate, servletResponse);
			}
			else if(pathParameters[1].equals("linkedin")){
				new GetAccessCodeLinkedin().getCode(UUIDstate, servletResponse);
			}
			else if(pathParameters[1].equals("facebook")){
				new GetAccessCodeFacebook().getCode(UUIDstate, servletResponse);
			}
			else{
				Core.getLogger("OAuthSignin").error("Unkown request path parameter");
				throw new ServletException("Unkown request path parameter");
			}
		}
		else{
			Core.getLogger("OAuthSignin").error("Missing request path parameter");
			throw new ServletException("Missing request path parameter");
		}



	}

	/**
	 * This method will generate a java UUID
	 * @return
	 */
	public String createUUID(){
		return UUID.randomUUID().toString();
	}

}
