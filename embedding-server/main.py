from fastapi import FastAPI
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer
import uvicorn

# 애플리케이션 초기화 -> 서버 켜질떄 한번 실행되는애임
app = FastAPI()
# 모델 로딩
model = SentenceTransformer('jhgan/ko-sbert-nli')

# 요청 본문 데이터모델 정의
class TextIn(BaseModel):
    text: str

# post요청 보내기 예쁘게생겻네
@app.post("/encode")
def encode_text(request: TextIn):
    # 모델 -> 입력 텍스트 -> 벡터
    vector = model.encode(request.text)
    return {"vector": vector.tolist()}

# 직접 실행용인데 uvicorn 명령어를 사용하면 필요없다
if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
