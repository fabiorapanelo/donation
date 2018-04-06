package fabiorapanelo.com.donation.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import fabiorapanelo.com.donation.R
import fabiorapanelo.com.donation.model.Ticket
import fabiorapanelo.com.donation.utils.PermissionUtils
import kotlinx.android.synthetic.main.fragment_generate_ticket.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class GenerateTicketFragment : BaseFragment() {

    var bitmapQRCode: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_generate_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn_generate_ticket.setOnClickListener { view ->
            val quantity = Integer.valueOf(edit_text_quantity.text.toString())
            val ticket = Ticket()
            ticket.status = "NEW"
            ticket.quantity = quantity

            ticketService.create(ticket, object : Callback<Ticket> {
                override fun onResponse(call: Call<Ticket>, response: Response<Ticket>) {

                    if(response.isSuccessful){

                        image_view_qr_code as ImageView
                        btn_share_qr_code.visibility = View.INVISIBLE
                        try {
                            bitmapQRCode = encodeAsBitmap(response.body()!!.id, 300)
                            if (bitmapQRCode != null) {
                                image_view_qr_code.setImageBitmap(bitmapQRCode)
                                btn_share_qr_code.visibility = View.VISIBLE
                            }
                        } catch (e: WriterException) {
                        }

                        btn_share_qr_code.setOnClickListener { _ ->
                            shareQrCode();
                        }

                    }

                }

                override fun onFailure(call: Call<Ticket>, t: Throwable) {

                }
            });
        }

    }

    @Throws(WriterException::class)
    private fun encodeAsBitmap(str: String, size: Int): Bitmap? {
        val result: BitMatrix
        try {
            result = MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, size, size, null)
        } catch (iae: IllegalArgumentException) {
            return null
        }

        val w = result.width
        val h = result.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (result.get(x, y)) BLACK else WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, size, 0, 0, w, h)
        return bitmap
    }

    private fun saveImageIntoDisk(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "QR-Code", null)
        return Uri.parse(path)
    }

    private fun shareQrCode() {

        val activity = this.activity as AppCompatActivity
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, REQUEST_CODE_SHARE_QR_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE, false)
        } else {

            val i = Intent(Intent.ACTION_SEND)
            i.type = "image/*"
            val stream = ByteArrayOutputStream()

            var uri = saveImageIntoDisk(activity, bitmapQRCode!!)

            i.putExtra(Intent.EXTRA_STREAM, uri)
            try {
                startActivity(Intent.createChooser(i, "Doe+ Cupom"))
            } catch (ex: android.content.ActivityNotFoundException) {
                ex.printStackTrace()
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode != REQUEST_CODE_SHARE_QR_CODE) {
            return
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            shareQrCode()
        } else {
            showMissingPermissionError()
        }
    }

    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(fragmentManager, "dialog")
    }

    companion object Factory {

        val REQUEST_CODE_SHARE_QR_CODE = 1

        fun newInstance(): GenerateTicketFragment {
            return GenerateTicketFragment()
        }
    }

}
