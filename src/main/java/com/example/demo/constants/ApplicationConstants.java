package com.example.demo.constants;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fca.myway.userdata.exception.PublisherLayerException;
import com.fca.myway.userdata.model.Appointment;

public class ApplicationConstants {

	public ApplicationConstants() {

	}
	
	https://samples.openweathermap.org/data/2.5/forecast?q=London,us&appid=d2929e9483efc82c82c32ee7e02d5%2063e

		
		@Test
		public void errorScheduler() throws PublisherLayerException, ClientProtocolException, IOException {
			Appointment appointment = Mockito.mock(Appointment.class);
			Mockito.when(sline.getStatusCode()).thenReturn(500);
			Mockito.when(httpResponse.getStatusLine()).thenReturn(sline);
			InputStream cont = new ByteArrayInputStream("2".getBytes());
			Mockito.when(ent.getContent()).thenReturn(cont);
			Mockito.when(httpResponse.getEntity()).thenReturn(ent);
			Mockito.when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(httpResponse);
			assertThrows(PublisherLayerException.class,()-> scheduleService.scheduler(appointment));
			
	public static final String APP_NAME = "Demo";
	public static final String APP_DESC = "This microservice connects with external system and fetches the required  Data such as High/Low Temp etc.";
	public static final String APP_VERSION = "1.0";
	public static final String LICENSE_INFO = "This is PS property";
	public static final String FOLDER_STRUCTURE = "com.example";
	public static final String BASE_REQUEST_PATH = "/weather";
	public static final String URL = "http://localhost";
	

}
