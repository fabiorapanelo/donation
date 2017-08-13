package fabiorapanelo.com.donation.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.services.UserService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterUserActivity extends BaseActivity {

    @Bind(R.id.input_name)
    EditText _nameText;

    @Bind(R.id.input_username)
    EditText _usernameText;

    @Bind(R.id.input_password)
    EditText _passwordText;

    @Bind(R.id.input_receive_donations)
    CheckBox _receiveDonationsCheckbox;

    @Bind(R.id.btn_register)
    Button _registerButton;

    protected UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        ButterKnife.bind(this);

        this.setupToolbar();

        userService = UserService.getInstance();

        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(isValid()){
                boolean receiveDonations = _receiveDonationsCheckbox.isChecked();
                if(receiveDonations){
                    displayDialog();
                } else {
                    checkUsernameAndRegister();
                }
            } else {
                Toast.makeText(RegisterUserActivity.this, "Preencha os campos acima", Toast.LENGTH_SHORT).show();
            }

            }
        });
    }

    protected boolean isValid(){

        String name = _nameText.getText().toString();
        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        return (StringUtils.isNotEmpty(name) &&
                StringUtils.isNotEmpty(username) &&
                StringUtils.isNotEmpty(password));

    }

    protected void displayDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message_receive_donations).setTitle(R.string.title_receive_donations);
        builder.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                checkUsernameAndRegister();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                _receiveDonationsCheckbox.setChecked(false);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void checkUsernameAndRegister(){

        String username = _usernameText.getText().toString();

        userService.findByUsername(username, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RegisterUserActivity.this, "Usuário já existe!", Toast.LENGTH_LONG).show();
                } else {
                    //Return 404 - Not found if the user doesn't exists
                    register();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //TODO: Handle this scenario
            }
        });

    }

    protected void register(){

        String name = _nameText.getText().toString();
        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();
        boolean receiveDonations = _receiveDonationsCheckbox.isChecked();

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setReceiveDonations(receiveDonations);

        userService.save(user, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    finish();
                } else {
                    _passwordText.setText("");
                    Toast.makeText(RegisterUserActivity.this, "Falha ao cadastrar o usuário!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                _passwordText.setText("");
                Toast.makeText(RegisterUserActivity.this, "Falha ao cadastrar o usuário!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
