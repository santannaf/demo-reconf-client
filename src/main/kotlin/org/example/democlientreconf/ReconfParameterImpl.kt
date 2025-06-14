package org.example.democlientreconf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reconf.client.proxy.ConfigurationRepositoryFactory
import reconf.client.proxy.Customization

@Configuration
class ReconfParameterImpl {
    @Bean
    fun reconfParameters(): ReconfParameters {
        val customization = Customization()
        customization.componentItemPrefix = ""
        return ConfigurationRepositoryFactory.get(ReconfParameters::class.java, customization)
    }

    companion object : ReconfParameters {
        override fun myKey(): Boolean {
            return true
        }
    }
}
