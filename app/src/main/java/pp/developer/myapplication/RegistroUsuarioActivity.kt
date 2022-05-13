package pp.developer.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pp.developer.myapplication.models.Respuesta
import pp.developer.myapplication.models.Usuario
import pp.developer.myapplication.services.APIServices
import pp.developer.myapplication.services.GetRetrofit
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.concurrent.schedule


class RegistroUsuarioActivity : AppCompatActivity() {
    private val USER_ID:String = "id"
    private val USER_USER:String = "usuario"
    private lateinit var retroApi: Retrofit
    //Valida usuario autenticado
    private  lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        val usr: FirebaseUser? = auth.currentUser
        if(usr != null){
            //Navigation.findNavController()
        }else{
            Message("usuario no valido. Favor de ingresar.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)
        retroApi = GetRetrofit().getRetrofit()
        auth = FirebaseAuth.getInstance()
        val spinn:Spinner = findViewById(R.id.spinnerSexo)
        val spinns = resources.getStringArray(R.array.a_sexo)
        spinn.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,spinns)


        Message("Valida: Email")
        Message("Valida: Password")
    }

    fun btn_Guarda_Click(view: View) {
        var status:Boolean = false

        val name:Boolean= !findViewById<EditText>(R.id.eTxtNombre).text.isNullOrEmpty()
        val mail: Boolean = validateEmail(findViewById<EditText>(R.id.eTxtMail).text.toString())
        val password: Boolean = validatePassword(findViewById<EditText>(R.id.eTxtContrasena).text.toString())
        val user:Boolean= !findViewById<EditText>(R.id.eTxtusuario).text.isNullOrEmpty()
        if(mail && password && user && name){
            //Toast.makeText(this,"Registrando usuario...", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                var email = findViewById<EditText>(R.id.eTxtMail).text.toString()
                var pswd = findViewById<EditText>(R.id.eTxtContrasena).text.toString()
                runOnUiThread {
                    //validateUsuario("validateUsuario")
                    auth.createUserWithEmailAndPassword (email.trim(),pswd.trim()).addOnCompleteListener {
                        if(it.isSuccessful){
                            Error("Registro exitoso. Redirigiendo...")
                            Thread.sleep(3000)
                            startActivity(Intent(this@RegistroUsuarioActivity,Login::class.java))
                        }//else Message("Datos incorrectos")
                    }.addOnFailureListener {
                        Message(it.localizedMessage)
                    }
                }
            }
//            if(status){
//                Error("Registro exitoso. Redirigiendo...")
//                val intent = Intent(this@RegistroUsuarioActivity,Login::class.java)
//                startActivity(intent)
//            }
        }else{
            Message("Verifique los datos solicitados.")
        }
    }

    fun validateEmail(email: String): Boolean {
        val pattern: String = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$"
        val regExp: Regex = Regex(pattern)
        return regExp.matches(email)
    }
    fun validatePassword(password: String): Boolean{
        val pattern: String = "^[a-z0-9]{6}\$"
        val regExp: Regex = Regex(pattern)
        return regExp.matches(password)
    }
    private fun Message( message:String){
        Toast.makeText(this,"${message}",Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,Login::class.java)
        startActivity(intent)
        finish()
    }
}