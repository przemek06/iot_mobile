package edu.pwr.iotmobile.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@Configuration
@EnableAspectJAutoProxy
class SpringConfig