## Open Authentication Module
<table>
<tr> 
    <td bgcolor="#DDD"> Name</td><td>Open Authentication Module</td>
</tr>
<tr> 
    <td bgcolor="#DDD"> Author</td><td>Erwin 't Hoen</td>
</tr>
<tr> 
    <td bgcolor="#DDD"> Company</td><td>FlowFabric</td>
</tr>
<tr> 
    <td bgcolor="#DDD"> Type</td><td>Module</td>
</tr>
<tr> 
    <td bgcolor="#DDD"> Latest version</td><td>1.2</td>
</tr>
<tr> 
    <td bgcolor="#DDD"> Package name</td><td>OAuthModule_v1.2.mpk</td>
</tr>
<tr> 
    <td bgcolor="#DDD"> Released</td><td>10-03-2014</td>
</tr>
</table>

##Description


The Open Authentication Module allows you to setup your app for authenticating users with their Google, Linkedin or Facebook account.
The module can easily be expanded to incorporate any OAuth 2 provider.

##What is OAuth?

OAuth 2 is an open standard to authorization. OAuth provides client applications a 'secure delegated access' to server resoources on behalf of a resource owner. It specifies a process for resource owners to authorize thrid-party access to their server resources without sharing their credentials. Designed specifically to work with HTTP, OAuth essentially allows access tokens to be issued to third-party clients by an end-user. The client then uses the access token to access the protected resources hosted by the resource server. OAuth is commonly used as a way for web surfers to log into third party web sites using their Google, Facebook or Twitter accounts, without worrying about their access credentials being comprimized.


##Services that support OAuth 2
<table>
<tr><td>
- <a href="http://groups.google.com/group/37signals-api/browse_thread/thread/86b0da52134c1b7e" target="_blank">37signals (draft 5)</a><br>
- <a href="http://developers.box.com/oauth/" target="_blank">Box</a><br>
- <a href="http://beeminder.com/api" target="_blank">Beeminder</a><br>
- <a href="http://www.campaignmonitor.com/api/getting-started/#authenticating_with_oauth" target="_blank">Campaign Monitor</a><br>
- DailyMotion<br>
- <a href="https://do.com" target="_blank">Do.com (draft 22)</a><br>
- <a href="https://www.dropbox.com/developers/core/docs#oa2-authorize" target="_blank">Dropbox</a><br>
- <a href="http://developers.facebook.com/docs/authentication/" target="_blank">Facebook</a> <a href="http://www.sociallipstick.com/?p=239">(see here)</a><br>
- <a href="https://developer.foursquare.com/overview/auth" target="_blank">Foursquare</a><br>
- <a href="https://developers.geoloqi.com" target="_blank">Geologi</a><br>
</td>
<td>
- <a href="http://developer.github.com/v3/oauth/" target="_blank">Github</a><br>
- <a href="https://developers.google.com/accounts/docs/OAuth2" target="_blank">Google</a><br>
- <a href="https://developer.linkedin.com/documents/authentication" target="_blank">Linkedin</a><br>
- Mailchimp<br>
- <a href="http://www.meetup.com/meetup_api/auth/#oauth2" target="_blank">Meetup</a>
- <a href="http://nationbuilder.com/api_quickstart" target="_blank">Nationbuilder</a><br>
- Paypal<br>
- Reddit<br>
- <a href="http://www.salesforce.com/us/developer/docs/api_rest/Content/quickstart_oauth.htm" target="_blank">Salesforce</a><br>
- Scoop.it<br>
</td>
<td>
- <a href="http://www.sharefile.com/" target="_blank">Sharefile Citrix</a><br>
- StockTwits<br>
- <a href="http://developers.soundcloud.com/docs/api/reference" target="_blank">Soundcloud</a><br>
- Vimeo<br>
- <a href="http://msdn.microsoft.com/en-us/library/live/hh243647.aspx" target="_blank">Windows Live</a><br>
- WePay<br>
- Wordpress<br>
- Yahoo<br>
- Yammer
</td>
</table>
*For a complete list see <a href="http://www.cheatography.com/kayalshri/cheat-sheets/oauth-end-points/" target="_blank">here</a>*

