package sideprojects.dreamdecoder.application.dream.usecase

import spock.lang.Specification
import sideprojects.dreamdecoder.application.dream.service.DreamService
import sideprojects.dreamdecoder.domain.dream.model.Dream
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle

import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle

class SaveDreamUseCaseImplSpec extends Specification {

    DreamService dreamService = Mock()

    SaveDreamUseCaseImpl saveDreamUseCaseImpl

    def setup() {
        saveDreamUseCaseImpl = new SaveDreamUseCaseImpl(dreamService)
    }

    def "꿈이 성공적으로 저장되어야 한다"() {
        given: "유효한 Dream 객체"
        def userId = 1L
        def dreamContent = "어제 밤 꿈에 하늘을 날아다녔다."
        def interpretationResult = "자유를 갈망하는 마음이 반영된 꿈입니다."
        def aiStyle = AiStyle.DEFAULT

        def dreamToSave = Dream.createNewDream(userId, dreamContent, interpretationResult, aiStyle)
        def savedDream = Dream.builder()
                .id(1L)
                .userId(userId)
                .dreamContent(dreamContent)
                .interpretationResult(interpretationResult)
                .aiStyle(aiStyle)
                .interpretedAt(dreamToSave.getInterpretedAt())
                .build()

        dreamService.saveDream(_ as Dream) >> savedDream

        when: "save 메소드를 호출하면"
        def result = saveDreamUseCaseImpl.save(dreamToSave)

        then: "저장된 Dream 객체가 반환되어야 한다"
        1 * dreamService.saveDream(_ as Dream)
        result.id == savedDream.id
        result.userId == savedDream.userId
        result.dreamContent == savedDream.dreamContent
        result.interpretationResult == savedDream.interpretationResult
        result.aiStyle == savedDream.aiStyle
        result.interpretedAt == savedDream.interpretedAt
    }
}
