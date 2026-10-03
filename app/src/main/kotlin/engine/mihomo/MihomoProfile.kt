data class MihomoProfile(
    val name: String,
    val content: String,
    val updateTime: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)