@file:Suppress("UNUSED_EXPRESSION")

package com.example.aplikasiku.customView


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class MyNameText : AppCompatEditText {

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

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length <= 4) {
                    error = "username harus lebih dari 4 karakter"
                    setBackgroundColor(Color.YELLOW) // Set background color to red
                } else {
                    setBackgroundColor(Color.WHITE) // Reset background color when password is valid
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Enter your name"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}
