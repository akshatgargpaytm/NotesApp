package com.example.notestakingapp.data.local.entity

/*Entity: Tells Room to treat this class as a table in the SQLite database.
PrimaryKey: Marks a field as the primary key (unique identifier) for the table.*/
import androidx.room.Entity
import androidx.room.PrimaryKey

//This annotation marks the class as a Room entity (table).
//tableName = "notes": Sets the name of the table in the database as "notes". If not specified,
// Room uses the class name as the table name.

@Entity(tableName = "notes")

data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val imageUrl: String? = null
)
