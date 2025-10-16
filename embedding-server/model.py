from sentence_transformers import SentenceTransformer
from .config import MODEL_NAME
from typing import List

# 모듈 임포트 시 모델을 한 번만 로드함
_model = SentenceTransformer(MODEL_NAME)

def encode_text(text: str) -> List[float]:
    """텍스트 문자열을 벡터로 인코딩함"""
    return _model.encode(text).tolist()