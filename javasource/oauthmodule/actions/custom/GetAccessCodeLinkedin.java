package oauthmodule.actions.custom;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import oauthmodule.proxies.constants.Constants;
import com.mendix.core.Core;
/**
 * HelperClass for retrieving a accesscode from Linkedin Oauth
 * 
 * @Author: Erwin 't Hoen
 * @version: 1.0
 * @since: 2014-10-02
 */
public class GetAccessCodeLinkedin {
	
	private final String OAUTHURI = Constants.getOAuthURI_Linkedin();
	private final String CLIENTID = Constants.getClientId_Linkedin();
	private final String CALLBACKURI = Constants.getCallbackURI_Linkedin();
	
	protected void getCode(String UUIDstate, HttpServletResponse servletResponse) throws IOException{
	Core.getLogger("OAuthSignin").trace("Get token from Linkedin");
	StringBuilder oauthUrl = new StringBuilder()
		.append(OAUTHURI)
		.append("?response_type=code")
		.append("&client_id=").append(CLIENTID) // the client id from the api console registration
		.append("&scope=r_fullprofile%20r_emailaddress") // scope is the api permissions we are requesting
		.append("&redirect_uri=").append(CALLBACKURI) // the servlet that linkedin redirects to after authorization
		.append("&state="+UUIDstate);
	Core.getLogger("OAuthSignin").trace("Url used for linkedin: \n"+oauthUrl.toString());
	servletResponse.sendRedirect(oauthUrl.toString());
	}
}
