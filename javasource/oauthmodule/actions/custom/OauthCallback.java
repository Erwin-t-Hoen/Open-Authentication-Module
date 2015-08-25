package oauthmodule.actions.custom;


import oauthmodule.proxies.OAuthConfig;
import oauthmodule.proxies.OAuthMessage;
import oauthmodule.proxies.Parameter;
import oauthmodule.proxies.constants.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mxmodelreflection.proxies.Microflows;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import system.proxies.User;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.core.CoreRuntimeException;
import com.mendix.externalinterface.connector.RequestHandler;
import com.mendix.m2ee.api.IMxRuntimeRequest;
import com.mendix.m2ee.api.IMxRuntimeResponse;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IDataType;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.IUser;

import communitycommons.XPath;
/**
 * Class handles the callback from the Oauth provider as the requesthandler
 * 
 * @Author: Erwin 't Hoen
 * @version: 1.0
 * @since: 2014-10-02
 */
public class OauthCallback extends RequestHandler{
	@SuppressWarnings("unused")
	private String contextPath;
	private final String USERINFOURI_GOOGLE =  Constants.getUserInfoURI_Google();
	private final String USERINFOURI_LINKEDIN = Constants.getUserInfoURI_Linkedin();
	private final String USERINFOURI_FACEBOOK = Constants.getUserInfoURI_Facebook();
	private final String UNAUTHHTML = Constants.getUnauthorizedAccessPage();
	private final String COOKIEHTML = Constants.getCookieErrorPage();
	private final String STATEHTML = Constants.getStateErrorPage();
	private final String OAUTHDIR = "Oauth";

