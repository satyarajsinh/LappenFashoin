package com.lappenfashion.ui.editProfile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainProfile
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.edtMobileNumber
import kotlinx.android.synthetic.main.activity_edit_profile.imgBack
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.nav_header_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class EditProfileActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener{

    private var selectedDate: String = ""
    private var picturePath: String? = ""
    private var imagePath: String = ""
    private var gender: String = ""
    private var selectedUriList: MutableList<Uri> = arrayListOf()
    var simpleDateFormat: SimpleDateFormat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initData()
        clickListener()
    }

    private fun initData(){
        if(Prefs.getString(Constants.PREF_PROFILE_PICTURE, "") != "") {
            Glide.with(this@EditProfileActivity)
                .load(Prefs.getString(Constants.PREF_PROFILE_PICTURE, "")).into(imgProfile)
        }

        if(Prefs.getString(Constants.PREF_PROFILE_MOBILE_NUMBER, "") != "") {
            edtMobileNumber.setText(Prefs.getString(Constants.PREF_PROFILE_MOBILE_NUMBER, ""))
        }

        if(Prefs.getString(Constants.PREF_PROFILE_FULL_NAME, "") != "") {
            edtFullName.setText(Prefs.getString(Constants.PREF_PROFILE_FULL_NAME, ""))
        }

        if(Prefs.getString(Constants.PREF_PROFILE_EMAIL, "") != "") {
            edtEmail.setText(Prefs.getString(Constants.PREF_PROFILE_EMAIL, ""))
        }

        if(Prefs.getString(Constants.PREF_PROFILE_GENDER, "") != "") {
            if(Prefs.getString(Constants.PREF_PROFILE_GENDER, "")== "Male"){
                txtMale.setBackgroundResource(R.drawable.border_accent)
                txtFemale.setBackgroundResource(R.drawable.border_grey)
                gender = "Male"
            }else{
                gender = "Female"
                txtMale.setBackgroundResource(R.drawable.border_grey)
                txtFemale.setBackgroundResource(R.drawable.border_accent)
            }
        }

        if(Prefs.getString(Constants.PREF_PROFILE_DATE_OF_BIRTH, "")!=""){
            txtBirthDate.setText(Prefs.getString(Constants.PREF_PROFILE_DATE_OF_BIRTH, ""))
        }
    }

    private fun clickListener() {
        imgBack.setOnClickListener {
            finish()
        }

        relativeImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this@EditProfileActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this@EditProfileActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(
                    this@EditProfileActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    101
                )
            } else {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            }
        }

        txtSaveDetails.setOnClickListener {
            if (edtMobileNumber.text.toString() == "") {
                edtMobileNumber.error = "Field is required"
            } else if (edtFullName.text.toString() == "" ) {
                edtFullName.error = "Field is required"
            } else if (edtEmail.text.toString() == "" ) {
                edtEmail.error = "Field is required"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches()) {
                edtEmail.error = "Enter valid email address"
            }else if (txtBirthDate.text.toString() == "Date Of Birth") {
                txtBirthDate.error = "Field is required"
            } else if (gender == "") {
                Helper.showTost(this@EditProfileActivity, "Please select your gender")
            } else {
                if (NetworkConnection.checkConnection(this)) {
                    Helper.showLoader(this@EditProfileActivity)
                    if(imagePath == "" && Prefs.getString(Constants.PREF_PROFILE_PICTURE, "") == ""){
                        updateProfileWithoutPhoto()
                    }else{
                        updateProfile()
                    }
                } else {
                    Helper.showTost(this, getString(R.string.no_internet))
                }

            }
        }

        txtFemale.setOnClickListener {
            txtFemale.setBackgroundResource(R.drawable.border_accent)
            txtMale.setBackgroundResource(R.drawable.border_grey)
            gender = "Female"
        }

        txtMale.setOnClickListener {
            txtFemale.setBackgroundResource(R.drawable.border_grey)
            txtMale.setBackgroundResource(R.drawable.border_accent)
            gender = "Male"
        }

        txtBirthDate.setOnClickListener {
            var year : Int= Calendar.getInstance().get(Calendar.YEAR);
            var day : Int= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            var month : Int= Calendar.getInstance().get(Calendar.MONTH);
            showDate(year, month, day, R.style.NumberPickerStyle);
        }
    }

    private fun updateProfileWithoutPhoto() {
        var api = MyApi(this)
        val requestCall: Call<ResponseMainProfile> = api.addProfileWithoutPhoto(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN,
                ""
            ),
            RequestBody.create(MediaType.parse("text/plain"), edtFullName.text.toString()),
            RequestBody.create(MediaType.parse("text/plain"), edtEmail.text.toString()),
            RequestBody.create(MediaType.parse("text/plain"), gender),
            RequestBody.create(MediaType.parse("text/plain"), selectedDate),
        )
     /*   val requestCall: Call<ResponseMainProfile> = api.addProfileWithoutPhoto(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN,
                ""
            ),
            edtFullName.text.toString(),
            edtEmail.text.toString(),
            gender,
            selectedDate,
        )*/
        requestCall.enqueue(object : Callback<ResponseMainProfile> {
            override fun onResponse(
                call: Call<ResponseMainProfile>,
                response: Response<ResponseMainProfile>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.result == true) {

                    Helper.showTost(this@EditProfileActivity, response.body()!!.message!!)
                    Prefs.putString(
                        Constants.PREF_PROFILE_PICTURE,
                        response.body()!!.payload?.image
                    )
                    Prefs.putString(
                        Constants.PREF_PROFILE_FULL_NAME,
                        response.body()!!.payload?.name
                    )
                    Prefs.putString(
                        Constants.PREF_PROFILE_MOBILE_NUMBER,
                        response.body()!!.payload?.mobileNumber
                    )
                    Prefs.putString(Constants.PREF_PROFILE_EMAIL, response.body()!!.payload?.email)
                    Prefs.putString(
                        Constants.PREF_PROFILE_DATE_OF_BIRTH,
                        response.body()!!.payload?.birthDate
                    )
                    Prefs.putString(
                        Constants.PREF_PROFILE_GENDER,
                        response.body()!!.payload?.gender
                    )
                    finish()
                }
            }

            override fun onFailure(call: Call<ResponseMainProfile>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@EditProfileActivity,t.message)
            }

        })
    }

    private fun showDate(year: Int, monthOfYear: Int, dayOfMonth: Int, datePickerSpinner: Int) {
        SpinnerDatePickerDialogBuilder()
            .context(this@EditProfileActivity)
            .callback(this@EditProfileActivity)
            .spinnerTheme(datePickerSpinner)
            .defaultDate(year, monthOfYear, dayOfMonth)
            .build()
            .show()

      /*  val calendar: Calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
        txtBirthDate.text = simpleDateFormat!!.format(calendar.getTime())*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                val selectedImageUri = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                if (selectedImageUri != null) {
                    val cursor: Cursor? = getContentResolver().query(
                        selectedImageUri,
                        filePathColumn, null, null, null
                    )
                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        imagePath = cursor.getString(columnIndex)
                        imgProfile.setImageURI(selectedImageUri)
//                        uploadPhotoMultipart(picturePath)
                        cursor.close()
                    }
                }
            }
        }
    }

    private fun updateProfile() {
        var file = File(imagePath)

        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        var api = MyApi(this)
        val requestCall: Call<ResponseMainProfile> = api.addProfile(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN,
                ""
            ),
            RequestBody.create(MediaType.parse("text/plain"), edtFullName.text.toString()),
            RequestBody.create(MediaType.parse("text/plain"), edtEmail.text.toString()),
            RequestBody.create(MediaType.parse("text/plain"), gender),
            RequestBody.create(MediaType.parse("text/plain"), txtBirthDate.text.toString()),
            body
        )

        requestCall.enqueue(object : Callback<ResponseMainProfile> {
            override fun onResponse(
                call: Call<ResponseMainProfile>,
                response: Response<ResponseMainProfile>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.result == true) {

                    Helper.showTost(this@EditProfileActivity, response.body()!!.message!!)
                    Prefs.putString(
                        Constants.PREF_PROFILE_PICTURE,
                        response.body()!!.payload?.image
                    )
                    Prefs.putString(
                        Constants.PREF_PROFILE_FULL_NAME,
                        response.body()!!.payload?.name
                    )
                    Prefs.putString(
                        Constants.PREF_PROFILE_MOBILE_NUMBER,
                        response.body()!!.payload?.mobileNumber
                    )
                    Prefs.putString(Constants.PREF_PROFILE_EMAIL, response.body()!!.payload?.email)
                    Prefs.putString(
                        Constants.PREF_PROFILE_DATE_OF_BIRTH,
                        response.body()!!.payload?.birthDate
                    )
                    Prefs.putString(
                        Constants.PREF_PROFILE_GENDER,
                        response.body()!!.payload?.gender
                    )
                    finish()
                }
            }

            override fun onFailure(call: Call<ResponseMainProfile>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@EditProfileActivity,t.message)
            }

        })
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        selectedDate = year.toString()+"-"+(monthOfYear+1).toString()+"-"+dayOfMonth.toString()
        var month = "";
        var dateOne = ""

        if((monthOfYear+1).toString().length == 1){
            month = "0"+(monthOfYear+1)
        }else{
            month = (monthOfYear+1).toString()
        }

        if(dayOfMonth.toString().length == 1){
            dateOne = "0"+dayOfMonth
        }else{
            dateOne = dayOfMonth.toString()
        }


        txtBirthDate.text = dateOne+"/"+month+"/"+year.toString()
    }
}