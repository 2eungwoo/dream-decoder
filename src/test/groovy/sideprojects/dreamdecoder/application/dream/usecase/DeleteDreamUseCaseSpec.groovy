package sideprojects.dreamdecoder.application.dream.usecase.delete

import spock.lang.Specification
import sideprojects.dreamdecoder.application.dream.service.DreamService
import sideprojects.dreamdecoder.domain.dream.util.exception.DreamNotFoundException
import sideprojects.dreamdecoder.application.dream.usecase.delete.DeleteDreamUseCaseImpl

class DeleteDreamUseCaseSpec extends Specification {

    DreamService dreamService = Mock()

    DeleteDreamUseCaseImpl deleteDreamUseCase

    def setup() {
        deleteDreamUseCase = new DeleteDreamUseCaseImpl(dreamService)
    }

    def "꿈을 성공적으로 삭제해야 한다"() {
        given: "삭제할 꿈 ID"
        def dreamId = 1L

        dreamService.deleteDream(dreamId) >> { /* no return value for void method */ }

        when: "delete 메소드를 호출하면"
        deleteDreamUseCase.delete(dreamId)

        then: "dreamService.deleteDream이 한 번 호출되어야 한다"
        1 * dreamService.deleteDream(dreamId)
    }

    def "존재하지 않는 꿈 삭제 시 DreamNotFoundException이 발생해야 한다"() {
        given: "존재하지 않는 꿈 ID"
        def dreamId = 99L

        dreamService.deleteDream(dreamId) >> { throw new DreamNotFoundException("삭제할 꿈을 찾을 수 없습니다.") }

        when: "delete 메소드를 호출하면"
        deleteDreamUseCase.delete(dreamId)

        then: "DreamNotFoundException이 발생해야 한다"
        thrown(DreamNotFoundException)
        1 * dreamService.deleteDream(dreamId)
    }
}
