/**
 * 
 */
package com.edwise.dedicamns.connections.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import net.maxters.android.ntlm.NTLM;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.edwise.dedicamns.connections.WebConnection;

/**
 * @author edwise
 * 
 */
public class MNSWebConnectionImpl implements WebConnection {

    private static final String URL_STR = "http://dedicaciones.medianet.es";
    private static final String DOMAIN = "medianet2k";
    private static final String COOKIE_SESSION = "ASP.NET_SessionId";

    private static String cookie = null;

    public boolean isOnline(Activity activity) {
	boolean online = false;
	ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo netInfo = cm.getActiveNetworkInfo();
	if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	    online = true;
	}

	return online;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.edwise.dedicamns.connections.WebConnection#connectWeb()
     */
    public Integer connectWeb(String userName, String password) {
	int responseCode;
	try {
	    Log.d(MNSWebConnectionImpl.class.toString(), "connectWeb... inicio...");
	    responseCode = doLoginAndGetCookie(URL_STR, DOMAIN, userName, password);
	    Log.d(MNSWebConnectionImpl.class.toString(), "connectWeb... fin...");
	} catch (Exception e) {
	    // TODO controlar el error devolviendo un responsecode erroneo?
	    Log.e(MNSWebConnectionImpl.class.toString(), "Error en el acceso web", e);
	    throw new RuntimeException(e);
	}
	return responseCode;
    }

    private static int doLoginAndGetCookie(final String urlStr, final String domain, final String userName,
	    final String password) throws ClientProtocolException, IOException, URISyntaxException {

	URL url = new URL(urlStr);
	DefaultHttpClient client = new DefaultHttpClient();
	NTLM.setNTLM(client, userName, password, domain, null, -1);
	HttpGet get = new HttpGet(url.toURI());

	HttpResponse resp = client.execute(get);
	List<Cookie> cookies = client.getCookieStore().getCookies();
	for (Cookie c : cookies) {
	    Log.d(MNSWebConnectionImpl.class.toString(),
		    "Cookie - Name: " + c.getName() + " Value: " + c.getValue());
	    if (COOKIE_SESSION.equals(c.getName())) {
		cookie = c.getValue();
	    }
	}

	Log.d(MNSWebConnectionImpl.class.toString(), "StatusCode: " + resp.getStatusLine().getStatusCode()
		+ " StatusLine: " + resp.getStatusLine().getReasonPhrase());
	// 200 OK. 401 error
	return resp.getStatusLine().getStatusCode();
    }
}
