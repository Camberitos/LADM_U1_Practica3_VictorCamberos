package mx.edu.ittepic.ladm_u1_practica3_victorcamberos

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*


class MainActivity : AppCompatActivity() {

    var total : String = ""
    var total2 : String = ""
    var vectorsito = Array<String>(10,{"0"})
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var valor : String = ""
        var posicion : Int = 0
        btnAsignar.setOnClickListener {
            valor = editValor.text.toString()
            posicion = editVector.text.toString().toInt()
            Asignar(valor,posicion)
        }
        btnMostrar.setOnClickListener {
            Mostrar()
        }
        btnGuardar.setOnClickListener {
            Guardar()
        }
        btnLeer.setOnClickListener {
            Leer()
        }
    }
    fun Asignar(valor1:String,posicion1:Int){
        vectorsito[posicion1] = valor1
        editValor.setText("")
        editVector.setText("")
    }
    fun Mostrar(){
        total = ""
        (0..9).forEach() {
            total = total + vectorsito[it] + "\n"
        }
        AlertDialog.Builder(this)
            .setTitle("")
            .setMessage(total)
            .show()
    }
    fun Guardar(){
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){//preguntar si está denegado para solicitarlo
                ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0) //
            //EL CÓDIGO DE ARRIBA ES EL EQUIVALENTE A OTORGAR PERMISOS EN MANIFEST
        }

        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
        }
        total2 = ""
        (0..9).forEach() {
            total2 = total2 + vectorsito[it] + " - "
        }
        try{
            //Referenciar a memoria externa
            var rutaSD = Environment.getExternalStorageDirectory() //la línea indica que está depreciado pero si funciona
            var datoArchivo = File(rutaSD.absolutePath,editGuardar.text.toString() + ".txt")
            var flujoSalida = OutputStreamWriter(FileOutputStream(datoArchivo))//FileOutputStream se puede referenciar a la ruta que sea
            flujoSalida.write(total2)
            flujoSalida.flush() //opcional equivalente al commit ()
            flujoSalida.close()

            mensaje("EXITO! se guardó correctamente")
            ponerTextos("","","")

        }catch( error : IOException){
            mensaje(error.message.toString())
        }
    }
    fun Leer (){
        if(noSD()) {
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,editLeer.text.toString() + ".txt")
            var flujoEntrada = BufferedReader( //se usa BufferedReader para que no lea letra por letra (HACE REFERENCIA A MEMORIA PRINCIPAL)
                InputStreamReader(
                    FileInputStream(datosArchivo)))
            var data = flujoEntrada.readLine()
            AlertDialog.Builder(this)
                .setTitle("Leer")
                .setMessage(data)
                .show()
            //ponerTextos(vector[0],vector[1],vector[2])
            //flujoEntrada.close()

        }catch(error:IOException){
            mensaje(error.message.toString())
        }
    }
    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }
    fun noSD(): Boolean{
        var estado = Environment.getExternalStorageState()//CLASE ESTÁTICA ENFOCADO A LA MEMORIA EXTERNA
        if (estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }
    fun ponerTextos(t1:String,t2:String, t3:String){ //Método que está un poco de más
        editValor.setText(t1)
        editVector.setText(t2)
        editGuardar.setText(t3)
    }

}
