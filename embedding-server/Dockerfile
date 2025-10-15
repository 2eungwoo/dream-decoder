FROM python:3.10-slim

WORKDIR /app

COPY requirements.txt .

# 라이브러리 설치 (PyTorch CPU 버전을 명시하면 용량 최적화할수있다고 함)
RUN pip install --no-cache-dir torch --index-url https://download.pytorch.org/whl/cpu
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
