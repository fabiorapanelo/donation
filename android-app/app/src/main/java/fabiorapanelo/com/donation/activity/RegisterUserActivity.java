package fabiorapanelo.com.donation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.model.Credentials;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.services.ServiceListener;
import fabiorapanelo.com.donation.services.UserService;

public class RegisterUserActivity extends AppCompatActivity {

    @Bind(R.id.input_name)
    EditText _nameText;

    @Bind(R.id.input_username)
    EditText _usernameText;

    @Bind(R.id.input_password)
    EditText _passwordText;

    @Bind(R.id.btn_register)
    Button _registerButton;

    protected UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        userService = new UserService();

        ButterKnife.bind(this);

        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(view);
            }
        });
    }

    protected void register(View view){

        String name = _nameText.getText().toString();
        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        //todo
        String type = "person";

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setType(type);

        userService.save(this, new ServiceListener() {
            @Override
            public void onSuccess(Object object) {
                finish();
            }

            @Override
            public void onError(Exception ex) {
                _passwordText.setText("");
                Toast.makeText(RegisterUserActivity.this, "Falha ao cadastrar o usuário!", Toast.LENGTH_LONG).show();
            }

        }, user);
    }
}
