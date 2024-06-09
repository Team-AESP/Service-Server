package io.say.better.domain.solution.ui

import com.google.gson.Gson
import io.say.better.domain.solution.application.SolutionFacade
import io.say.better.domain.solution.ui.dto.SolutionRequest
import io.say.better.domain.solution.ui.dto.SolutionResponse
import io.say.better.global.common.code.status.SuccessStatus
import io.say.better.storage.mysql.domain.constant.ProgressState
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(SolutionController::class)
class SolutionControllerTest
    @Autowired
    constructor(
        private val mockMvc: MockMvc,
        @MockBean private val solutionFacade: SolutionFacade,
    ) {
        @Test
        @DisplayName("symbol 이름을 탐색한 결과 리스트를 반환한다. 검색 결과가 없을 경우 빈 리스트를 반환한다.")
        @WithMockUser
        fun searchSymbolTest() {
            // Given
            val symbolName = "testName"
            val symbolList = SolutionResponse.SymbolList(symbolName, null)
            BDDMockito.given(solutionFacade.searchSymbol(symbolName)).willReturn(symbolList)

            // When
            val actions: ResultActions =
                mockMvc.perform(
                    RestDocumentationRequestBuilders.get("/api/solution/symbol/search/{name}", symbolName)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()),
                )

            // Then
            actions.andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess", true).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", SuccessStatus.OK.code).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", SuccessStatus.OK.message).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", List::class).exists())
        }

        @Test
        @DisplayName("새로운 solution을 생성한다.")
        @WithMockUser
        fun createSolutionSuccessTest() {
            // Given
            val request =
                SolutionRequest.CreateSolution(
                    learnerEmail = "learnerEmail@gmail.com",
                    nowState = ProgressState.READY,
                    educationGoal = "testEducationGoal",
                    description = "testDescription",
                    title = "testTitle",
                    commOptTimes = 1,
                    commOptCnt = 1,
                )
            BDDMockito.doNothing().`when`(solutionFacade).createSolution(request)

            // When
            val converter = Gson()
            val actions: ResultActions =
                mockMvc.perform(
                    RestDocumentationRequestBuilders.post("/api/solution")
                        .content(converter.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()),
                )

            // Then
            actions.andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess", true).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", SuccessStatus.OK.code).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", SuccessStatus.OK.message).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").doesNotExist())
        }

        @Test
        @DisplayName("솔루션을 시작한다.")
        @WithMockUser
        fun startSolutionTest() {
            // Given
            val request: SolutionRequest.StartSolution =
                SolutionRequest.StartSolution(
                    sessionOrder = 1,
                    solutionId = 1,
                    nowStep = "BASELINE",
                    sessionGoal = "Test",
                    sessionDesc = "Test",
                )
            val progressInfo: SolutionResponse.ProgressInfo = SolutionResponse.ProgressInfo(1)
            BDDMockito.given(solutionFacade.startSolution(request)).willReturn(progressInfo)
            // When
            val converter = Gson()
            val actions: ResultActions =
                mockMvc.perform(
                    RestDocumentationRequestBuilders.post("/api/solution/start")
                        .content(converter.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()),
                )
            // Then
            actions.andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess", true).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", SuccessStatus.OK.code).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", SuccessStatus.OK.message).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", SolutionResponse.ProgressInfo::class).exists())
        }

        @Test
        @DisplayName("솔루션을 종료한다.")
        @WithMockUser
        fun endSolutionTest() {
            // Given
            val endSolution: SolutionRequest.EndSolution =
                SolutionRequest.EndSolution(
                    1,
                    1,
                    1,
                    listOf(
                        SolutionRequest.CreateRecordSymbol(
                            1,
                            1,
                            "timestamp",
                        ),
                    ),
                    1,
                )
            val request: List<SolutionRequest.EndSolution> = listOf(endSolution)
            BDDMockito.doNothing().`when`(solutionFacade).endSolution(request)

            // When
            val converter = Gson()
            val actions: ResultActions =
                mockMvc.perform(
                    RestDocumentationRequestBuilders.post("/api/solution/end")
                        .content(converter.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()),
                )

            // Then
            actions.andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess", true).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", SuccessStatus.OK.code).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", SuccessStatus.OK.message).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").doesNotExist())
        }

        @Test
        @DisplayName("symbol 추천 결과 리스트를 반환한다. 추천 결과가 없을 경우 빈 리스트를 반환한다.")
        @WithMockUser
        fun recommendSymbolTest() {
            // Given
            val word = "testWord"
            val recommendList = arrayListOf(
                SolutionResponse.SymbolInfo("testDescription1", "testImg1.jpg"),
                SolutionResponse.SymbolInfo("testDescription2", "testImg2.jpg")
            )
            BDDMockito.given(solutionFacade.recommendSymbol(word)).willReturn(SolutionResponse.SymbolList(word, recommendList))

            // When
            val actions: ResultActions =
                mockMvc.perform(
                    RestDocumentationRequestBuilders.get("/api/solution/symbol/recommend/{name}", word)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()),
                )

            // Then
            actions.andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess", true).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", SuccessStatus.OK.code).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", SuccessStatus.OK.message).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", List::class).exists())
        }
    }
