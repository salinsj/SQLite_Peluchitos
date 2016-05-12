package danielsalinasjaramillo.com.practica7;

    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.ContentValues;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.support.v4.app.NotificationCompat;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    public class MainActivity extends AppCompatActivity {

        private Button bAgregar;
        private Button bBuscar;
        private Button bEliminar;
        private Button bActualziar;
        private Button bMostrar;
        private Button bVender;
        private Button bGanancias;
        private Button bLimpiar;
        private TextView Vista;
        private EditText etCodigo;
        private EditText etNombre;
        private EditText etCantidad;
        private EditText etValor;

        SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAgregar = (Button) findViewById(R.id.bAgregar);
        bBuscar = (Button) findViewById(R.id.bBuscar);
        bEliminar = (Button) findViewById(R.id.bEliminar);
        bActualziar = (Button) findViewById(R.id.bActualizar);
        bMostrar = (Button) findViewById(R.id.bMostrar);
        bVender = (Button) findViewById(R.id.bVender);
        bGanancias = (Button) findViewById(R.id.bGanancias);
        bLimpiar = (Button) findViewById(R.id.bLimpiar);
        Vista = (TextView) findViewById(R.id.vista);
        etCodigo = (EditText) findViewById(R.id.etCodigo);
        etNombre=(EditText) findViewById(R.id.etNombre);
        etCantidad=(EditText) findViewById(R.id.etCantidad);
        etValor=(EditText) findViewById(R.id.etValor);

        MarvelDB heroe = new MarvelDB(this);

        db=heroe.getWritableDatabase();

       Ver_Tabla();

        bAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codigo = etCodigo.getText().toString();
                String nombre = etNombre.getText().toString();
                String cantidad = etCantidad.getText().toString();
                String valor = etValor.getText().toString();

                if(nombre.equals("") || cantidad.equals("") || valor.equals(""))
                    Toast.makeText(MainActivity.this,"Digite: nombre, cantidad y valor",Toast.LENGTH_SHORT).show();
                else
                {
                    ContentValues nuevoRegistro = new ContentValues();
                    nuevoRegistro.put("nombre", nombre);
                    nuevoRegistro.put("cantidad", cantidad);
                    nuevoRegistro.put("valor", valor);
                    nuevoRegistro.put("ventas", 0);

                    if (codigo.equals(""))
                        db.insert("Heroes", null, nuevoRegistro);
                    else {
                        nuevoRegistro.put("id", codigo);
                        db.insert("Heroes", null, nuevoRegistro);
                    }
                    Ver_Tabla();
                    Toast.makeText(MainActivity.this, "Peluche \"" + nombre + "\" Agregado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etNombre.getText().toString();

                String[] campos = new String[]{"id","nombre","cantidad","valor","ventas"};
                String[] args = new String[]{name};

                Cursor c = db.query("Heroes", campos, "nombre=?", args, null, null, null);

                if(c.moveToFirst())
                {
                    Vista.setText("");
                    Vista.setText("  Codigo -- Nombre -- Cantidad -- Valor -- Vendidos\n");
                    do {
                        String codigo = c.getString(0);
                        String nombre = c.getString(1);
                        String cantidad= c.getString(2);
                        String valor= c.getString(3);
                        String ventas=c.getString(4);

                        Vista.append("    " + codigo + "   --  " + nombre +"  --    " + cantidad + "    --    " + valor + "    -- "+ventas+"\n");
                    } while (c.moveToNext());
                }
                else
                    Vista.setText("El peluche llamado \""+name+"\" no se encuentra registrado, verifiquelo.");
            }
        });

        bEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etNombre.getText().toString();

                String[] campos = new String[]{"id"};
                String[] args = new String[]{name};

                Cursor c = db.query("Heroes", campos, "nombre=?", args, null, null, null);

                if(!c.moveToFirst())
                    Vista.setText("El peluche llamado \""+name+"\" no se encuentra registrado, verifiquelo.");
                else
                {
                    String id = c.getString(0);
                    db.delete("Heroes", "id=" + id, null);
                    Ver_Tabla();
                    Toast.makeText(MainActivity.this,"Peluche \""+name+"\" Eliminado",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bActualziar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etNombre.getText().toString();

                String[] campos = new String[]{"id"};
                String[] args = new String[]{name};

                Cursor c = db.query("Heroes", campos, "nombre=?", args, null, null, null);

                if (!c.moveToFirst())
                    Vista.setText("El peluche llamado \""+name+"\" no se encuentra registrado, verifiquelo.");
                else
                {
                    String id = c.getString(0);
                    String cantidad = etCantidad.getText().toString();

                    if (cantidad.equals(""))
                        Toast.makeText(MainActivity.this,"Digite la nueva cantidad.",Toast.LENGTH_SHORT).show();
                    else
                    {
                        ContentValues nuevoValor = new ContentValues();
                        nuevoValor.put("cantidad", cantidad);
                        db.update("Heroes", nuevoValor, "id=" + id, null);
                        Ver_Tabla();
                        Toast.makeText(MainActivity.this, "Cantidad de Peluches \"" + name + "\" Asignada a " + cantidad, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        bMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ver_Tabla();
            }
        });

        bVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etNombre.getText().toString();

                String[] campos = new String[]{"id","cantidad","ventas"};
                String[] args = new String[]{name};

                Cursor c = db.query("Heroes", campos, "nombre=?", args, null, null, null);

                if (!c.moveToFirst())
                    Vista.setText("El peluche llamado \""+name+"\" no se encuentra registrado, verifiquelo.");
                else
                {
                    String objetos = etCantidad.getText().toString();
                    if (objetos.equals(""))
                        Toast.makeText(MainActivity.this, "Digite la cantidad de peluches que desea vender.", Toast.LENGTH_SHORT).show();
                    else
                    {
                        int unidades = Integer.parseInt(objetos);
                        String id = c.getString(0);
                        int cantidad = c.getInt(1);
                        int ventas = c.getInt(2);
                        if (cantidad >= unidades) {
                            cantidad -= unidades;
                            ventas += unidades;
                            ContentValues nuevoValor = new ContentValues();
                            nuevoValor.put("cantidad", cantidad);
                            nuevoValor.put("ventas", ventas);
                            db.update("Heroes", nuevoValor, "id=" + id, null);
                            Ver_Tabla();
                            Toast.makeText(MainActivity.this, "Se vendieron " + unidades + " peluches \"" + name + "\"", Toast.LENGTH_SHORT).show();
                            //aqui viene la notificacion
                            if (cantidad <= 5) {
                                String title, content, ticker;

                                ticker = "Existencias Escasas";
                                title = "Peluche " + name;
                                content = "Quedan menos de 5 peluches";

                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);

                                builder.setContentTitle(title)
                                        .setContentText(content)
                                        .setTicker(ticker)
                                        .setSmallIcon(R.drawable.peluchitos)
                                        .setContentInfo("Dato");

                                Intent notIntent = new Intent(MainActivity.this, MainActivity.class);

                                PendingIntent contIntent = PendingIntent.getActivity(MainActivity.this, 0, notIntent, 0);

                                builder.setContentIntent(contIntent);

                                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                nm.notify(1, builder.build());
                            }
                        }
                        else
                            Vista.setText("Solo tiene " + cantidad + " unidades del peluche \"" + name + "\", por tanto no puede vender " + unidades);
                    }
                }
            }
        });

        bGanancias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int ganancias=0,ventas,valor;

               Ver_Tabla();

                Cursor c = db.rawQuery("SELECT valor,ventas FROM Heroes", null);

                if (c.moveToFirst())
                    do {
                        valor=c.getInt(0);
                        ventas=c.getInt(1);
                        ganancias+=valor*ventas;

                    }while (c.moveToNext());

                Vista.append("\nLas ganancias totales son: "+ganancias);
            }
        });

        bLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCodigo.setText("");
                etNombre.setText("");
                etCantidad.setText("");
                etValor.setText("");
                Ver_Tabla();
            }
        });
    }

   protected void Ver_Tabla()
    {
        //PAra mostrar todos los campos de la tabla
        Cursor c = db.rawQuery("SELECT id,nombre,cantidad,valor,ventas FROM Heroes", null);

        Vista.setText("  Codigo -- Nombre -- Cantidad -- Valor -- Vendidos\n");

        if (c.moveToFirst())
            do {
                String id = c.getString(0);
                String nombre = c.getString(1);
                String cantidad = c.getString(2);
                String valor = c.getString(3);
                String ventas = c.getString(4);

                Vista.append("    " + id + "   --  " + nombre +"  --    " + cantidad + "    --    " + valor + "    -- "+ventas+"\n");

            } while (c.moveToNext());
    }
}
