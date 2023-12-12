@file:OptIn(ExperimentalCoroutinesApi::class)

package edu.pwr.iotmobile.androidimcs

import edu.pwr.iotmobile.androidimcs.data.dto.DashboardDto
import edu.pwr.iotmobile.androidimcs.data.entity.DashboardEntity
import edu.pwr.iotmobile.androidimcs.data.result.CreateResult
import edu.pwr.iotmobile.androidimcs.model.datasource.local.dao.DashboardDao
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.DashboardRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.impl.DashboardRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import kotlin.test.assertFails
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class DashboardRepositoryTest {
    private val remoteDataSource: DashboardRemoteDataSource = mockk()
    private val localDataSource: DashboardDao = mockk()

    private val dashboardRepository = DashboardRepositoryImpl(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource
    )

    @Test
    fun createDashboardSuccessTest() = runTest {
        everyCreateDashboardReturnsSuccess()

        val dto = getDefaultDashboardDto()
        val result = dashboardRepository.createDashboard(dto)
        assertNotNull(result)
        assertEquals(CreateResult.Success, result)
    }

    @Test
    fun createDashboardFailureTest() = runTest {
        everyCreateDashboardReturnsFailure()

        val dto = getDefaultDashboardDto()
        val result = dashboardRepository.createDashboard(dto)
        assertEquals(CreateResult.Failure, result)
    }

    @Test
    fun createDashboardNotAuthorizedTest() = runTest {
        everyCreateDashboardReturnsNotAuthorized()

        val dto = getDefaultDashboardDto()
        val result = dashboardRepository.createDashboard(dto)
        assertEquals(CreateResult.NotAuthorized, result)
    }

    @Test
    fun createDashboardAlreadyExistsTest() = runTest {
        everyCreateDashboardReturnsAlreadyExists()

        val dto = getDefaultDashboardDto()
        val result = dashboardRepository.createDashboard(dto)
        assertEquals(CreateResult.AlreadyExists, result)
    }

    @Test
    fun deleteDashboardSuccessTest() = runTest {
        everyDeleteDashboardReturnsSuccess()

        val result = dashboardRepository.deleteDashboard(1)
        assertTrue { result.isSuccess }
    }

    @Test
    fun deleteDashboardFailureTest() = runTest {
        everyDeleteDashboardReturnsFailure()

        val result = dashboardRepository.deleteDashboard(1)
        assertTrue { result.isFailure }
    }

    @Test
    fun getDashboardsSuccessTest() = runTest {
        everyGetDashboardsReturnsSuccess()

        val dto = getDefaultDashboardDto()
        val result = dashboardRepository.getDashboardsByProjectId(1)
        assertEquals(dto.name, result.first().name)
        assertEquals(dto.id, result.first().id)
    }

    @Test
    fun getDashboardsFailureTest() = runTest {
        everyGetDashboardsReturnsFailure()

        val result = dashboardRepository.getDashboardsByProjectId(1)
        assertTrue { result.isEmpty() }
    }

    @Test
    fun getLastAccessedDashboardsSuccessTest() = runTest {
        everyGetLastAccessedDashboardsReturnsSuccess()

        val entity = getDefaultDashboardEntity()
        val result = dashboardRepository.getLastAccessedDashboardsByUserId(1)
        assertEquals(entity.dashboardName, result.first().dashboardName)
        assertEquals(entity.id, result.first().id)
        assertEquals(1, result.size)

        assertFails {  }
    }

    @Test
    fun getSaveDashboardFailureTest() = runTest {
        everySaveDashboardReturnsFailure()

        val entity = getDefaultDashboardEntity()
        assertFails { dashboardRepository.saveLastAccessedDashboard(entity) }
    }

    private fun everyCreateDashboardReturnsSuccess() {
        val dashboardDto = getDefaultDashboardDto()
        val response = Response.success(dashboardDto)

        every {
            runBlocking { remoteDataSource.createDashboard(any()) }
        } returns response
    }

    private fun everyCreateDashboardReturnsFailure() {
        val responseBody = "operation failed".toResponseBody()
        val response = Response.error<DashboardDto>(400, responseBody)

        every {
            runBlocking { remoteDataSource.createDashboard(any()) }
        } returns response
    }

    private fun everyCreateDashboardReturnsNotAuthorized() {
        val responseBody = "operation failed".toResponseBody()
        val response = Response.error<DashboardDto>(401, responseBody)

        every {
            runBlocking { remoteDataSource.createDashboard(any()) }
        } returns response
    }

    private fun everyCreateDashboardReturnsAlreadyExists() {
        val responseBody = "operation failed".toResponseBody()
        val response = Response.error<DashboardDto>(409, responseBody)

        every {
            runBlocking { remoteDataSource.createDashboard(any()) }
        } returns response
    }

    private fun everyDeleteDashboardReturnsSuccess() {
        val responseBody = "success".toResponseBody()
        val response = Response.success(responseBody)

        every {
            runBlocking { remoteDataSource.deleteDashboard(any()) }
        } returns response
    }

    private fun everyDeleteDashboardReturnsFailure() {
        val responseBody = "operation failed".toResponseBody()
        val response = Response.error<ResponseBody>(400, responseBody)

        every {
            runBlocking { remoteDataSource.deleteDashboard(any()) }
        } returns response
    }

    private fun everyGetDashboardsReturnsSuccess() {
        val responseBody = listOf(getDefaultDashboardDto())
        val response = Response.success(responseBody)

        every {
            runBlocking { remoteDataSource.getDashboardsByProjectId(any()) }
        } returns response
    }

    private fun everyGetDashboardsReturnsFailure() {
        val responseBody = "operation failed".toResponseBody()
        val response = Response.error<List<DashboardDto>>(400, responseBody)

        every {
            runBlocking { remoteDataSource.getDashboardsByProjectId(any()) }
        } returns response
    }

    private fun everyGetLastAccessedDashboardsReturnsSuccess() {
        val response = listOf(getDefaultDashboardEntity())

        every {
            runBlocking { localDataSource.getAllByUserId(any()) }
        } returns response
    }

    private fun everySaveDashboardReturnsFailure() {
        every {
            runBlocking { localDataSource.deleteDashboardById(any()) }
        } returns Unit

        every {
            runBlocking { localDataSource.insertDashboard(any()) }
        } throws Exception()
    }

    private fun getDefaultDashboardDto() = DashboardDto(
        id = 1,
        name = "d1",
        projectId = 1
    )

    private fun getDefaultDashboardEntity() = DashboardEntity(
        id = 1,
        userId = 1,
        dashboardId = 1,
        projectId = 1,
        dashboardName = "d1"
    )

}