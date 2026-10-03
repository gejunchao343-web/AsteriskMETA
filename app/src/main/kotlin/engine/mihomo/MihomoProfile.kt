import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class MihomoProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val content: String,
    val updateTime: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)