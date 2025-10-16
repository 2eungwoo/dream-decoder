from qdrant_client import QdrantClient, models
from qdrant_client.http import models as rest_models
from typing import List, Optional
from config import QDRANT_HOST, QDRANT_PORT, COLLECTION_NAME, VECTOR_SIZE, DISTANCE_METRIC

# 클라이언트 초기화
_client = QdrantClient(host=QDRANT_HOST, port=QDRANT_PORT)

def init_db():
    """벡터 데이터베이스 컬렉션이 존재하지 않으면 초기화함"""
    try:
        _client.get_collection(collection_name=COLLECTION_NAME)
        print(f"컬렉션 '{COLLECTION_NAME}' 이미 존재함")
    except Exception:
        print(f"컬렉션 '{COLLECTION_NAME}' 찾을 수 없음, 새 컬렉션 생성함")
        _client.recreate_collection(
            collection_name=COLLECTION_NAME,
            vectors_config=models.VectorParams(size=VECTOR_SIZE, distance=DISTANCE_METRIC),
        )
        print(f"컬렉션 '{COLLECTION_NAME}' 성공적으로 생성됨")

def add_vector(dream_id: int, vector: List[float], style: str, emotion: str):
    """꿈 벡터와 메타데이터(스타일, 감정)를 데이터베이스에 업서트함"""
    _client.upsert(
        collection_name=COLLECTION_NAME,
        points=[
            models.PointStruct(
                id=dream_id,
                vector=vector,
                payload={
                    "dream_id": dream_id,
                    "style": style,
                    "emotion": emotion
                }
            )
        ],
        wait=True,
    )

def search_vector(vector: List[float], style: str, emotion: str) -> Optional[rest_models.ScoredPoint]:
    """스타일과 감정으로 필터링하여 데이터베이스에서 가장 유사한 벡터를 검색함"""
    search_results = _client.search(
        collection_name=COLLECTION_NAME,
        query_vector=vector,
        query_filter=rest_models.Filter(
            must=[
                rest_models.FieldCondition(
                    key="style",
                    match=rest_models.MatchValue(value=style),
                ),
                rest_models.FieldCondition(
                    key="emotion",
                    match=rest_models.MatchValue(value=emotion),
                ),
            ]
        ),
        limit=1,
    )
    if not search_results:
        return None
    return search_results[0]
