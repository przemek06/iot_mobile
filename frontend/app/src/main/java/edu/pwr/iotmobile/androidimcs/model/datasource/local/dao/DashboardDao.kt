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

    @Query("SELECT * FROM dashboards WHERE userId LIKE :userId")
    fun getAllByUserId(userId: Int): List<DashboardEntity>

    @Query("SELECT * FROM dashboards WHERE dashboardId LIKE :id")
    fun getByDashboardId(id: Int): List<DashboardEntity>

    @Delete
    fun deleteDashboards(vararg dashboards: DashboardEntity)

    @Query("DELETE FROM dashboards WHERE dashboardId LIKE :id")
    fun deleteDashboardById(id: Int)
}