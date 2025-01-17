package com.example.buildingmanagement

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.buildingmanagement.HttpApi.HttpApi
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_landing.*
import org.json.JSONArray
import android.view.MotionEvent
import android.widget.TextView


val displayMetrics = DisplayMetrics()

//val height = displayMetrics.heightPixels / displayMetrics.density //手機真實高度
//val width = displayMetrics.widthPixels / displayMetrics.density //手機真實寬度

//val window: Window = dialog.getWindow()

class loginActivity : AppCompatActivity() {
    private val TAG = "loginActivity"

    var heightPixels = 0
    var widthPixels = 0

    val httpApi = HttpApi()

    lateinit var adapter: ArrayAdapter<String>
    lateinit var listView: ListView
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var dialog: AlertDialog

//    val array = arrayListOf("社區1", "社區2", "社區3", "社區4", "社區5", "社區6", "社區7", "社區8", "社區9", "社區10",
//        "社區11", "社區12", "社區13", "社區14", "社區15" )

    var array: ArrayList<String> = arrayListOf()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.hasExtra("reload")) {
            setTheme(R.style.Theme_NoActionBar)
        }
        super.onCreate(savedInstanceState)
        val handler = Handler()
        if (!intent.hasExtra("reload")) {
            setContentView(R.layout.login_loading)
            handler.postDelayed({
                initLandingPage()

            }, 3000)
        } else {
            setContentView(R.layout.activity_login)
            // set status bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.0
                val window = window
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // 確認取消半透明設置。
                window.decorView.systemUiVisibility =
                    (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 全螢幕顯示，status bar 不隱藏，activity 上方 layout 會被 status bar 覆蓋。
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE) // 配合其他 flag 使用，防止 system bar 改變後 layout 的變動。
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) // 跟系統表示要渲染 system bar 背景。
                window.statusBarColor = Color.WHITE
            }

            landingedit.addTextChangedListener(object : TextWatcher {
                @SuppressLint("ResourceAsColor")
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    landingbutton3.setBackgroundResource(R.drawable.enable_btn)
                    landingbutton3.setTextColor(resources.getColor(R.color.white))
                    landingedit.setBackgroundResource(R.drawable.edit_border)
                    Log.d(TAG, "beforeTextChanged: ")
                }

                @SuppressLint("ResourceAsColor")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (landingedit.text.isEmpty()){
                        landingbutton3.setBackgroundResource(R.drawable.shape_circle)
                        landingbutton3.setTextColor(resources.getColor(R.color.Choosecommunity))
                        Log.d(TAG, "onTextChanged: ")
                    }
                }

                @SuppressLint("ResourceAsColor")
                override fun afterTextChanged(s: Editable?) {
                    Log.d(TAG, "afterTextChanged: ")
                }

            })
        }


        Log.d(TAG, "onCreate: ${savedInstanceState}")
        Log.d(TAG, "onCreate: hasExtra reload ${intent.hasExtra("reload")}")


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val decor = window.decorView
//            if (shouldChangeStatusBarTintToDark) {
//                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            } else {
//                // We want to change tint color to white again.
//                // You can also record the flags in advance so that you can turn UI back completely if
//                // you have set other flags before, such as translucent or full screen.
//                decor.systemUiVisibility = 0
//            }
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            window.getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // 表示我們的 UI 是 LIGHT 的 style，icon 就會呈現深色系。
//            )
//        }

        getDeviceWH() // initialize get device width and height

        httpApi.GetAllProjectName {
            onSuccess {
                Log.d(TAG, "GetAllProjectName onSuccess: ${it}")
                val list = JSONArray(it as String)
                for (i in 0 until list.length()) {
                    Log.d(TAG, "item -> ${list[i]}")
                    array.add(list[i] as String)
                }
            }
        }

