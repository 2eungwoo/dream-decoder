FROM python:3.10-slim

WORKDIR /app

COPY requirements.txt .

# dependencies 설치
RUN pip install --no-cache-dir -r requirements.txt

# torch 버전을 강제 업그레이드 (sentence-transformers가 낮은 버전 설치하는 경우 대비)
RUN pip install --no-cache-dir --upgrade "torch>=2.6.0"

COPY . .

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
