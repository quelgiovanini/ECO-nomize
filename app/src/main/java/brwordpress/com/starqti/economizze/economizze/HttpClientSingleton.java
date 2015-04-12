package brwordpress.com.starqti.economizze.economizze;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpClientSingleton {
	   
    private static final int JSON_CONNECTION_TIMEOUT = 6000;
    private static final int JSON_SOCKET_TIMEOUT = 10000; 
    private static HttpClientSingleton instance;
    private HttpParams parametros ;
    HttpClient client;
   
    private void setTimeOut(HttpParams params){
	     HttpConnectionParams.setConnectionTimeout(params, JSON_CONNECTION_TIMEOUT);
	     HttpConnectionParams.setSoTimeout(params, JSON_SOCKET_TIMEOUT);
    }

    private HttpClientSingleton() {
	     parametros = new BasicHttpParams();
	     setTimeOut(parametros);
	     client = HttpClientSingleton.getHttpsClient(new DefaultHttpClient()); 
    }
    
    public static HttpClient getHttpClientInstance() {
		 if (instance == null)
		 instance = new HttpClientSingleton();
		 
		 return instance.client;
	}
	
	public static HttpClient getHttpsClient(HttpClient client) {
		
		try {
			X509TrustManager x509TrustManager = new X509TrustManager() {
				
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}
				
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}
				
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
			
			SSLSocketFactory sslSocketFactory = new ConexaoSSL(sslContext);
			sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager clientConnectionManager = client.getConnectionManager();
			
			SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
			
			return new DefaultHttpClient(clientConnectionManager, client.getParams());
			
		} catch (Exception ex) {
			return null;
		}
	}
 
}