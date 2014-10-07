package oauthmodule.actions.custom;

import java.io.IOException;
import oauthmodule.proxies.constants.Constants;
import org.apache.http.client.ClientProtocolException;
import com.google.common.collect.ImmutableMap;
import com.mendix.core.Core;
/**
 * HelperClass for retrieving a accesstoken from Facebook Oauth
 * 
 * @Author: Erwin 't Hoen
 * @version: 1.0
 * @since: 2014-10-02
 */
public class GetAccessTokenFacebook extends GetAccessToken{

	private final String CLIENTID = Constants.getClientId_Facebook();
	private final String CLIENTSECRET = Constants.getClientSecret_Facebook();
	private final String CALLBACKURI =  Constants.getCallbackURI_Facebook();
	private final String OAUTHTOKENURI = Constants.getOAuthTokenURI_Facebook();
	private ImmutableMap<String, String> map = ImmutableMap.<String,String>builder()
			.put("code", code)
			.put("redirect_uri", CALLBACKURI)
			.put("client_id", CLIENTID)
			.put("client_secret", CLIENTSECRET)
			.build();
	
	public GetAccessTokenFacebook(String code) {
		super(code);
	}
	
	protected String getResult() throws ClientProtocolException, IOException{
		Core.getLogger("OauthCallback").debug("Get access token from Facebook");
		return PostHttpRequest.post(OAUTHTOKENURI, map);
	}
}
