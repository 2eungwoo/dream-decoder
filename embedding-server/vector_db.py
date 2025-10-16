from qdrant_client import QdrantClient, models
from typing import List, Optional
from .config import QDRANT_HOST, QDRANT_PORT, COLLECTION_NAME, VECTOR_SIZE, DISTANCE_METRIC

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

def add_vector(dream_id: int, vector: List[float]):
    """꿈 벡터를 데이터베이스에 업서트함"""
    _client.upsert(
        collection_name=COLLECTION_NAME,
        points=[
            models.PointStruct(
                id=dream_id,
                vector=vector,
                payload={"dream_id": dream_id}
            )
        ],
        wait=True,
    )

def search_vector(vector: List[float]) -> Optional[models.ScoredPoint]:
    """데이터베이스에서 가장 유사한 벡터를 검색함"""
    search_results = _client.search(
        collection_name=COLLECTION_NAME,
        query_vector=vector,
        limit=1,
    )
    if not search_results:
        return None
    return search_results[0]