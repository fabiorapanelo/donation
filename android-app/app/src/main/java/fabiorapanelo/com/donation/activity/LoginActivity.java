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
import fabiorapanelo.com.donation.database.UserDao;
import fabiorapanelo.com.donation.model.Credentials;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.services.UserService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

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

    protected UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userService = new UserService();
        userDao = new UserDao(this);

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(view);
            }
        });
    }

    protected void login(View view){

        _loginButton.setEnabled(false);

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(password);

        userService.authenticate(credentials, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {

                if(response.isSuccessful()){
                    User user = response.body();
                    userDao.save(user);

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    _passwordText.setText("");
                    Toast.makeText(LoginActivity.this, "Authenticação Falhou!", Toast.LENGTH_LONG).show();
                }
                _loginButton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                _passwordText.setText("");
                Toast.makeText(LoginActivity.this, "Authenticação Falhou!", Toast.LENGTH_LONG).show();
                _loginButton.setEnabled(true);
            }
        });

    }

    protected void register(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
        startActivity(intent);
    }

}
