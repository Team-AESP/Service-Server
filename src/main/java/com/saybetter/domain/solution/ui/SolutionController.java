package com.saybetter.domain.solution.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saybetter.domain.solution.application.SolutionFacade;
import com.saybetter.domain.solution.ui.dto.SolutionResponse;
import com.saybetter.global.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Solution", description = "Solution API")
@RestController
@RequestMapping("/api/solution")
@RequiredArgsConstructor
public class SolutionController {

	private final SolutionFacade solutionFacade;

	@GetMapping("/symbol/recommend/{name}")
	public ResponseDto<SolutionResponse.SymbolRecommend> recommendSymbol(@PathVariable String name) {
		return ResponseDto.onSuccess(solutionFacade.recommendSymbol(name));
	}

}
