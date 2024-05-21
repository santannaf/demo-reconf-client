package org.example.democlientreconf

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/client/reconf"])
class TestReconfClientController(
    private val reconfParameters: ReconfParameters
) {
    @GetMapping
    fun fetchMYProperty(): Boolean {
        return this.reconfParameters.myKey()
    }
}
