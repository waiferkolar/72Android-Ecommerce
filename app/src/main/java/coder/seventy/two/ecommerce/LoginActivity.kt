package coder.seventy.two.ecommerce

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import coder.seventy.two.ecommerce.libby.H.Companion.USER_TOKEN
import coder.seventy.two.ecommerce.libby.H.Companion.l
import coder.seventy.two.ecommerce.modals.Token
import coder.seventy.two.ecommerce.services.ServiceBuilder
import coder.seventy.two.ecommerce.services.WebService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Login"
        tvLoginAds.isSelected = true

        btnLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val pass = etLoginPass.text.toString()


            loginAUser(email, pass)
        }
        btnLoginCancel.setOnClickListener {
            etLoginEmail.text = null
            etLoginPass.text = null
        }
        tvToRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginAUser(email: String, pass: String) {
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseLogin: Call<Token> = services.loginUser(email, pass)

        responseLogin.enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                l(t.message!!)
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                val token: Token = response.body()!!
                l(token.token)
                USER_TOKEN = token.token
                val intent = Intent(this@LoginActivity, CategoryActivity::class.java)
                startActivity(intent)
            }
        })
    }
}
