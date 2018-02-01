package fabiorapanelo.com.donation.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.apache.commons.lang3.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.barcode.BarcodeCaptureActivity;
import fabiorapanelo.com.donation.model.Ticket;
import fabiorapanelo.com.donation.model.UserInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fabio on 24/10/2017.
 */

public class AddTicketFragment extends BaseFragment {

    public static final int REQUEST_CODE_ADD_TICKET = 9001;

    @Bind(R.id.txt_user_balance)
    protected TextView txtUserBalance;

    @Bind(R.id.btn_scan_ticket)
    protected Button btnScanTicket;

    @Bind(R.id.txt_ticket_quantity)
    protected TextView txtTicketQuantity;

    @Bind(R.id.txt_ticket_id)
    protected TextView txtTicketId;

    @Bind(R.id.btn_cancel)
    protected Button btnCancel;

    @Bind(R.id.btn_add_ticket)
    protected Button btnAddTicket;

    @Bind(R.id.progress_bar)
    protected ProgressBar progressBar;

    public static AddTicketFragment newInstance() {
        AddTicketFragment fragment = new AddTicketFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_ticket, container, false);

        ButterKnife.bind(this, view);

        btnScanTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanTicket(view);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTicket(view);
            }
        });
        btnAddTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTicket(view);
            }
        });

        this.getBalance();

        return view;
    }

    protected void scanTicket(View view){
        Intent intent = new Intent(this.getActivity(), BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
        startActivityForResult(intent, REQUEST_CODE_ADD_TICKET);
    }

    protected void cancelTicket(View view){
        btnScanTicket.setVisibility(View.VISIBLE);
        txtTicketQuantity.setText("");
        txtTicketId.setText("");
    }

    protected void addTicket(View view){
        String ticketId = txtTicketId.getText().toString();

        if(StringUtils.isEmpty(ticketId)){
            Toast.makeText(AddTicketFragment.this.getActivity(), "Cupom inválido", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        ticketService.consume(ticketId, user.getId().toString(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBar.setVisibility(View.INVISIBLE);

                if(response.isSuccessful()){
                    btnScanTicket.setVisibility(View.VISIBLE);
                    AddTicketFragment.this.getBalance();
                    txtTicketQuantity.setText("");
                    txtTicketId.setText("");
                    Toast.makeText(AddTicketFragment.this.getActivity(), "Cupom adicionado com sucesso", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddTicketFragment.this.getActivity(), "Cupom inválido", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddTicketFragment.this.getActivity(), "Cupom inválido", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_TICKET && resultCode == CommonStatusCodes.SUCCESS && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

            btnScanTicket.setVisibility(View.GONE);
            txtTicketId.setText(barcode.displayValue);
            progressBar.setVisibility(View.VISIBLE);

            ticketService.find(barcode.displayValue, new Callback<Ticket>() {
                @Override
                public void onResponse(Call<Ticket> call, Response<Ticket> response) {

                    if(response.isSuccessful()){
                        Ticket ticket = response.body();

                        if("NEW".equals(ticket.getStatus())){
                            txtTicketQuantity.setText("R$ " + ticket.getQuantity() + ",00");
                        } else {
                            txtTicketQuantity.setText("Cupom inválido");
                        }

                    } else {
                        txtTicketQuantity.setText("Cupom inválido");
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<Ticket> call, Throwable t) {
                    txtTicketQuantity.setText("Cupom inválido");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void getBalance(){

        progressBar.setVisibility(View.VISIBLE);

        userService.getBalance(user, new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.isSuccessful()){
                    try {
                        Integer balance = response.body().getBalance();
                        txtUserBalance.setText(getResources().getQuantityString(R.plurals.text_user_balance, balance, balance));

                    } catch (Exception e) {
                        txtUserBalance.setText(getResources().getString(R.string.text_user_balance_unavailable));
                    }
                } else {
                    txtUserBalance.setText(getResources().getString(R.string.text_user_balance_unavailable));
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                txtUserBalance.setText(getResources().getString(R.string.text_user_balance_unavailable));
            }
        });
    }
}
