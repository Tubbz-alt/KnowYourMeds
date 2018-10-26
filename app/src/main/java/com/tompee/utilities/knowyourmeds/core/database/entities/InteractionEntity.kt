package com.tompee.utilities.knowyourmeds.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.tompee.utilities.knowyourmeds.model.InteractionPair

@Entity(tableName = "interactions",
        primaryKeys = ["medId", "source", "partner"],
        foreignKeys = [ForeignKey(entity = MedicineEntity::class,
                parentColumns = ["id"], childColumns = ["medId"], onDelete = ForeignKey.CASCADE)],
        indices = [Index(name = "interactionEntityIndex", value = ["medId"])])
data class InteractionEntity(
        @ColumnInfo(name = "medId")
        var medId: String = "",

        @ColumnInfo(name = "source")
        var source: String = "",

        @ColumnInfo(name = "sourceUrl")
        var sourceUrl: String = "",

        @ColumnInfo(name = "partner")
        var partner: String = "",

        @ColumnInfo(name = "partnerUrl")
        var partnerUrl: String = "",

        @ColumnInfo(name = "interaction")
        var interaction: String = ""
) {
    fun convertToPair(): InteractionPair =
            InteractionPair(source, sourceUrl, partner, partnerUrl, interaction)
}