	public OauthCallback(String contextPath) {
		this.contextPath = contextPath;
	}
	/**
	 * This method will start processing the incoming callback request from the Oauth provider
	 *
	 */
	@Override
	protected void processRequest(IMxRuntimeRequest request,IMxRuntimeResponse response, String path) throws Exception {
		HttpServletRequest servletRequest =  request.getHttpServletRequest();
		Core.getLogger("OauthCallback").trace("Received process request event");
		try {
			Core.getLogger("OauthCallback").debug("Request URI: "+ servletRequest.getRequestURI());
			oauthCallbackService(request, response);
		} catch (Exception ex) {
			Core.getLogger("OauthCallback").error("Exception occurred while processing request "+ex);
			response.sendError("Exception occurred while processing request");
		}
	}
	/**
	 * This method will process the incoming request
	 * First the state of the request is checked against the value in the cookie
	 * The the code is exchanged for an Access Token
	 * The Access Token is used to retrieve information on behalf of the user with the Oauth provider
	 */
	private void oauthCallbackService(IMxRuntimeRequest request, IMxRuntimeResponse response) throws ServletException, IOException, CoreException {
		HttpServletResponse servletResponse =  response.getHttpServletResponse();
		HttpServletRequest servletRequest =  request.getHttpServletRequest();
		if (request.getParameter("error") != null) {
			throw new ServletException("Error found");
		}
		/*
		 * Check cookie for UUID compared to state value
		 */
		String cookieName = Constants.getCookieName();

		Cookie[] cookies = servletRequest.getCookies();
		boolean cookieStateVerified = false;
		if (cookies != null) 
		{
			for(int i=0; i<cookies.length; i++)
			{
				Cookie cookie = cookies[i];
				Core.getLogger("OauthCallback").debug("found cookie with name: "+cookie.getName());
				if (cookieName.equals(cookie.getName())) 
				{
					Core.getLogger("OauthCallback").debug("cookie value: "+cookie.getValue());
					Core.getLogger("OauthCallback").debug(servletRequest.getRequestURI());
					String state = servletRequest.getParameter("state");
					Core.getLogger("OauthCallback").debug("State value: "+state);
					if( cookie.getValue().equals(state)){
						Core.getLogger("OauthCallback").debug("state matches cookie");
						cookieStateVerified = true;
					}
					else{
						/*
						 * state in the cookie is different from what is returned by the provider
						 * create log record
						 * show csf/xss error page 
						 */
						new LogRecordHandler().createLogRecord("Possible CSF/XSS attempt",OAuthMessage.XSSAttempt);
						Core.getLogger("OauthCallback").error("False state detected");
						new ErrorHandler().processErrorHandler(servletResponse, OAUTHDIR, STATEHTML);
						return;
					}
				}
			}
		}
		else
		{
			/*
			 * No cookies found whatsoever
			 * create log record and show cookie error page
			 */
			new LogRecordHandler().createLogRecord("State could not be verified",OAuthMessage.StateError);
			Core.getLogger("OauthCallback").error("Cookie not found");
			new ErrorHandler().processErrorHandler(servletResponse, OAUTHDIR, COOKIEHTML);
			return;
		} 
		if (!cookieStateVerified){
			/*
			 * Cookies found but none contain the correct state
			 * create log record and show cookie error page
			 */
			new LogRecordHandler().createLogRecord("State could not be verified",OAuthMessage.StateError);
			Core.getLogger("OauthCallback").error("Cookie not found");
			new ErrorHandler().processErrorHandler(servletResponse, OAUTHDIR, COOKIEHTML);
			return;
		}
		/*
		 *  Oauth provide returns a code that can be exchanged for a access token
		 *  get the access token by post to Oauth provider
		 *  example returned access token
		 *	 {
		 *	    "access_token": "ya29.AHES6ZQS-BsKiPxdU_iKChTsaGCYZGcuqhm_A5bef8ksNoU",
		 *	    "token_type": "Bearer",
		 *	    "expires_in": 3600,
		 *	    "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjA5ZmE5NmFjZWNkOGQyZWRjZmFiMjk0NDRhOTgyN2UwZmFiODlhYTYifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiZW1haWxfdmVyaWZpZWQiOiJ0cnVlIiwiZW1haWwiOiJhbmRyZXcucmFwcEBnbWFpbC5jb20iLCJhdWQiOiI1MDgxNzA4MjE1MDIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdF9oYXNoIjoieUpVTFp3UjVDX2ZmWmozWkNublJvZyIsInN1YiI6IjExODM4NTYyMDEzNDczMjQzMTYzOSIsImF6cCI6IjUwODE3MDgyMTUwMi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImlhdCI6MTM4Mjc0MjAzNSwiZXhwIjoxMzgyNzQ1OTM1fQ.Va3kePMh1FlhT1QBdLGgjuaiI3pM9xv9zWGMA9cbbzdr6Tkdy9E-8kHqrFg7cRiQkKt4OKp3M9H60Acw_H15sV6MiOah4vhJcxt0l4-08-A84inI4rsnFn5hp8b-dJKVyxw1Dj1tocgwnYI03czUV3cVqt9wptG34vTEcV3dsU8",
		 *	    "refresh_token": "1/Hc1oTSLuw7NMc3qSQMTNqN6MlmgVafc78IZaGhwYS-o"
		 *	 }
		 */
		String requestPath = servletRequest.getRequestURI();
		String[] pathParameters = StringUtils.split(requestPath, '/');
		String body=  "";
		String code = request.getParameter("code");
		if (pathParameters.length > 0) {
			if(pathParameters[1].equals("google")){
				GetAccessTokenGoogle accessTokenGoogle = new GetAccessTokenGoogle(code);
				body = accessTokenGoogle.getResult();
			}
			else if(pathParameters[1].equals("linkedin")){
				GetAccessTokenLinkedin accessTokenLinkedin = new GetAccessTokenLinkedin(code);
				body = accessTokenLinkedin.getResult();
			}
			else if(pathParameters[1].equals("facebook")){
				GetAccessTokenFacebook accessTokenFacebook = new GetAccessTokenFacebook(code);
				body = accessTokenFacebook.getResult();
			}
			else{
				Core.getLogger("OauthCallback").error("Unkown request path parameter");
				throw new ServletException("Unkown request path parameter");
			}
		}
		else{
			Core.getLogger("OauthCallback").error("Missing request path parameter");
			throw new ServletException("Missing request path parameter");
		}
		JSONObject jsonObject = null;
		/*
		 * Check if body is returned as json if true then
		 * get the access token from json and request info from the OAuth provider
		 * else get access token specifically from the returned body
		 * 
		 * Be aware that tokens will expire!
		 */
		boolean isJson = true;
		try {
			jsonObject = (JSONObject) new JSONParser().parse(body);
		} catch (ParseException e) {
			isJson = false;
			//throw new RuntimeException("Unable to parse json " + body);
		}

		String accessToken = "";
		if(isJson){
			/*
			 * This works specifically for facebook as they return the bodys as:
			 * accesstoken=<token>&expires=<time>
			 */
			
			accessToken = (String) jsonObject.get("access_token");
		}
		else{
			
			accessToken = body.substring(13,body.length()-16);
			Core.getLogger("OAuthCallback").debug("accestoken is: "+accessToken);
		}
		/*
		 *  get some info about the user with the access token
		 */
		String userInfoString = "";
		if (pathParameters.length > 0) {
			GetHttpRequest geturi = new GetHttpRequest();
			if(pathParameters[1].equals("google")){
				Core.getLogger("OauthCallback").trace("Get user info from Google");
				userInfoString = geturi.get(new StringBuilder(USERINFOURI_GOOGLE).append(accessToken).toString());
			}
			else if(pathParameters[1].equals("linkedin")){
				Core.getLogger("OauthCallback").trace("Get userinfo from Linkedin");
				userInfoString = geturi.get(new StringBuilder(USERINFOURI_LINKEDIN).append(accessToken+"&format=json").toString());
			}
			else if(pathParameters[1].equals("facebook")){
				Core.getLogger("OauthCallback").trace("Get userinfo from Facebook");
				userInfoString = geturi.get(new StringBuilder(USERINFOURI_FACEBOOK).append(accessToken).toString());
			}
			else{
				Core.getLogger("OauthCallback").error("Unkown request path parameter");
				throw new ServletException("Unkown request path parameter");
			}
		}
		else{
			Core.getLogger("OauthCallback").error("Missing request path parameter");
			throw new ServletException("Missing request path parameter");
		}
		/*
		 *  return the string with the user's basic info and get the email address from the data
		 *  to retrieve the user.
		 */
		Core.getLogger("OauthCallback").debug("Request URI: "+ userInfoString);

		JSONObject jsonObj;
		try {
			jsonObj = (JSONObject)new JSONParser().parse(userInfoString);
			IContext context = Core.createSystemContext();
			User user = null;
			
			OAuthConfig configuration = getConfig(context);
			
			if(pathParameters[1].equals("google")){
			 user =resolveUser(context, jsonObj, configuration);
			}
			else if(pathParameters[1].equals("linkedin")){
				 user =resolveUser(context, jsonObj, configuration);
			}
			else if(pathParameters[1].equals("facebook")){
				 user =resolveUser(context, jsonObj, configuration);
			}

			if(user != null){
				/*
				 * Create a log record
				 * Create a session for the user
				 */
				Core.getLogger("OauthCallback").debug("User found: "+user.getName());
				new LogRecordHandler().createLogRecord(jsonObj.toString(),OAuthMessage.Access);
				IUser iUser = Core.getUser(context, user.getName());
				LoginHelper.createSession(request, response, context, iUser);
			}else{
				/*
				 * Create a log record for the unauthorized access attempt
				 * Show the unauthorized access page
				 */
				Core.getLogger("OauthCallback").debug("User not found");
				new LogRecordHandler().createLogRecord(jsonObj.toString(),OAuthMessage.Unauthorized);
				new ErrorHandler().processErrorHandler(servletResponse, OAUTHDIR, UNAUTHHTML);
				return;

			}
		} catch (ParseException e) {
			Core.getLogger("OauthCallback").error("Response for userinfo is not the expected format. /n"+ e.getStackTrace());
		}
	}

