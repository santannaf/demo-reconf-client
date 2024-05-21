package org.example.democlientreconf

import org.blocks4j.reconf.client.annotations.ConfigurationItem
import org.blocks4j.reconf.client.annotations.ConfigurationRepository
import java.util.concurrent.TimeUnit

@ConfigurationRepository(
    product = "santannaf",
    component = "client",
    pollingRate = 1,
    pollingTimeUnit = TimeUnit.MINUTES
)
interface ReconfParameters {
    @ConfigurationItem("my-key2")
    fun myKey(): Boolean
}
