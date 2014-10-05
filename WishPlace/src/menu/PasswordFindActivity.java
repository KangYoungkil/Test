package menu;

import json.JSONParser;

import com.example.wishplace.R;
import com.example.wishplace.R.id;
import com.example.wishplace.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordFindActivity extends Activity implements OnClickListener{

	EditText inputPhone;
	EditText inputID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_find);
		
		inputPhone = (EditText)findViewById(R.id.find_pw_phone);
		inputID = (EditText)findViewById(R.id.find_pw_id);
		
		SharedPreferences pref = getSharedPreferences("save_info.xml", 0);
		inputID.setText(pref.getString("wishPlaceEmail", ""));
		
		findViewById(R.id.pw_find_bnt).setOnClickListener(this);
		setPhoneNumber();
		
	}
	
	void setPhoneNumber() {
		try {
			TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String phoneNumber = systemService.getLine1Number();
			if (phoneNumber.length() > 10) {
				phoneNumber = phoneNumber.substring(phoneNumber.length() - 10,
						phoneNumber.length());
			}
			phoneNumber = "0" + phoneNumber;
			inputPhone.setText(phoneNumber);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), 1000).show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.pw_find_bnt:
			if(inputID.toString()=="")
			{
				Toast.makeText(getApplicationContext(), "ID¸¦ ÀÔ·ÂÇØÁÖ¼¼¿ä.", 1000).show();
				inputID.findFocus();
				return;
			}
			if(inputPhone.toString()=="")
			{
				Toast.makeText(getApplicationContext(), "ÀüÈ­¹øÈ£¸¦ ÀÔ·ÂÇØÁÖ¼¼¿ä.", 1000).show();
				inputPhone.findFocus();
				return;
			}
			
			JSONParser json = new JSONParser("findPW","findPassword.php?&email="+inputID.getText().toString()
					+"&phone="+inputPhone.getText().toString());
			json.execute();
			
			try {
				Thread.sleep(500); // ¾²·¹µî
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String password = json.result;
			
			AlertDialog.Builder alert = new AlertDialog.Builder(PasswordFindActivity.this);
			alert.setPositiveButton("È®ÀÎ", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();     //´Ý±â]
			    finish();
			    }
			});
			alert.setTitle("암호 찾기");
			if(password.equals("false"))
				alert.setMessage("ÇØ´ç ÇÏ´Â ¾ÆÀÌµð°¡ Á¸ÀçÇÏÁö ¾Ê½À´Ï´Ù.");
			else
				alert.setMessage("찾으시는 암호는 [ "+password+" ] 입니다.");
			alert.show();
			break;
		}
		
	}
}
