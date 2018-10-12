package com.tompee.utilities.knowyourmeds.core.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "main")
data class Main(

        @PrimaryKey
        @ColumnInfo(name = "rxcui")
        var id: String,

        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "presc")
        var isPrescribable: Boolean,

        @ColumnInfo(name = "tty")
        var type: String,

        @ColumnInfo(name = "url")
        var url: String,

        @ColumnInfo(name = "ingredients")
        var ingredients: List<String>,

        @ColumnInfo(name = "sources")
        var sources: List<String>,

        @ColumnInfo(name = "brands")
        var brands: List<String>,

        @ColumnInfo(name = "SCDC")
        var scdc: List<String>,

        @ColumnInfo(name = "SBDC")
        var sbdc: List<String>,

        @ColumnInfo(name = "SBDG")
        var sbdg: List<String>,

        @ColumnInfo(name = "SCD")
        var scd: List<String>,

        @ColumnInfo(name = "date")
        var date: Date,

        @ColumnInfo(name = "SBD")
        var sbd: List<String>,

        @ColumnInfo(name = "SPL_SET")
        var splSet: List<String>

//        @ColumnInfo(name = "INTERACTION")
//        var interaction: Map<String, Map<String, String>>
)