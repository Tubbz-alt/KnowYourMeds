package com.tompee.utilities.knowyourmeds.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.tompee.utilities.knowyourmeds.model.MarketDrug

@Entity(tableName = "market_drug",
        primaryKeys = ["medId", "setId"],
        foreignKeys = [ForeignKey(entity = MedicineEntity::class,
                parentColumns = ["id"], childColumns = ["medId"], onDelete = ForeignKey.CASCADE)],
        indices = [Index(name = "marketEntityIndex", value = ["medId"])])
data class MarketDrugEntity(

        @ColumnInfo(name = "medId")
        var medId: String = "",

        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "setId")
        var setId: String = "",

        @ColumnInfo(name = "version")
        var version: Int = 0,

        @ColumnInfo(name = "date")
        var publishedDate: String = ""
) {
    fun convertToMarketDrug(): MarketDrug =
            MarketDrug(name, setId, version, publishedDate)
}