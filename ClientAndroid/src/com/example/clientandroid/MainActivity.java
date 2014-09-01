package com.example.clientandroid;

import org.apache.http.message.LineParser;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity {
	
	private EditText edtUsuario;
	private EditText edtPassword;
	private Button btnLogin;
	private TextView txtUsuario;
	private TextView txtPassword;
	
	private TextView txtNombre;
	private TextView txtApellido;
	private TextView txtEmail;
	private TextView txtNickName;
	private TextView txtFoto;
	
	private static String nick;
	private static String pass;
	
	private RelativeLayout mainLayout;
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainLayout = new  RelativeLayout(this);
		ini();
		loadData();
		setContentView(mainLayout);		
			 
		
		
	}
	
	public void ini(){
		edtUsuario = new EditText(this);
		edtPassword = new  EditText(this);
		btnLogin = new Button(this);
		txtUsuario = new TextView(this);
		txtPassword = new  TextView(this);
		
		txtUsuario.setText("Usuario");
		txtPassword.setText("Password");
		btnLogin.setText("Login");
		
		edtUsuario.setWidth(500);
		edtUsuario.setHeight(50);
		
		edtPassword.setWidth(500);
		edtPassword.setHeight(50);
		edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		
	
		
		LinearLayout linearLogin = new LinearLayout(this);
		linearLogin.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout linearUsuario = new  LinearLayout(this);
		linearUsuario.setOrientation(LinearLayout.HORIZONTAL);
		linearUsuario.addView(txtUsuario);
		linearUsuario.addView(edtUsuario);
		
		LinearLayout linearPassword = new  LinearLayout(this);
		linearPassword.setOrientation(LinearLayout.HORIZONTAL);
		linearPassword.addView(txtPassword);
		linearPassword.addView(edtPassword);
		
		linearLogin.addView(linearUsuario);
		linearLogin.addView(linearPassword);
		linearLogin.addView(btnLogin);		
		
		mainLayout.addView(linearLogin);	
		
	}

	
	public void loadData(){
		txtNombre = new TextView(this);
		txtApellido  = new TextView(this);
		txtEmail = new TextView(this);
		txtNickName = new TextView(this);
		txtFoto = new TextView(this);
		
		LinearLayout linearName = new LinearLayout(this);
		linearName.setOrientation(LinearLayout.HORIZONTAL);
		txtNombre.setWidth(500);
		txtNombre.setHeight(50);
		txtApellido.setWidth(500);
		txtApellido.setHeight(50);
		txtEmail.setWidth(500);
		txtNickName.setHeight(50);
		System.out.println("HASTA ACA LLEGUE 4");
		txtNombre.setText("Nombre");
		txtApellido.setText("Apellido");
		txtEmail.setText("Email");
		txtNickName.setText("NickName");
		
		linearName.addView(txtNombre);
		linearName.addView(txtApellido);
		
		LinearLayout linearData = new LinearLayout(this);
		linearData.setOrientation(LinearLayout.HORIZONTAL);
		linearData.addView(txtEmail);
		linearData.addView(txtNickName);
		
		LinearLayout linearPerfil = new LinearLayout(this);
		linearPerfil.setOrientation(LinearLayout.VERTICAL);
		linearPerfil.addView(linearName);
		linearPerfil.addView(linearData);
		
		
		linearPerfil.setX((float) 0.0);
		linearPerfil.setY((float) 500.0);
		
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				nick = edtUsuario.getText().toString();
				pass = edtPassword.getText().toString();
			TareaWSConsulta tarea = new TareaWSConsulta();
			tarea.execute();	
			}
			});
		
		mainLayout.addView(linearPerfil);
		
				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class TareaWSConsulta extends AsyncTask<String,String,Boolean> {
		private String[] listaDatos;
		protected Boolean doInBackground(String... params) {			
		boolean resul = true;
		final String NAMESPACE = "http://WebServices.PhotoSharing.unsa.com";
		final String URL="http://192.168.1.34:8080/PhotoSharingPro/services/WsPerfil?wsdl";
		final String METHOD_NAME = "obtenerPerfil";		
		final String SOAP_ACTION = "/";
		try{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);		
		request.addProperty("nick",nick );
		request.addProperty("pass",pass );
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		//envelope.dotNet = true;
		envelope.bodyOut=request;
		
		//envelope.setOutputSoapObject(request);
		HttpTransportSE transporte = new HttpTransportSE(URL);
		
		try
		{
		transporte.call(NAMESPACE+SOAP_ACTION+METHOD_NAME, envelope);
		
		if(envelope.bodyIn!=null){
			SoapObject resSoap =(SoapObject)envelope.getResponse();			
			listaDatos = new String[resSoap.getPropertyCount()];		
			for (int i = 0; i < listaDatos.length; i++)
			{
			SoapObject ic = (SoapObject)resSoap.getProperty(i);			
			listaDatos[i]= ic.getProperty(0).toString();
			}
		
		}		
		
		}
		catch (Exception e)
		{
		resul = false;
		}
}catch(Exception e){
			
		}
		return resul;
		}
		
		
		protected void onPostExecute(Boolean result) {
		if (result)
		{
		
		final String[] datos = new String[listaDatos.length];
		for(int i=0; i<listaDatos.length; i++){
			datos[i] = listaDatos[i];
			
		}
		txtNombre.setText(datos[0]);
		txtApellido.setText(datos[1]);
		txtEmail.setText(datos[2]);
		txtNickName.setText(datos[3]);
		txtFoto.setText(datos[4]);	
		}
		else{		
		}
		}
		}
	
	
	
}
