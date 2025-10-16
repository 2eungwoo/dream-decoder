import uvicorn
from fastapi import FastAPI
from pydantic import BaseModel, Field

from . import model, vector_db
from .config import SIMILARITY_THRESHOLD

# --- FastAPI 앱 초기화 ---
app = FastAPI()

# --- API용 Pydantic 모델 ---
class AddRequest(BaseModel):
    dream_id: int = Field(..., description="꿈의 고유 식별자")
    text: str = Field(..., description="벡터화하여 저장할 꿈의 내용")

class SearchRequest(BaseModel):
    text: str = Field(..., description="유사한 항목을 검색할 꿈의 내용")

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
    """꿈 텍스트를 인코딩하고 벡터 데이터베이스에 추가함"""
    vector = model.encode_text(request.text)
    vector_db.add_vector(dream_id=request.dream_id, vector=vector)
    return {"message": f"Dream with ID {request.dream_id} added successfully."}

@app.post("/search", response_model=SearchResponse)
def search_similar_dreams(request: SearchRequest):
    """꿈 텍스트를 인코딩하고 유사한 꿈을 검색함"""
    query_vector = model.encode_text(request.text)
    top_hit = vector_db.search_vector(query_vector)

    if top_hit and top_hit.score >= SIMILARITY_THRESHOLD:
        return SearchResponse(dream_id=top_hit.payload.get("dream_id"), score=top_hit.score)

    return SearchResponse(dream_id=None, score=None)

# --- 메인 실행 ---
if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)