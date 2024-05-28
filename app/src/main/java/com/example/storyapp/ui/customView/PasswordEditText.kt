package com.example.storyapp.ui.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val passwordLayout = parent.parent as TextInputLayout
                if (s.isNullOrEmpty()) {
                    passwordLayout.error = context.getString(R.string.password_required)
                } else if (s.length < 8) {
                    passwordLayout.error = context.getString(R.string.password_8_character)
                } else {
                    passwordLayout.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}