	/**
	 * Method to call a microflow that will resolve the user based on the mfInput parameter
	 */
	private User resolveUser(IContext context, JSONObject json, OAuthConfig configuration) {
		try {
			Microflows microflow = configuration.getOAuthConfig_Microflows();
			if (microflow == null) {
				throw new CoreRuntimeException(
						"Microflow to resolve user not set in GoogleOauth configuration, please contact the application administrator");
			}
			List<IMendixObject> parametersObjs = Core.retrieveByPath(context, 
					configuration.getMendixObject(), Parameter.MemberNames.Parameter_OAuthConfig.toString());
			List<Parameter> parameters = new LinkedList<Parameter>();
			
			for (IMendixObject parametersObj : parametersObjs) {
				parameters.add(Parameter.initialize(context, parametersObj));
			}
			
			Map<String, Object> params = new HashMap<String, Object>();
			String actionName = microflow.getCompleteName();
			
			for (Parameter parameter : parameters) {
				mxmodelreflection.proxies.Parameter reflParameter = parameter.getParameter_Parameter();
				if (reflParameter != null) {
					Object value = json.get(parameter.getJSONKey());
					params.put(reflParameter.getName(), (value != null ? value.toString() : null) );
				}
			}
			
			Core.getLogger("OauthCallback").debug("Calling action "+actionName+" with params: "+params);
			Object result = Core.execute(context, actionName, params);
			/*
			 * If user not found then return null
			 * Else return the user object
			 */
			if (result == null) {
				return null;
			}
			else{
				User user = User.initialize(context, (IMendixObject) result);
				Core.getLogger("OauthCallback").debug("Resolved user "+user.getName());
				return user;
			}
		} catch (CoreException ex) {
			Core.getLogger("OauthCallback").error("Exception occurred while resolving user "+ ex);
			throw new CoreRuntimeException("Failed to resolve user");
		}
	}
	
	private OAuthConfig getConfig(IContext context){
		try {
			OAuthConfig configuration = XPath.create(context, OAuthConfig.class).first();
			if (configuration == null) {
				throw new CoreRuntimeException(
						"Missing Oauth configuration, please contact the application administrator");
			}
			return configuration;
		} catch (CoreException e) {
			Core.getLogger("OauthCallback").error("Something went wrong while retrieving the Oauth configuration. \n"+ e);
			
		}
		return null;
		
		
		
	}
}
