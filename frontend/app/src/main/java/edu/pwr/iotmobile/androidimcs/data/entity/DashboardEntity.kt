package edu.pwr.iotmobile.androidimcs.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dashboards")
data class DashboardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dashboardId: Int,
    val projectId: Int,
    val dashboardName: String,
)
