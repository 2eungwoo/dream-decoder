package sideprojects.dreamdecoder.cache

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType
import sideprojects.dreamdecoder.global.config.CacheConfig
import sideprojects.dreamdecoder.global.config.LocalCacheConfig
import sideprojects.dreamdecoder.global.properties.LocalCacheProperties
import sideprojects.dreamdecoder.infrastructure.external.openai.config.OpenAiClient
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle
import sideprojects.dreamdecoder.infrastructure.external.openai.service.InterpretationProvider
import spock.lang.Specification

import java.time.Duration

@SpringBootTest(classes = LocalCacheTestApp)
class LocalCacheSpec extends Specification {

    @Autowired
    InterpretationProvider interpretationProvider

    @SpringBean
    OpenAiClient openAiClient = Mock()

    def "동일한 입력에 대해 로컬 캐싱이 트리거되어 OpenAI호출이 한 번만 일어난다"() {
        given:
        def style = AiStyle.DEFAULT
        def emotion = DreamEmotion.CALM
        def dreamContent = "스타벅스에서 커피 마시면서 코딩하는 꿈"
        def firstTypes = [DreamType.FOOD_FEAST, DreamType.SCHOOL]
        def secondTypes = [DreamType.SCHOOL, DreamType.FOOD_FEAST] // 순서만 다르게
        def expected = "대충 해몽 결과"

        and:
//        openAiClient.chat(_ as String, dreamContent) >> expected
        openAiClient.chat(_ as String, _ as String) >> expected

        when:
        def first = interpretationProvider.generateInterpretation(style, emotion, firstTypes, dreamContent)
        def second = interpretationProvider.generateInterpretation(style, emotion, secondTypes, dreamContent)

        then:
        first == second
        1 * openAiClient.chat(_ as String, _ as String)
    }

    @EnableCaching // @Cachable은 프록시동작이라 실제 로컬캐시되느닞 보려면 캐시AOP활성화 해줘야됨
    @SpringBootConfiguration
    @Import([InterpretationProvider, LocalCacheConfig, CacheConfig])
    static class LocalCacheTestApp {

        @Bean
        LocalCacheProperties localCacheProperties() {
            def props = new LocalCacheProperties()
            props.initialCapacity = 10
            props.maximumSize = 100
            props.expireAfterWrite = Duration.ofMinutes(10)
            return props
        }
    }
}
