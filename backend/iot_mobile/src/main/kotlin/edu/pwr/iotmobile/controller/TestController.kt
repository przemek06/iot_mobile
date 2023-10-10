package edu.pwr.iotmobile.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/anon/test")
    fun anon() : String {
        return "anon"
    }

    @GetMapping("/user/test")
    fun user() : String {
        return "user"
    }

    @GetMapping("/admin/test")
    fun admin() : String {
        return "admin"
    }
}