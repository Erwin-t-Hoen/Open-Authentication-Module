package oauthmodule.actions.custom;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import oauthmodule.proxies.constants.Constants;
import com.mendix.core.Core;
/**
 * HelperClass for retrieving a accesscode from Facebook Oauth
 * 
 * @Author: Erwin 't Hoen
 * @version: 1.0
 * @since: 2014-10-02
 */
public class GetAccessCodeFacebook {
	
	private final String OAUTHURI = Constants.getOAuthURI_Facebook();
	private final String CLIENTID = Constants.getClientId_Facebook();
	private final String CALLBACKURI = Constants.getCallbackURI_Facebook();
	
	protected void getCode(String UUIDstate, HttpServletResponse servletResponse) throws IOException{
	Core.getLogger("OAuthSignin").trace("Get token from Facebook");
	StringBuilder oauthUrl = new StringBuilder()
	.append(OAUTHURI)
	.append("?client_id=").append(CLIENTID) // the client id from the api console registration
	.append("&redirect_uri=").append(CALLBACKURI) // the servlet that linkedin redirects to after authorization
	.append("&scope=email") // scope is the api permissions we are requesting
	.append("&state="+UUIDstate);
	Core.getLogger("OAuthSignin").trace("Url used for facebook: \n"+oauthUrl.toString());
	servletResponse.sendRedirect(oauthUrl.toString());
	}
}
