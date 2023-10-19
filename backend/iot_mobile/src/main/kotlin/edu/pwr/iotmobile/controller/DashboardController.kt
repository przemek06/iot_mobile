package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.DashboardDTO
import edu.pwr.iotmobile.service.DashboardService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DashboardController(val dashboardService: DashboardService) {
    @PostMapping("/user/dashboard")
    fun createDashboard(@Valid @RequestBody dashboard: DashboardDTO): ResponseEntity<DashboardDTO> {
        return ResponseEntity.ok(dashboardService.createDashboard(dashboard))
    }

    @DeleteMapping("/user/dashboard/{dashboardId}")
    fun deleteDashboard(@PathVariable dashboardId: Int): ResponseEntity<Unit> {
        return if (dashboardService.deleteDashboard(dashboardId)) ResponseEntity.ok().build()
        else ResponseEntity.noContent().build()
    }

    @GetMapping("/user/dashboard")
    fun findAllDashboardInProject(@PathVariable projectId: Int) : ResponseEntity<List<DashboardDTO>> {
        return ResponseEntity.ok(dashboardService.findAllDashboardInProject(projectId))
    }
}