package org.example.democlientreconf

import java.util.concurrent.TimeUnit
//import org.blocks4j.reconf.client.annotations.ConfigurationItem
//import org.blocks4j.reconf.client.annotations.ConfigurationRepository

import reconf.client.annotations.ConfigurationItem
import reconf.client.annotations.ConfigurationRepository

@ConfigurationRepository(
    product = "santannaf",
    component = "client",
    pollingRate = 10,
    pollingTimeUnit = TimeUnit.SECONDS
)
interface ReconfParameters {
    @ConfigurationItem("my-key2")
    fun myKey(): Boolean
}
