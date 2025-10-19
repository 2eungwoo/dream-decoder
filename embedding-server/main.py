import uvicorn
from fastapi import FastAPI
from pydantic import BaseModel, Field
import logging

import model
import vector_db
from config import SIMILARITY_THRESHOLD

# --- 로깅 설정 ---
log = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')

# --- FastAPI 앱 초기화 ---
app = FastAPI()

# --- API용 Pydantic 모델 ---
class AddRequest(BaseModel):
    dream_id: int = Field(..., description="꿈의 고유 식별자")
    text: str = Field(..., description="벡터화하여 저장할 꿈의 내용")
    style: str = Field(..., description="응답 스타일 (MBTI)")
    emotion: str = Field(..., description="꿈에 대한 감정")

class SearchRequest(BaseModel):
    text: str = Field(..., description="유사한 항목을 검색할 꿈의 내용")
    style: str = Field(..., description="응답 스타일 (MBTI)")
    emotion: str = Field(..., description="꿈에 대한 감정")

class SearchResponse(BaseModel):
    dream_id: int | None = Field(None, description="찾은 가장 유사한 꿈의 ID")
    score: float | None = Field(None, description="찾은 꿈의 유사도 점수")

# --- 시작 이벤트 ---
@app.on_event("startup")
def startup_event():
    """시작 시 벡터 데이터베이스를 초기화함"""
    vector_db.init_db()

# --- API 엔드포인트 ---
@app.post("/add", status_code=201)
def add_dream_vector(request: AddRequest):
    """꿈 텍스트를 인코딩하고 벡터 데이터베이스에 메타데이터와 함께 추가함"""
    log.info(f"'/add' 엔드포인트 수신, 꿈 ID: {request.dream_id}")
    vector = model.encode_text(request.text)
    log.info(f"꿈 ID: {request.dream_id} 벡터 생성 완료, DB 추가 시작함")

    vector_db.add_vector(
        dream_id=request.dream_id,
        vector=vector,
        style=request.style,
        emotion=request.emotion
    )
    log.info(f"꿈 ID: {request.dream_id} DB 추가 완료됨")
    return {"message": f"꿈 ID: {request.dream_id} 추가 성공"}

@app.post("/search", response_model=SearchResponse)
def search_similar_dreams(request: SearchRequest):
    """꿈 텍스트를 인코딩하고, 스타일과 감정으로 필터링하여 유사한 꿈을 검색함"""
    query_vector = model.encode_text(request.text)
    top_hit = vector_db.search_vector(
        vector=query_vector,
        style=request.style,
        emotion=request.emotion
    )

    if top_hit and top_hit.score >= SIMILARITY_THRESHOLD:
        return SearchResponse(dream_id=top_hit.payload.get("dream_id"), score=top_hit.score)

    return SearchResponse(dream_id=None, score=None)

# --- 메인 실행 ---
if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
