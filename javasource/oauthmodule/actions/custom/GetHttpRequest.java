package oauthmodule.actions.custom;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import com.mendix.core.Core;
/**
 * HelperClass for Get requests to Oauth Providers
 * 
 * @Author: Erwin 't Hoen
 * @version: 1.0
 * @since: 2014-10-02
 */
public class GetHttpRequest {
	/*
	 *  makes a GET request to url and returns body as a string
	 */
	protected String get(String url) throws ClientProtocolException, IOException {
		Core.getLogger("OAuthCallback").trace("URI is: "+ url);
		return new ExecuteHttpRequest().execute(new HttpGet(url));

	}

}
