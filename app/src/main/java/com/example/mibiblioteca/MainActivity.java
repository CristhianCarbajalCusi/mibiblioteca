package com.example.mibiblioteca;

import static com.android.volley.toolbox.Volley.newRequestQueue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    ListView lista_libros;
    Spinner spinTIPO;

    EditText nombre,apellido,correo;
    Button btnprestamo;
    List<Libro> obtener_libros_por_Genero;
    List<Libro> prestamo_generar_comprobante = new ArrayList<Libro>();
    String PEDIDO_ALUMNO = "";
    public static final String COMPROBANTE = "#Comprobante";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre = findViewById(R.id.txtNombre);
        apellido = findViewById(R.id.txtApellido);
        correo = findViewById(R.id.txtCorreo);
        btnprestamo = findViewById(R.id.btnPrestamo);
        btnprestamo.setEnabled(false);
        spinTIPO = findViewById(R.id.spinTipo);
        String URL = "http://192.168.1.7/BIBLIOTECA.MYSQL.ANDROID/mostrar_libros.php";
        buscarDatos(URL);
        btnprestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 generar_prestamo();
            }
        });

    }
    private void generar_prestamo(){
        TimeZone timeZonePeru = TimeZone.getTimeZone("America/Lima");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(timeZonePeru);
        String fechaHoraPeru = dateFormat.format(new Date());
        // Generar un número aleatorio de 3 dígitos
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100;
        String numerodeorden = "ORD-"+numeroAleatorio;
        String comprobante = "";
        comprobante += "Numero de comprobante de alumno: "+numerodeorden+"\n";
        comprobante += "estudiante: "+nombre+" "+apellido+"\n";
        comprobante += "correo: "+correo+"\n";
        comprobante += "Fecha de prestamo: "+ fechaHoraPeru+"\n";
        for(Libro li : prestamo_generar_comprobante){
            comprobante +="Libro: "+li.getTitulo()+"\n";
            comprobante +="Autor: "+li.getAutor()+"\n";
            comprobante +="Genero: "+li.getGenero()+"\n";
        }
        comprobante += "\n\n"+"Gracias por su pasion por los libros,vuelva pronto!"+"\n";
        // pasar parametro a otro intent
        Intent toPrestamo= new Intent(MainActivity.this, comprobante.class);
        toPrestamo.putExtra(COMPROBANTE,comprobante);
        startActivity(toPrestamo);
    }
    private void buscarDatos(String URL){

        JsonArrayRequest jsr = new JsonArrayRequest(URL,(response)->{
            System.out.println("Respuesta JSON: " + response.toString());
            List<Libro> libros = obtener_libros_php(response);
            ArrayList<String> lista_tipo_genero = agregar_genero_al_spinner();
            lista_libros = findViewById(R.id.LVlibros);
            spinTIPO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    obtener_libros_por_Genero = elegir_por_genero(lista_tipo_genero.get(i),libros);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            lista_libros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    carrito(i);
                    }
            });
        },(error)->{System.out.println(error);});

        //RequestQueue rq = Volley.newRequestQueue(this);
        //rq.start();
        //rq = newRequestQueue(this);
        //rq.add(jsr);
        RequestQueue rq = newRequestQueue(this);
        rq.add(jsr);

    }

    public void carrito(int i){

        if(nombre.getText().toString().equals("") || apellido.getText().toString().equals("") || correo.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe rellenaar los tres campos",Toast.LENGTH_SHORT).show();

        }else {

            nombre.setEnabled(false);
            apellido.setEnabled(false);
            correo.setEnabled(false);
            btnprestamo.setEnabled(true);
            prestamo_generar_comprobante.add(obtener_libros_por_Genero.get(i));

            for(Libro li : prestamo_generar_comprobante){

                PEDIDO_ALUMNO += "Titulo: "+ li.getTitulo()+"\n"+
                        "Autor: "+ li.getAutor()+"\n\n";

            }

            Toast.makeText(getApplicationContext(), "Alumno.Usted Esta prestandose:\n"+PEDIDO_ALUMNO,Toast.LENGTH_LONG).show();

        }
    }
    private ArrayList<String> agregar_genero_al_spinner() {
        ArrayList<String> lista_tipo_genero = new ArrayList<String>();
        lista_tipo_genero.add("Seleccione el genero de libro");
        lista_tipo_genero.add("Realismo mágico");
        lista_tipo_genero.add("Poesía");
        lista_tipo_genero.add("Novela");

        ArrayAdapter spinTipo = new ArrayAdapter(
                this, android.R.layout.simple_spinner_dropdown_item,lista_tipo_genero);
        spinTIPO.setAdapter(spinTipo);
        return lista_tipo_genero;
    }
    public List<Libro> elegir_por_genero(String genero,List<Libro> libros){
        ArrayList<String> datos_de_libros = new ArrayList<String>();
        List<Libro> obtener_libros_por_genero = new ArrayList<Libro>();
        if(genero.equals("Seleccione el genero de libro")){
            for (Libro li : libros) {
                datos_de_libros.add("=> "+li.getTitulo() + "\n"+"Autor: " + li.getAutor() + "\n"+"Genero: "+li.getGenero()+"\n");
                obtener_libros_por_genero.add(li);
            }
            ArrayAdapter consumoAdaptador = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,datos_de_libros);
            lista_libros.setAdapter(consumoAdaptador);
            return obtener_libros_por_genero;
        }else{
            for (Libro li : libros) {
                if(li.getGenero().equals(genero))
                    datos_de_libros.add("=> "+li.getTitulo()+"\n"+"Autor: "+ li.getAutor()+"\n"+"Genero: "+li.getGenero()+"\n");
                obtener_libros_por_genero.add(li);
            }

            ArrayAdapter consumoAdaptador = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,datos_de_libros);
            lista_libros.setAdapter(consumoAdaptador);
            return obtener_libros_por_genero;
        }
    }
    public List<Libro> obtener_libros_php(JSONArray response){
        JSONObject jso = null;
        List<Libro> libros = new ArrayList<Libro>();
        for(int i = 0; i<response.length();i++){
            try{
                jso = response.getJSONObject(i);
                int libro_id = jso.getInt("libro_id");
                String titulo = jso.getString("titulo");
                String autor = jso.getString("autor");
                String genero = jso.getString("genero");
                Libro lib = new Libro(libro_id, titulo, autor, genero);
                libros.add(lib);
            }catch(Exception e){
                System.out.println(e);
            }
        }
        return libros;
    }




}