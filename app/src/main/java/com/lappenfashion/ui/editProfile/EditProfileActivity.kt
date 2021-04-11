package com.lappenfashion.ui.editProfile

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.bumptech.glide.Glide
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainProfile
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
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

class EditProfileActivity : AppCompatActivity() {

    private var imagePath: String = ""
    private var gender: String = ""
    private var selectedUriList: MutableList<Uri> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initData()
        clickListener()
    }

    private fun initData(){
        if(Prefs.getString(Constants.PREF_PROFILE_PICTURE,"") != "") {
            Glide.with(this@EditProfileActivity)
                .load(Prefs.getString(Constants.PREF_PROFILE_PICTURE, "")).into(imgProfile)
        }

        if(Prefs.getString(Constants.PREF_PROFILE_MOBILE_NUMBER,"") != "") {
            edtMobileNumber.setText(Prefs.getString(Constants.PREF_PROFILE_MOBILE_NUMBER,""))
        }

        if(Prefs.getString(Constants.PREF_PROFILE_FULL_NAME,"") != "") {
            edtFullName.setText(Prefs.getString(Constants.PREF_PROFILE_FULL_NAME,""))
        }

        if(Prefs.getString(Constants.PREF_PROFILE_EMAIL,"") != "") {
            edtEmail.setText(Prefs.getString(Constants.PREF_PROFILE_EMAIL,""))
        }

        if(Prefs.getString(Constants.PREF_PROFILE_GENDER,"") != "") {
            if(Prefs.getString(Constants.PREF_PROFILE_GENDER,"")== "Male"){
                txtMale.setBackgroundResource(R.drawable.border_accent)
                txtFemale.setBackgroundResource(R.drawable.border_grey)
                gender = "Male"
            }else{
                gender = "Female"
                txtMale.setBackgroundResource(R.drawable.border_grey)
                txtFemale.setBackgroundResource(R.drawable.border_accent)
            }
        }

        if(Prefs.getString(Constants.PREF_PROFILE_DATE_OF_BIRTH,"")!=""){
            txtBirthDate.setText(Prefs.getString(Constants.PREF_PROFILE_DATE_OF_BIRTH,""))
        }
    }

    private fun clickListener() {
        imgBack.setOnClickListener {
            finish()
        }

        relativeImage.setOnClickListener {
            /*TedImagePicker.with(this@EditProfileActivity)
                .showCameraTile(false)
                .title("Lappen Fashion")
                .max(1, "You can select only 1 images at a time..")
                //.mediaType(MediaType.IMAGE)
                //.scrollIndicatorDateFormat("YYYYMMDD")
                //.buttonGravity(ButtonGravity.BOTTOM)
                //.buttonBackground(R.drawable.btn_sample_done_button)
                //.buttonTextColor(R.color.sample_yellow)
                .errorListener { message -> Log.d("Lappen Fasion", "message: $message") }
                .selectedUri(selectedUriList)
                .startMultiImage { list: List<Uri> -> showMultiImage(list) }*/

            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 1)

        }

        txtSaveDetails.setOnClickListener {
            if (edtMobileNumber.text.toString() == "") {
                edtMobileNumber.error = "Field is required"
            } else if (edtFullName.text.toString() == "") {
                edtFullName.error = "Field is required"
            } else if (edtEmail.text.toString() == "") {
                edtEmail.error = "Field is required"
            } else if (txtBirthDate.text.toString() == "Date Of Birth") {
                txtBirthDate.error = "Field is required"
            } else if (gender == "") {
                Helper.showTost(this@EditProfileActivity, "Please select your gender")
            } else {
                if (NetworkConnection.checkConnection(this)) {
                    Helper.showLoader(this@EditProfileActivity)
                    updateProfile()
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
            try {
                val pickerPopWin = DatePickerPopWin.Builder(
                    this
                ) { year, month, day, dateDesc -> //                            Toast.makeText(getActivity(), dateDesc, Toast.LENGTH_SHORT).show();
                    //                            String myFormat = "MM/dd/yy"; //In which you need put here
                    //                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    txtBirthDate.setText("$year-$month-$day")
                }.textConfirm("CONFIRM") //text of confirm button
                    .textCancel("CANCEL") //text of cancel button
                    .btnTextSize(12) // button text size
                    .viewTextSize(25) // pick view text size
                    .colorCancel(Color.parseColor("#999999")) //color of cancel button
                    .colorConfirm(Color.parseColor("#009900")) //color of confirm button
                    .minYear(1900) //min year in loop
                    .maxYear(2550) // max year in loop
                    //                            .dateChose("2013-11-11") // date chose when init popwindow
                    .build()
                pickerPopWin.showPopWin(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                val selectedImageUri = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                if (selectedImageUri != null) {
                    val cursor: Cursor = contentResolver.query(
                        selectedImageUri,
                        filePathColumn, null, null, null
                    )!!
                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val picturePath = cursor.getString(columnIndex)
                        //                                Toast.makeText(mContext, "Chosen from storage: " + selectedImageUri, Toast.LENGTH_LONG).show();
                        Log.e("Upload Photo", " Gallery Photo Path: $picturePath")
                        imgProfile.setImageURI(picturePath.toUri())
                        imagePath = picturePath
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
            ), edtFullName.text.toString(),
            edtEmail.text.toString(),
            gender,
            txtBirthDate.text.toString(),
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
                }
            }

            override fun onFailure(call: Call<ResponseMainProfile>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }

    private fun showMultiImage(uriList: List<Uri>) {
        this.selectedUriList = uriList as MutableList<Uri>
        imgProfile.setImageURI(selectedUriList[0])
    }
}