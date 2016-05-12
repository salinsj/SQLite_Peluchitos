package danielsalinasjaramillo.com.practica7;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by daniel on 05/05/2016.
 */
public class MarvelDB extends SQLiteOpenHelper
{

    private static final String DATA_BASE_NAME="Vengadores";
    private static final int DATA_VERSION=1;


    //String sqlCreate = "CREATE TABLE Usuarios(codigo INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT,apellido TEXT)";


    String sqlCreate = "CREATE TABLE Heroes(id INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT,cantidad INTEGER,valor INTEGER,ventas INTEGER)";

     String InsertVengadores = "INSERT INTO Heroes(nombre,cantidad,valor,ventas)" +
             " VALUES('Iron_Man','10','15000','0'),('Viuda_Negra','10','12000','0'),('Capitan_America','10','15000','0')," +
             "('Hulk','10','12000','0'),('Bruja_Escarlata','10','15000','0'),('Spider_Man','10','10000','0')";



    public MarvelDB(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
        db.execSQL(InsertVengadores);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