//        httpApi.BindUserData("1", "2", "3") {
//             onSuccess {
//                 it as BindUserDat
//                 it.address
//             }
//        }
    }


    fun initLandingPage() {
        setContentView(R.layout.login_landing)
        imageview.layoutParams.height = (heightPixels * 0.669).toInt()
        (textview.layoutParams as RelativeLayout.LayoutParams).apply {
            topMargin = (heightPixels * 0.041).toInt()
            bottomMargin = (heightPixels * 0.016).toInt()
        }
        (textview1.layoutParams as RelativeLayout.LayoutParams).apply {
            topMargin = (heightPixels * 0.041).toInt()
            bottomMargin = (heightPixels * 0.016).toInt()
        }
        (textview2.layoutParams as RelativeLayout.LayoutParams).apply {
            bottomMargin = (heightPixels * 0.063).toInt()
        }
        (landingbutton.layoutParams as RelativeLayout.LayoutParams).apply {
            height = (heightPixels * 0.069).toInt()
            bottomMargin = (heightPixels * 0.056).toInt()
        }
    }

    fun loginbtn(view: View){
        if (!intent.hasExtra("reload")) {
            intent.putExtra("reload", true)
            finish()
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_login)
            // set status bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.0
                val window = window
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // 確認取消半透明設置。
                window.decorView.systemUiVisibility =
                    (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 全螢幕顯示，status bar 不隱藏，activity 上方 layout 會被 status bar 覆蓋。
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE) // 配合其他 flag 使用，防止 system bar 改變後 layout 的變動。
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) // 跟系統表示要渲染 system bar 背景。
                window.statusBarColor = Color.WHITE
            }

            landingedit.addTextChangedListener(object : TextWatcher {
                @SuppressLint("ResourceAsColor")
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    landingbutton3.setBackgroundResource(R.drawable.enable_btn)
                    landingbutton3.setTextColor(resources.getColor(R.color.white))
                    landingedit.setBackgroundResource(R.drawable.edit_border)
                    Log.d(TAG, "beforeTextChanged: ")
                }

                @SuppressLint("ResourceAsColor")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (landingedit.text.isEmpty()){
                        landingbutton3.setBackgroundResource(R.drawable.shape_circle)
                        landingbutton3.setTextColor(resources.getColor(R.color.Choosecommunity))
                        Log.d(TAG, "onTextChanged: ")
                    }
                }

                @SuppressLint("ResourceAsColor")
                override fun afterTextChanged(s: Editable?) {
                    Log.d(TAG, "afterTextChanged: ")
                }

            })
        }


    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    fun openDialog(view: View) {
        alertDialog = AlertDialog.Builder(this)
        val rowList: View = layoutInflater.inflate(R.layout.my_dialog, container,false)
        listView = rowList.findViewById(R.id.listView)

        adapter = ArrayAdapter(this, R.layout.listview_item, array)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            listView.setItemChecked(position, true)
            spinner.text = array[position]
            spinner.setTextColor(R.color.forgotresidentcode)
            view.setBackgroundResource(R.drawable.list_view)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(resources.getString(R.color.loading)))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
        }


        // title
        var title = TextView(this)
        title.text = "選擇社區"
        title.setTextColor(resources.getColor(R.color.forgotresidentcode))
        title.textSize = 20F

        alertDialog.setCustomTitle(title)
        alertDialog.setPositiveButton("確定") { dialog, which ->  }
        alertDialog.setNegativeButton("取消") { dialog: DialogInterface?, which: Int -> closeDialog() }
        adapter.notifyDataSetChanged()
        alertDialog.setView(rowList)
        dialog = alertDialog.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
        dialog.show()

        dialog.window?.setLayout((widthPixels*0.8).toInt(), (heightPixels*0.674).toInt())
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(resources.getString(R.color.Choosecommunity)))     //確定
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor(resources.getString(R.color.loading)))     //取消
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    fun closeDialog() {
        spinner.text = resources.getString(R.string.Choosecommunity)
//        spinner.setTextColor(resources.getColor(R.color.Choosecommunity))
        spinner.setTextColor(Color.parseColor(resources.getString(R.color.Choosecommunity)))
        dialog.dismiss()
    }

    // 住戶代碼不存在
    @SuppressLint("ResourceType")
    fun userCodeErrorDialog() {
        alertDialog = AlertDialog.Builder(this, R.style.MyDialogStyle)
            .setPositiveButton("好", null)   // listener argument can write some action

        // title
        var title = TextView(this)
        title.text = "住戶代碼不存在"
        title.setTextColor(R.color.forgotresidentcode)
        title.textSize = 16F

        alertDialog.setCustomTitle(title)

        //message
        var message = SpannableString("請確認您輸入的 iPad 住戶代碼正確無誤，再試一次吧！")
            message.setSpan(ForegroundColorSpan(R.color.Choosecommunity),0,message.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        alertDialog.setMessage(message)

        dialog = alertDialog.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
        dialog.show()
        dialog.window?.setLayout((widthPixels*0.778).toInt(), (heightPixels*0.281).toInt())
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(resources.getString(R.color.loading)))

    }

    //住戶代碼已被其他裝置綁定
    @SuppressLint("ResourceType")
    fun userCodeUsing(){
        alertDialog = AlertDialog.Builder(this)
            .setPositiveButton("好", null)   // listener argument can write some action

        // title
        var title = TextView(this)
        title.text = "住戶代碼已被其他裝置綁定"
        title.setTextColor(R.color.forgotresidentcode)
        title.textSize = 16F

        alertDialog.setCustomTitle(title)

        //message
        var message = SpannableString("若您需要更換新的裝置，請洽詢社區管理中心協助解除綁定，並重新登入即可繼續此服務。")
        message.setSpan(ForegroundColorSpan(R.color.Choosecommunity),0,message.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        alertDialog.setMessage(message)

        dialog = alertDialog.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog)
        dialog.show()
        dialog.window?.setLayout((widthPixels*0.778).toInt(), (heightPixels*0.313).toInt())
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor(resources.getString(R.color.loading)))

    }


    fun forgot(view: View){
        setContentView(R.layout.forgotresidentcode)
    }

    fun privacypolicy(view: View){
        setContentView(R.layout.privacypolicy)
        val handler = Handler()
        handler.postDelayed({setContentView(R.layout.privacypolicy1)}, 1000)
    }

    // login and start MainActivity
    @SuppressLint("ResourceAsColor")
    fun landingClick(view: View) {
        val button = findViewById<Button>(R.id.landingbutton)

        if (landingedit.text.isNullOrEmpty()) {
            landingedit.setBackgroundResource(R.drawable.error_border)
            userCodeErrorDialog()
        } else {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, intent)
            }
            startActivity(intent)
        }
    }

    // get device width and height
    fun getDeviceWH() {
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        widthPixels = displayMetrics.widthPixels
        heightPixels = displayMetrics.heightPixels

//        Log.d(TAG, "getDeviceWH: width " + displayMetrics.widthPixels)
//        Log.d(TAG, "getDeviceWH: height " + displayMetrics.heightPixels)
    }

    fun backLoginLanding(view: View) {
        initLandingPage()
    }

    fun close_view(view: View){
        setContentView(R.layout.activity_login)
    }

    //點擊空白處關閉鍵盤
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (this@loginActivity.getCurrentFocus() != null) {
                if (this@loginActivity.getCurrentFocus()!!.getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(
                        this@loginActivity.getCurrentFocus()!!.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                }
            }
        }
        return super.onTouchEvent(event)
    }

}
















