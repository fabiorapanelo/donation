package fabiorapanelo.com.donation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.model.Credentials;
import fabiorapanelo.com.donation.services.ServiceListener;
import fabiorapanelo.com.donation.services.UserService;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.input_username)
    EditText _usernameText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    protected UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userService = new UserService();

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });
    }

    protected void login(View view){

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(password);

        userService.authenticate(this, new ServiceListener() {
            @Override
            public void onSuccess(Object object) {
                Intent intent = new Intent(LoginActivity.this, ChooseUserTypeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(Exception ex) {
                _passwordText.setText("");
                Toast.makeText(LoginActivity.this, "Authenticação Falhou!", Toast.LENGTH_LONG).show();
            }
        }, credentials);

    }


}
