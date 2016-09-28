package oauthmodule.actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.mendix.core.Core;
import com.mendix.externalinterface.connector.RequestHandler;
import com.mendix.m2ee.api.IMxRuntimeRequest;
import com.mendix.m2ee.api.IMxRuntimeResponse;
import com.mendix.systemwideinterfaces.core.ISession;

public class JA_LogoutHandler extends RequestHandler {

	@Override
	public void processRequest(IMxRuntimeRequest request, IMxRuntimeResponse response, String path) throws Exception
	{
		final String LOGOUTPAGELOCATION = "/Oauth/logout.html";
		ISession session = getSessionFromRequest(request);
		if (session != null){
			Core.logout(session);
			}
		StringBuilder stringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(Core.getConfiguration().getResourcesPath()+LOGOUTPAGELOCATION)))
		{
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				stringBuilder.append(sCurrentLine);
			}   
			String logoutPage = stringBuilder.toString();
			OutputStream outputStream = response.getOutputStream();
			IOUtils.write(logoutPage, outputStream);
			IOUtils.closeQuietly(outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}