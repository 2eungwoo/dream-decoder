import logging
from sentence_transformers import SentenceTransformer
from config import MODEL_NAME
from typing import List
import numpy as np

# INFO 레벨 이상의 로그를 출력하도록 기본 로거 설정함
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
log = logging.getLogger(__name__)

# 모듈 임포트 시 모델을 한 번만 로드함
_model = SentenceTransformer(MODEL_NAME)


def encode_text(text: str) -> List[float]:
    """텍스트 문자열을 벡터로 인코딩함"""
    vector_np = _model.encode(text)
    # 생성된 벡터의 차원과 앞 5개 요소를 INFO 로그로 출력함
    log.info(f"벡터 생성 완료, 차원: {vector_np.shape}, 앞 5개 요소: {vector_np[:5]}")
    return vector_np.tolist()
