package sideprojects.dreamdecoder.application.dream.usecase.find

import spock.lang.Specification
import sideprojects.dreamdecoder.application.dream.service.DreamService
import sideprojects.dreamdecoder.domain.dream.model.Dream
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle

import java.time.LocalDateTime

class FindDreamByIdUseCaseSpec extends Specification {

    DreamService dreamService = Mock()

    FindOneDreamUseCaseImpl findDreamByIdUseCase

    def setup() {
        findDreamByIdUseCase = new FindOneDreamUseCaseImpl(dreamService)
    }

    def "ID로 꿈을 성공적으로 조회해야 한다"() {
        given: "조회할 꿈이 존재한다"
        def dreamId = 1L
        def expectedDream = Dream.builder()
                .id(dreamId)
                .userId(1L)
                .dreamContent("꿈 내용")
                .interpretationResult("해몽 결과")
                .aiStyle(AiStyle.DEFAULT)
                .interpretedAt(LocalDateTime.now())
                .build()

        dreamService.findDreamById(dreamId) >> Optional.of(expectedDream)

        when: "findById 메소드를 호출하면"
        def result = findDreamByIdUseCase.findById(dreamId)

        then: "해당 꿈이 반환되어야 한다"
        1 * dreamService.findDreamById(dreamId)
        result.isPresent()
        result.get() == expectedDream
    }

    def "존재하지 않는 ID로 꿈 조회 시 빈 Optional을 반환해야 한다"() {
        given: "존재하지 않는 꿈 ID"
        def dreamId = 99L

        dreamService.findDreamById(dreamId) >> Optional.empty()

        when: "findById 메소드를 호출하면"
        def result = findDreamByIdUseCase.findById(dreamId)

        then: "빈 Optional이 반환되어야 한다"
        1 * dreamService.findDreamById(dreamId)
        !result.isPresent()
    }
}
