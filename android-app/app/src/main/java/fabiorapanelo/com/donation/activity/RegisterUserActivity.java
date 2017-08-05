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
import fabiorapanelo.com.donation.services.ServiceListener;
import fabiorapanelo.com.donation.services.UserService;

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

        userService = new UserService();

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

        userService.findByUsername(username, new ServiceListener() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(RegisterUserActivity.this, "Usuário já existe!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable t) {
                //Return 404 - Not found if the user doesn't exists
                register();
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

        userService.save(user, new ServiceListener() {
            @Override
            public void onSuccess(Object object) {
                finish();
            }

            @Override
            public void onError(Throwable t) {
                _passwordText.setText("");
                Toast.makeText(RegisterUserActivity.this, "Falha ao cadastrar o usuário!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
