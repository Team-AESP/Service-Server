package io.say.better.domain.member.ui

import io.say.better.core.enums.RoleType
import io.say.better.domain.member.application.AuthFacade
import io.say.better.global.common.response.ResponseDto
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authFacade: AuthFacade,
) {
    @PostMapping("/assign/educator")
    fun assignEducator(): ResponseDto<Nothing?> {
        val role = RoleType.EDUCATOR
        authFacade.assignUserRole(role)
        return ResponseDto.onSuccess(null)
    }

    @PostMapping("/assign/learner")
    fun assignLearner(): ResponseDto<Nothing?> {
        val role = RoleType.LEARNER
        authFacade.assignUserRole(role)
        return ResponseDto.onSuccess(null)
    }
}
