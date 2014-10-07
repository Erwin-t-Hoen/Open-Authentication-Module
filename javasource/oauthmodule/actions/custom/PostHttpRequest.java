package oauthmodule.actions.custom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
/**
 * This class performs post requests to the provided Url
 * @author Erwin 't Hoen
 * @version 1.0
 * @since 2014-10-02
 */

public class  PostHttpRequest {
	/**
	 * This method will generate the url with request parameters based on the input Map
	 * and post this request.
	 * @param url
	 * @param formParameters
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, Map<String,String> formParameters) throws ClientProtocolException, IOException {	
		HttpPost request = new HttpPost(url);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		for (String key : formParameters.keySet()) {
			nvps.add(new BasicNameValuePair(key, formParameters.get(key)));	
		}
		request.setEntity(new UrlEncodedFormEntity(nvps));
		return new ExecuteHttpRequest().execute(request);
	}

}
