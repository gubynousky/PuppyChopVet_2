package cl.martinez.puppychopvet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos Room para PuppyChop
 * Gestiona las citas veterinarias
 */
@Database(
    entities = [CitaVeterinaria::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * DAO para acceder a las citas veterinarias
     */
    abstract fun citaVeterinariaDao(): CitaVeterinariaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "puppychop_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}