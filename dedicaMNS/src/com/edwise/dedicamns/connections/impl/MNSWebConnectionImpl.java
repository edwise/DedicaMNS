/**
 * 
 */
package com.edwise.dedicamns.connections.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.maxters.android.ntlm.NTLM;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.edwise.dedicamns.beans.ActivityDay;
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.beans.MonthYearBean;
import com.edwise.dedicamns.beans.ProjectSubprojectBean;
import com.edwise.dedicamns.connections.ConnectionException;
import com.edwise.dedicamns.connections.WebConnection;

/**
 * @author edwise
 * 
 */
public class MNSWebConnectionImpl implements WebConnection {
    private static final String LOGTAG = MNSWebConnectionImpl.class.toString();

    private static final int FIRST_YEAR = 2004;

    private static final String URL_STR = "http://dedicaciones.medianet.es";
    private static final String DOMAIN = "medianet2k";
    private static final String COOKIE_SESSION = "ASP.NET_SessionId";

    private static final String URL_ACCOUNTS_STR = "http://dedicaciones.medianet.es/Home/Accounts";

    private DefaultHttpClient client = null;
    private String cookie = null;
    private MonthYearBean monthYears = null;
    private ProjectSubprojectBean projects = null;

    public boolean isOnline(Activity activity) {
	boolean online = false;
	ConnectivityManager cm = (ConnectivityManager) activity
		.getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public Integer connectWeb(String userName, String password) throws ConnectionException {
	int responseCode;
	try {
	    Log.d(LOGTAG, "connectWeb... inicio...");
	    responseCode = doLoginAndGetCookie(URL_STR, DOMAIN, userName, password);
	    Log.d(LOGTAG, "connectWeb... fin...");
	} catch (Exception e) {
	    // TODO controlar el error devolviendo un responsecode erroneo?
	    Log.e(LOGTAG, "Error en el acceso web", e);
	    throw new ConnectionException(e);
	}
	return responseCode;
    }

    private int doLoginAndGetCookie(final String urlStr, final String domain, final String userName,
	    final String password) throws ClientProtocolException, IOException, URISyntaxException {
	long beginTime = System.currentTimeMillis();

	URL url = new URL(urlStr);
	client = new DefaultHttpClient();
	NTLM.setNTLM(client, userName, password, domain, null, -1);
	HttpGet get = new HttpGet(url.toURI());

	HttpResponse resp = client.execute(get);
	List<Cookie> cookies = client.getCookieStore().getCookies();
	for (Cookie c : cookies) {
	    Log.d(LOGTAG, "Cookie - Name: " + c.getName() + " Value: " + c.getValue());
	    if (COOKIE_SESSION.equals(c.getName())) {
		cookie = c.getValue();
	    }
	}

	Log.d(LOGTAG, "StatusCode: " + resp.getStatusLine().getStatusCode() + " StatusLine: "
		+ resp.getStatusLine().getReasonPhrase());

	long endTime = System.currentTimeMillis();
	Log.d(LOGTAG, "Tiempo login: " + (endTime - beginTime));

	// 200 OK. 401 error
	return resp.getStatusLine().getStatusCode();
    }

    public List<String> getMonths() {
	return this.monthYears.getMonths();
    }

    public List<String> getYears() {
	return this.monthYears.getYears();
    }

    public List<String> getArrayProjects() {
	return this.projects.getProjects();
    }

    public List<String> getArraySubProjects(String projectId) {
	return this.projects.getSubProjects(projectId);
    }

    public void fillProyectsAndSubProyectsCached() throws ConnectionException {
	if (projects == null) {
	    long beginTime = System.currentTimeMillis();

	    fillProyectsAndSubProyects();

	    long endTime = System.currentTimeMillis();
	    Log.d(LOGTAG, "Tiempo carga proyectos: " + (endTime - beginTime));
	}
    }

    private void fillProyectsAndSubProyects() throws ConnectionException {
	String html = this.getHttpContent(URL_ACCOUNTS_STR);
	Document document = Jsoup.parse(html);

	Elements selectSpansAccounts = document.select("span.Account");
	if (selectSpansAccounts != null) {
	    List<String> projects = new ArrayList<String>();
	    Map<String, List<String>> projectsAndSubProjects = new HashMap<String, List<String>>();
	    projects.add(ProjectSubprojectBean.PROJECT_DEFAULT);
	    projectsAndSubProjects.put(ProjectSubprojectBean.PROJECT_DEFAULT,
		    ProjectSubprojectBean.createSubProjectsDefault());
	    Iterator<Element> it = selectSpansAccounts.iterator();
	    while (it.hasNext()) {
		Element span = it.next();
		String projectId = parseStringProjectId(span.html());
		Element liParent = span.parent();
		Element nextLiOrUl = liParent.nextElementSibling();

		List<String> subProjects = new ArrayList<String>();
		subProjects.add(ProjectSubprojectBean.SUBPROJECT_DEFAULT);
		if (nextLiOrUl != null) {
		    Elements selectSpansSubAccounts = nextLiOrUl.select("span.Subaccount");
		    if (selectSpansSubAccounts != null) {
			Iterator<Element> subIt = selectSpansSubAccounts.iterator();
			while (subIt.hasNext()) {
			    Element subSpan = subIt.next();
			    subProjects.add(subSpan.html());
			}
		    }
		}

		projects.add(projectId);
		projectsAndSubProjects.put(projectId, subProjects);

	    }

	    this.projects = new ProjectSubprojectBean(projects, projectsAndSubProjects);
	}
    }

    private String parseStringProjectId(String projectName) {
	return projectName.trim().substring(0, projectName.indexOf(" -"));
    }

    public void fillMonthsAndYearsCached() {
	if (monthYears == null) {
	    fillMonthsAndYears();
	}
    }

    private void fillMonthsAndYears() {
	List<String> months = generateMonthsList();
	List<String> years = generateYearsList();

	this.monthYears = new MonthYearBean(months, years);
    }

    private List<String> generateMonthsList() {
	List<String> monthsList = new ArrayList<String>();
	monthsList.add("Enero");
	monthsList.add("Febrero");
	monthsList.add("Marzo");
	monthsList.add("Abril");
	monthsList.add("Mayo");
	monthsList.add("Junio");
	monthsList.add("Julio");
	monthsList.add("Agosto");
	monthsList.add("Septiembre");
	monthsList.add("Octubre");
	monthsList.add("Noviembre");
	monthsList.add("Diciembre");

	return monthsList;
    }

    private List<String> generateYearsList() {
	List<String> yearsList = new ArrayList<String>();

	Calendar today = Calendar.getInstance();
	int lastYear = today.get(Calendar.YEAR);
	if (today.get(Calendar.MONTH) == Calendar.DECEMBER) {
	    // Si es diciembre, le añadimos ya el año siguiente, deberia estar ya...
	    lastYear++;
	}

	for (int i = FIRST_YEAR; i <= lastYear; i++) {
	    yearsList.add(String.valueOf(i));
	}

	return yearsList;
    }

    private String getHttpContent(String url) throws ConnectionException {
	String html = null;
	try {
	    URL urlObject = new URL(url);
	    HttpGet get = new HttpGet(urlObject.toURI());
	    HttpResponse resp = client.execute(get);
	    InputStream is = resp.getEntity().getContent();

	    StringWriter writer = new StringWriter();
	    IOUtils.copy(is, writer, "UTF-8");

	    html = writer.toString();
	} catch (URISyntaxException e) {
	    Log.d(LOGTAG, "Error de URI en el acceso getHttp a " + url, e);
	    throw new ConnectionException(e);
	} catch (IOException e) {
	    Log.d(LOGTAG, "Error IO en el acceso getHttp a " + url, e);
	    throw new ConnectionException(e);
	}

	return html;
    }

    // TODO refactorizar bien y constantizar
    public List<DayRecord> getListDaysForMonth() throws ConnectionException {
	List<DayRecord> listDays = new ArrayList<DayRecord>();
	String html = this.getHttpContent(URL_STR);
	Document document = Jsoup.parse(html);

	Elements selectUlDays = document.select("#ListOfDays");
	if (selectUlDays != null) {
	    Element ulDays = selectUlDays.first();
	    Elements liDays = ulDays.children();
	    Iterator<Element> itDays = liDays.iterator();
	    while (itDays.hasNext()) {
		Element liDay = itDays.next();
		Elements spanDayNumbers = liDay.select(".DayNumber");
		Elements spanDayNInitials = liDay.select(".DayInitials");
		Elements spanTotalHours = liDay.select(".TotalHours");

		DayRecord dayRecord = new DayRecord();
		dayRecord.setHours(spanTotalHours.first().html());
		dayRecord.setIsWeekend("WeekendDay".equals(liDay.className()));
		dayRecord.setIsHoliday("Holiday".equals(liDay.className()));
		dayRecord.setDayNum(Integer.valueOf(spanDayNumbers.first().html()));
		dayRecord.setDayName(spanDayNInitials.first().html());

		Elements selectUlActivities = liDay.select("ul.Activities");
		Element ulActivities = selectUlActivities.first();
		Elements liActivities = ulActivities.children();
		Iterator<Element> itAct = liActivities.iterator();
		while (itAct.hasNext()) {
		    Element liActivity = itAct.next();
		    ActivityDay activityDay = new ActivityDay();
		    activityDay.setHours(liActivity.select("div.ActivityHours").html());
		    activityDay.setProjectId(liActivity.select("div.ActivityAccount span").html());
		    activityDay.setSubProject("");
		    activityDay.setSubProjectId(liActivity.select("div.ActivitySubaccount span").html());
		    activityDay.setTask(liActivity.select("div.ActivityTask").html());

		    dayRecord.getActivities().add(activityDay);
		}

		listDays.add(dayRecord);
	    }
	} else {
	    throw new ConnectionException("No existen datos de días en la página recibida!");
	}

	return listDays;
    }

    final static String[] dayNames = { "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado",
	    "Domingo" };

}
