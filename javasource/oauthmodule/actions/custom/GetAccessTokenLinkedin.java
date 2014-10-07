package oauthmodule.actions.custom;

import java.io.IOException;
import oauthmodule.proxies.constants.Constants;
import org.apache.http.client.ClientProtocolException;
import com.google.common.collect.ImmutableMap;
import com.mendix.core.Core;
/**
 * HelperClass for retrieving a accesstoken from Linkedin Oauth
 * 
 * @Author: Erwin 't Hoen
 * @version: 1.0
 * @since: 2014-10-02
 */
public class GetAccessTokenLinkedin extends GetAccessToken{

	private final String CLIENTID = Constants.getClientId_Linkedin();
	private final String CLIENTSECRET = Constants.getClientSecret_Linkedin();
	private final String CALLBACKURI =  Constants.getCallbackURI_Linkedin();
	private final String OAUTHTOKENURI = Constants.getOAuthTokenURI_Linkedin();
	private ImmutableMap<String, String> map = ImmutableMap.<String,String>builder()
			.put("code", code)
			.put("redirect_uri", CALLBACKURI)
			.put("client_id", CLIENTID)
			.put("client_secret", CLIENTSECRET)
			.build();

	public GetAccessTokenLinkedin(String code) {
		super(code);
	}

	protected String getResult() throws ClientProtocolException, IOException{
		Core.getLogger("OauthCallback").debug("Get access token from Linkedin");
		return PostHttpRequest.post(OAUTHTOKENURI, map);
	}


}
