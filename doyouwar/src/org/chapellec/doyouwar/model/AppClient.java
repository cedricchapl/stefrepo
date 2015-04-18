/**
 * 
 */
package org.chapellec.doyouwar.model;

import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chapellec.doyouwar.ProjectParams;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author Cédric Chapelle
 *
 */
public class AppClient {

	private static final Logger logger = LogManager.getLogger(AppClient.class);

	private Client build() {
		Client client;
		try {
			// configure the SSLContext with a TrustManager
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() },
					new SecureRandom());

			ClientBuilder clientBuilder = ClientBuilder.newBuilder().sslContext(ctx)
					.hostnameVerifier(new HostnameVerifier() {

						@Override
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					});
			client = clientBuilder.build();
		} catch (Exception e) {
			logger.error("Impossible to obtain a REST client configured with all certifs SSL trust");
			client = null;
		}
		return client;
	}

	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	/**
	 * 
	 * @param encrypted
	 * @return
	 */
	public static URI getBaseURI(boolean encrypted) {
		if (logger.isDebugEnabled()) {
			logger.debug("start getBaseURI(" + encrypted + ")");
		}

		String restURL = ProjectParams.getProperty(ProjectParams.REST_URL_KEY);
		UriBuilder builder = UriBuilder.fromUri(restURL);
		URI restURI = null;
		logger.debug("predefined rest url to use : " + restURL);
		if (encrypted) {			
			restURI = builder.build();
		} else {
			logger.debug("force the use of the http rest url to avoid ssl certficats problems");
			restURI = builder.scheme("http:").build();
		}

		return restURI;
	}

	/**
	 * Obtenir les stats depuis le serveur
	 * 
	 * @return les stats si réussite, null sinon
	 */
	public ServerStats get() {
		logger.debug("start get()");

		WebTarget target = build().target(AppClient.getBaseURI(true));
		Response resp = target
				.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION,
						"Basic " + ProjectParams.getProperty("rest.auth")).get(Response.class);

		if (resp.getStatus() == HttpsURLConnection.HTTP_OK) {
			ServerStats serverStats = resp.readEntity(ServerStats.class);
			logger.debug("get() response : " + resp);
			logger.debug(serverStats);
			return serverStats;
		}
		logger.error("get(), response error : " + resp);
		return null;
	}

	/**
	 * Transférer des réponses vers le serveur
	 * 
	 * @param yes
	 *            nb de yes
	 * @param no
	 *            nb de no
	 * @return true si réussite du transfert
	 */
	public boolean post(int yes, int no) {
		logger.debug("start post()");

		WebTarget target = build().target(AppClient.getBaseURI(true));
		JSONObject data = null;
		try {
			data = new JSONObject("{\"yes\":" + yes + ", \"no\":" + no + "}");
		} catch (JSONException e) {
			logger.error(e);
		}

		Response resp = target
				.request()
				.header(HttpHeaders.AUTHORIZATION,
						"Basic " + ProjectParams.getProperty("rest.auth"))
				.post(Entity.entity(data, MediaType.APPLICATION_JSON));
				
		if (resp.getStatus() == HttpsURLConnection.HTTP_CREATED) {
			logger.debug("post() response : " + resp);
			logger.debug("json data sent : " + data);		
			return true;
		}
		logger.error("post(), response error : " + resp);
		logger.error("json data sent : " + data);
		return false;
	}

}