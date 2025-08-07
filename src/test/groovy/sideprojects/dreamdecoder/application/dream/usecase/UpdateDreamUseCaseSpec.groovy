package sideprojects.dreamdecoder.application.dream.usecase

import spock.lang.Specification
import sideprojects.dreamdecoder.application.dream.service.DreamService
import sideprojects.dreamdecoder.domain.dream.model.Dream
import sideprojects.dreamdecoder.domain.dream.util.exception.DreamNotFoundException
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle

import java.time.LocalDateTime

class UpdateDreamUseCaseSpec extends Specification {

    DreamService dreamService = Mock()

    UpdateDreamUseCaseImpl updateDreamUseCase

    def setup() {
        updateDreamUseCase = new UpdateDreamUseCaseImpl(dreamService)
    }

    def "꿈을 성공적으로 수정해야 한다"() {
        given: "수정할 꿈과 업데이트된 내용"
        def dreamId = 1L
        def existingDream = Dream.builder()
                .id(dreamId)
                .userId(1L)
                .dreamContent("원래 꿈 내용")
                .interpretationResult("원래 해몽 결과")
                .aiStyle(AiStyle.DEFAULT)
                .interpretedAt(LocalDateTime.now())
                .build()

        def updatedDreamContent = "수정된 꿈 내용"
        def updatedInterpretationResult = "수정된 해몽 결과"
        def updatedAiStyle = AiStyle.KIND

        def updatedDream = Dream.builder()
                .id(dreamId)
                .userId(1L)
                .dreamContent(updatedDreamContent)
                .interpretationResult(updatedInterpretationResult)
                .aiStyle(updatedAiStyle)
                .interpretedAt(existingDream.getInterpretedAt())
                .build()

        dreamService.updateDream(dreamId, updatedDream) >> updatedDream

        when: "update 메소드를 호출하면"
        def result = updateDreamUseCase.update(dreamId, updatedDream)

        then: "수정된 꿈 객체가 반환되어야 한다"
        1 * dreamService.updateDream(dreamId, updatedDream)
        result.id == dreamId
        result.dreamContent == updatedDreamContent
        result.interpretationResult == updatedInterpretationResult
        result.aiStyle == updatedAiStyle
    }

    def "존재하지 않는 꿈 수정 시 DreamNotFoundException이 발생해야 한다"() {
        given: "존재하지 않는 꿈 ID와 업데이트된 내용"
        def dreamId = 99L
        def updatedDream = Dream.builder()
                .id(dreamId)
                .userId(1L)
                .dreamContent("수정된 꿈 내용")
                .interpretationResult("수정된 해몽 결과")
                .aiStyle(AiStyle.DEFAULT)
                .interpretedAt(LocalDateTime.now())
                .build()

        dreamService.updateDream(dreamId, updatedDream) >> { throw new DreamNotFoundException("꿈을 찾을 수 없습니다.") }

        when: "update 메소드를 호출하면"
        updateDreamUseCase.update(dreamId, updatedDream)

        then: "DreamNotFoundException이 발생해야 한다"
        thrown(DreamNotFoundException)
        1 * dreamService.updateDream(dreamId, updatedDream)
    }
}
