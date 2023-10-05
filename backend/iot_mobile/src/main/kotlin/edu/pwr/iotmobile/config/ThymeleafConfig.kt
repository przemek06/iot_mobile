package edu.pwr.iotmobile.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver


@Configuration
class ThymeleafConfiguration {
    @Bean
    fun templateEngine(): TemplateEngine {
        val templateEngine = TemplateEngine()
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.setTemplateMode("HTML")
        templateResolver.prefix = "templates/" // Path to your template file
        templateResolver.suffix = ".html"
        templateEngine.setTemplateResolver(templateResolver)
        return templateEngine
    }
}