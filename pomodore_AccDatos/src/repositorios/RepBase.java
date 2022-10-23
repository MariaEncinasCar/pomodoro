/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositorios;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 * Clase que genera el repositorio base con la conexión a la base de datos
 * @author Arguello, Encinas, García
 */
public interface RepBase {
    
    public static final String HOST = "localhost";
    public static final String PUERTO = "27017";
    public static final String BASE_DATOS = "pomodoro";
    
    public CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
    public CodecRegistry codeRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

    public ConnectionString cadenaConexion = new ConnectionString(String.format("mongodb://%s/%s", HOST, PUERTO));

    public MongoClientSettings clientSetting = MongoClientSettings.builder()
            .applyConnectionString(cadenaConexion).codecRegistry(codeRegistry)
            .build();

    //conexion al servidor
    public com.mongodb.client.MongoClient mongoClient = MongoClients.create(clientSetting);

    //acceso base de datos
    public com.mongodb.client.MongoDatabase baseDatos = mongoClient.getDatabase(BASE_DATOS);
    
}
