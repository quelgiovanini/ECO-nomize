package brwordpress.com.starqti.economizze.economizze;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.util.Log;

public class WebServiceCliente {

	public final String[] get (String url) {
		
		String resultado[] = new String[2];
		HttpGet httpget = new HttpGet(url);
		HttpResponse httpresponse;
		
		try {
			httpresponse = HttpClientSingleton.getHttpClientInstance().execute(httpget);
			HttpEntity entity = httpresponse.getEntity();
			
			if (entity != null) {
				
				resultado[0] = String.valueOf(httpresponse.getStatusLine().getStatusCode());
				InputStream inputstream = entity.getContent();
				resultado[1] = toString(inputstream);
				inputstream.close();
				
				Log.i("GET", "JSONGET: " + resultado[0] + " : " + resultado[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return resultado;
	}
	

	public final String[] post (String url, String json) {
		
		String resultado[] = new String[2];
		
		try {
			HttpPost httpPost = new HttpPost(new URI(url));
			httpPost.setHeader("Content-Type", "application/json");
			StringEntity sEntity = new StringEntity(json, "UTF-8");
			httpPost.setEntity(sEntity);
			
			HttpResponse response;
			response = HttpClientSingleton.getHttpClientInstance().execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				
				resultado[0] = String.valueOf(response.getStatusLine().getStatusCode());
				InputStream is = entity.getContent();
				
				resultado[1] = toString(is);
				is.close();
				
				Log.i("POST", "JSONPOST: " + resultado[0] + " : " + resultado[1]);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	private String toString(InputStream is) throws IOException {
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int lidos;
		
		while ((lidos = is.read(bytes)) > 0) {
			baos.write(bytes, 0, lidos);
		}
		return new String(baos.toByteArray());
	}
}
