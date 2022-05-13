package pp.developer.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pp.developer.myapplication.models.Restaurante
import pp.developer.myapplication.models.Usuario
import pp.developer.myapplication.services.APIServices
import pp.developer.myapplication.services.GetRetrofit
import retrofit2.Response
import retrofit2.Retrofit

class Login : AppCompatActivity() {
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
        setContentView(R.layout.activity_login)
        retroApi = GetRetrofit().getRetrofit()
        auth = FirebaseAuth.getInstance()
    }

    fun login_Click(view: View) {
        val usuario = findViewById<EditText>(R.id.iTxtUsuario).text.toString()
        val contrasena = findViewById<EditText>(R.id.iTxtContrasena).text.toString()
        if( !usuario.isNullOrEmpty() && validatePassword( contrasena) ){
            //validateUsuario("validateUsuario")
            auth.signInWithEmailAndPassword(usuario,contrasena).addOnCompleteListener {
                if(it.isSuccessful){
                    //Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_slideshow)
                    startActivity(Intent(this,MainActivity::class.java))
                }//else Message("Datos incorrectos")
            }.addOnFailureListener {
                Message(it.localizedMessage)
            }
        }else{
            Toast.makeText(this,"Usuario no valido.",Toast.LENGTH_SHORT).show()
        }
    }
//    fun validateUsuario(path: String){
//        var id:Int = 0
//        var usuario:String = ""
//            Message("Validando usuario...")
//
//            CoroutineScope(Dispatchers.IO).launch {
//                val call: Response<Usuario> = retroApi.create(APIServices::class.java).validateUser(path)
//                val rest:Usuario? = call.body()
//                runOnUiThread {
//                    if(call.isSuccessful){
//                        id = rest!!.id
//                        usuario = rest!!.usuario
//                    }else{
//                        Message("Error: Carga de informacion")
//                    }
//                }
//            }
//            val intent = Intent(this,MainActivity::class.java).apply {
//                putExtra(USER_ID,id)
//                putExtra(USER_USER,usuario)
//            }
//            startActivity(intent)
//    }
    private fun Message( message:String){
        Toast.makeText(this,"${message}",Toast.LENGTH_SHORT).show()
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

    fun txtV_Registro_Click(view: View) {
        val intent = Intent(this,RegistroUsuarioActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}