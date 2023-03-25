package cn.edu.hznu.broadcastbestpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceDataStore;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.prefs.Preferences;

public class LoginActivity extends BaseActivity {

    private EditText accountEdit;

    private EditText passwordEdit;
    private SharedPreferences pref;
    private Button login;
    private CheckBox rememberPass;
    private SharedPreferences.Editor editor;
    private String account;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        accountEdit =(EditText) findViewById(R.id.account);
        passwordEdit =(EditText) findViewById(R.id.password);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        login = (Button) findViewById(R.id.login);
        rememberPass=(CheckBox)findViewById(R.id.remember_pass);
        boolean isRemember=pref.getBoolean(  "remember_password",false);
        if(isRemember){
            account=pref.getString("account","");
            password=pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();
                if(account.equals("admin")&& password.equals("123456")){
                    editor=pref.edit();
                    if(rememberPass.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",account);
                        editor.putString("password",password);
                    }else {
                        editor.clear();
                    }
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"用户名或密码错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}