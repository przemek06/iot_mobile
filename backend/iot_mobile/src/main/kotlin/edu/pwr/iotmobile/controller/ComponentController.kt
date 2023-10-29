package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.ComponentListDTO
import edu.pwr.iotmobile.service.ComponentService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ComponentController (val componentService: ComponentService) {

    @PutMapping("/user/component")
    fun updateAll(@Valid @RequestBody componentListDTO: ComponentListDTO) : ResponseEntity<ComponentListDTO> {
        return ResponseEntity.ok(componentService.updateAll(componentListDTO))
    }

    @GetMapping("/user/component/{dashboardId}")
    fun findAllByDashboardId(@PathVariable dashboardId: Int) : ResponseEntity<ComponentListDTO> {
        return ResponseEntity.ok(componentService.findAllByDashboardId(dashboardId))
    }


}