## Typical usage scenario


If you want to have your users login to their Mendix app with their credentials from their favorite social network or cloud app. For example when using Chrome the user already logs in automatically into their Google account, then the Oauth module will allow the user to be logged in directly into their Mendix app (SSO).


If you need 2-factor authentication for accessing your app, then make use of the options that Google offers for this purpose in stead of build 2-factor authentication from scratch in your Mendix app. <br>
Using a corporate Google account, users can be setup so that the login process will require 2-factor authentication. <a href="https://support.google.com/a/answer/184711?hl=en" target="_blank">This</a> is how you set this up on Google

## Features and limitations


<li> Authenticate users via Facebook, Google or Linkedin OOTB</li>
<li> Or use only one of these providers (pick and choose)</li>
<li> Use 2-factor authentication provided by the OAuth providers</li>
<li> Style your own fault pages</li>
<li> Logging for all access attempts</li>
<li> CSRF protection</li>
<li> English and Dutch supported</li>
<li> Easily add your own OAuth 2 provider</li>
<li> Determine how you identify your authorized users based on the data provided by the OAuth provider <br>&nbsp;&nbsp;  (OOTB this is done based on the email address)</li>
<li> Implement your own logic for authentication of users even add user provisioning with a simple microflow</li>
<li> If you do not implement user provisioning then the users have to be known in the app by integration to an &nbsp;&nbsp;&nbsp;Active Directory or an Identity Management solution</li>
<li> Control if an Admin can login via the standard Mendix login circumventing the OAuth login via adaptation in &nbsp;&nbsp;&nbsp;your theme directory</li>
<li> Javadoc for custom code provided in the custom directory </li>


## Installation & Configuration

1. Download the zip file from Github
2. Extract the zip
3. Import the module (.mpk) into you application as a new module
4. From the theme directory copy all the files to your theme directory
5. Connect the page and microflow from the #Implementation folder to your navigation Assign the permissions to the userrole(s)
6. Import the Community Commons module from the app store if not already part of your project
7. Import the Model Reflection module from the app store  if not already part of your project
8. Set the microflow AS_StartOAuthRequestHandlers as After Startup Microflow
9. Add the attribute Email to the Administration.Account entity and pages
10. Register your app with the OAuth provider, make sure that the callback URL is https://(yourapp)/callback/(OAuth_provider)
For Google e.g. http://myfirstapp.mendixcloud.com/callback/google
11. Update the constants ClientId_(OAuth provider) and ClientSecret_(OAuth provider)
12. The next setup step for your OAuth module is:  navigate to https://(yourapp)/admin.html and login with your Admin account
13. Synchronize your Model Reflection module and make sure that the data for the OAuthModule is created
14. Select the OAuth Config menu item and select the microflow ResolveUserByEmail
15. And you're done!
 

16. Wait, what if security requirements are more strict? Perform step 17
17. Delete the admin.html from your theme folder to make sure that OAuth is the only login option, and redeploy your app
18. Don't forget to set your requesthandlers in the cloud ('signin/' and 'callback/')
19. Done!

If you want to use another OAuth provider than those that come with the module read the Add_OAuth_Provider_v1.2.pdf file on github.
To implement your own resolve user logic either adapt the microflow ResolveUserByEmail or create your own microflow and link this in the OAuth config.

## Dependencies
 

1. Mendix 5.8.1 environment
2. Mx Model Reflection module
3. Community Commons module
4. apache-httpcomponents-httpclient.jar
5. httpclient-4.3.5.jar
6. json-simple.jar
7. org.apache.httpcomponents.httpclient_4.3.5.jar


## Known bugs
 

* None so far
 

## Frequently Asked Questions


Ask your question at the Mendix Community <a href="https://mxforum.mendix.com/" target="_blank">Forum</a>

* None





