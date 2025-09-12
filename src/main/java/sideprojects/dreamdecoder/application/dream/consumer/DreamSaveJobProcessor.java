package sideprojects.dreamdecoder.application.dream.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.StreamMessageId;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.application.dream.service.DreamService;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;
import sideprojects.dreamdecoder.infrastructure.external.openai.enums.AiStyle;
import sideprojects.dreamdecoder.presentation.dream.dto.request.SaveDreamRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class DreamSaveJobProcessor {

    private final DreamService dreamService;
    private final ObjectMapper objectMapper;

    public void process(RStream<String, String> stream, String groupName, StreamMessageId messageId,
        Map<String, String> messageBody) {
        try {
            SaveDreamRequest request = buildRequestFromMessage(messageBody);
            DreamModel dreamModelToSave = DreamModel.createNewDream(request);
            dreamService.saveDream(dreamModelToSave);
            stream.ack(groupName, messageId); // 처리 성공 시 ACK
            log.info("꿈 해석 결과 DB 저장 및 ACK 성공 (메시지 ID: {})", messageId);
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생 (메시지 ID: {}), ACK하지 않음. 나중에 재처리됩니다.", messageId, e);
            // ACK를 보내지 않으면 메시지는 PEL에 남아있게 됨
        }
    }

    private SaveDreamRequest buildRequestFromMessage(Map<String, String> message)
        throws com.fasterxml.jackson.core.JsonProcessingException {
        List<DreamType> dreamTypes = objectMapper.readValue(
            message.get("dreamTypes"),
            new TypeReference<>() {
            }
        );

        return SaveDreamRequest.builder()
                .userId(Long.parseLong(message.get("userId")))
                .dreamContent(message.get("dreamContent"))
                .interpretationResult(message.get("interpretationResult"))
                .dreamEmotion(DreamEmotion.valueOf(message.get("dreamEmotion")))
                .aiStyle(AiStyle.valueOf(message.get("aiStyle")))
                .dreamTypes(dreamTypes)
                .build();
    }
}
