package coder.seventy.two.ecommerce

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.title = "RegisterActivity"

        register_button.setOnClickListener {
            val username = register_username.text
            val email = register_email.text
            val pass = register_password.text
        }
        register_cancel.setOnClickListener {
            register_username.text = null
            register_email.text = null
            register_password.text = null
        }
        tvToLogin.setOnClickListener {
            finish()
        }
    }
}
