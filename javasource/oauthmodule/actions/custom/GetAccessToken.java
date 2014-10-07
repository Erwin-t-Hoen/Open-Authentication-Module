package oauthmodule.actions.custom;
/**
 * HelperSuperClass for retrieving a Accesstokens from Oauth providers
 * Currently configured ootb are
 * Google
 * Linkedin
 * Facebook
 * 
 * @Author: Erwin 't Hoen
 * @version: 1.0
 * @since: 2014-10-02
 */
public class GetAccessToken {
	String code;
	public GetAccessToken(String code) {
		super();
		this.code = code;
	}
}