package fabiorapanelo.com.donation.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.barcode.BarcodeCaptureActivity;

/**
 * Created by fabio on 24/10/2017.
 */

public class AddFragment extends BaseFragment {

    public static final int REQUEST_CODE_ADD_TICKET = 9001;

    @Bind(R.id.btn_add_ticket)
    protected Button btnAddTicket;

    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        ButterKnife.bind(this, view);

        btnAddTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTicket(view);
            }
        });

        return view;
    }

    protected void addTicket(View view){
        Intent intent = new Intent(this.getActivity(), BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
        startActivityForResult(intent, REQUEST_CODE_ADD_TICKET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_TICKET && resultCode == CommonStatusCodes.SUCCESS && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
            Toast.makeText(this.getActivity(), barcode.displayValue, Toast.LENGTH_LONG).show();
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
}
