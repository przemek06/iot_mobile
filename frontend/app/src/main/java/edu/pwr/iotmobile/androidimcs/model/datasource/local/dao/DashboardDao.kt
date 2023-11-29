package edu.pwr.iotmobile.androidimcs.model.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.pwr.iotmobile.androidimcs.data.entity.DashboardEntity

@Dao
interface DashboardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDashboard(dashboard: DashboardEntity)

    @Query("SELECT * FROM dashboards")
    fun getAll(): List<DashboardEntity>

    @Query("SELECT * FROM dashboards WHERE dashboardId LIKE :id")
    fun getByDashboardId(id: Int): List<DashboardEntity>

    @Delete
    fun deleteDashboards(vararg dashboards: DashboardEntity)
}