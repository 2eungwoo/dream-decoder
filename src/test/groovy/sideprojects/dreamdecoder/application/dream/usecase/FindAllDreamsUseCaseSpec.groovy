package sideprojects.dreamdecoder.application.dream.usecase.find

import sideprojects.dreamdecoder.application.web.dream.usecase.find.FindAllDreamsUseCaseImpl
import spock.lang.Specification
import sideprojects.dreamdecoder.application.web.dream.service.DreamService
import sideprojects.dreamdecoder.domain.dream.model.DreamModel
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle

import java.time.LocalDateTime

class FindAllDreamsUseCaseSpec extends Specification {

    DreamService dreamService = Mock()

    FindAllDreamsUseCaseImpl findAllDreamsUseCase

    def setup() {
        findAllDreamsUseCase = new FindAllDreamsUseCaseImpl(dreamService)
    }

    def "모든 꿈 목록을 성공적으로 조회해야 한다"() {
        given: "두 개의 꿈이 존재한다"
        def dream1 = DreamModel.builder()
                .id(1L)
                .userId(1L)
                .dreamContent("꿈 내용 1")
                .interpretationResult("해몽 결과 1")
                .aiStyle(AiStyle.DEFAULT)
                .interpretedAt(LocalDateTime.now())
                .build()
        def dream2 = DreamModel.builder()
                .id(2L)
                .userId(1L)
                .dreamContent("꿈 내용 2")
                .interpretationResult("해몽 결과 2")
                .aiStyle(AiStyle.DEFAULT)
                .interpretedAt(LocalDateTime.now())
                .build()
        def expectedDreams = [dream1, dream2]

        dreamService.findAllDreams() >> expectedDreams

        when: "findAll 메소드를 호출하면"
        def result = findAllDreamsUseCase.findAll()

        then: "두 개의 꿈 목록이 반환되어야 한다"
        1 * dreamService.findAllDreams()
        result.size() == 2
        result == expectedDreams
    }

    def "꿈 목록이 없을 때 빈 리스트를 반환해야 한다"() {
        given: "꿈이 존재하지 않는다"
        dreamService.findAllDreams() >> []

        when: "findAll 메소드를 호출하면"
        def result = findAllDreamsUseCase.findAll()

        then: "빈 리스트가 반환되어야 한다"
        1 * dreamService.findAllDreams()
        result.isEmpty()
    }
}
