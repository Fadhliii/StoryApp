package com.example.gagalmuluyaallah.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.gagalmuluyaallah.R

class MyButton2 : AppCompatButton {
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        textSize = 18f
        gravity = Gravity.CENTER
        text = if (isEnabled) "Submit" else "Enter your data"
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground =
                ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }

    fun setValidation(emailEditText: EditText, passwordEditText: EditText) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Tidak melakukan
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Tidak melakukan
            }

            override fun afterTextChanged(s: Editable) {
                // Cek apakah format email benar dan password memiliki minimal 6 karakter
                val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()
                val isPasswordValid = passwordEditText.text.toString().length >= 6

                // Aktifkan atau nonaktifkan tombol berdasarkan validitas email dan password
                isEnabled = isEmailValid && isPasswordValid

                // Ubah warna latar belakang tombol berdasarkan validitas email dan password
                background = if (isEmailValid && isPasswordValid) enabledBackground else disabledBackground
            }
        }

        // Pasang TextWatcher ke EditText
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
    }
}