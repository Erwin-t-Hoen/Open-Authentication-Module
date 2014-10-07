package oauthmodule.actions.custom;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import oauthmodule.proxies.constants.Constants;
import com.google.common.collect.ImmutableMap;
import com.mendix.core.Core;
/**
 * HelperClass for retrieving a accesstoken from Google Oauth
 * 
 * @Author: Erwin 't Hoen
 * @version: 1.0
 * @since: 2014-10-02
 */

public class GetAccessTokenGoogle extends GetAccessToken {

	private final String CLIENTID = Constants.getClientId_Google();
	private final String CLIENTSECRET = Constants.getClientSecret_Google();
	private final String CALLBACKURI =  Constants.getCallbackURI_Google();
	private final String OAUTHTOKENURI = Constants.getOAuthTokenURI_Google();
	private ImmutableMap<String, String> map = ImmutableMap.<String,String>builder()
	.put("code", code)
	.put("client_id", CLIENTID)
	.put("client_secret", CLIENTSECRET)
	.put("redirect_uri", CALLBACKURI)
	.put("grant_type", "authorization_code").build();

	public GetAccessTokenGoogle(String code) {
		super(code);
	}

	protected String getResult() throws ClientProtocolException, IOException{
		Core.getLogger("OauthCallback").debug("Get access token from Google");
		return PostHttpRequest.post(OAUTHTOKENURI, map);
	}

}
