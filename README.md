<!--
# Dream Decoder

This project is for providing an `AI-based dream interpretation service`.  
It solves the problem of `subjective and inconsistent dream explanations` by combining `OpenAI GPT-based natural language interpretation` with `structured emotional and symbolic data` from user inputs.


## Key Focus Areas
- I acheived: Building an **end-to-end AI interpretation pipeline** using caching, message queues, and vector search.
- I provide: **Fast, consistent, and contextual dream interpretations** powered by AI and efficient caching.



## Features I Implemented
- **Dream Interpretation Pipeline**: Integrated OpenAI API with structured prompts to generate dream interpretations.
- **Caching Layer**: Implemented **Caffeine (L1)** and **Redis (L2)** caching to reduce redundant API calls.
- **Vector Search**: Connected **Qdrant** vector DB to retrieve similar dreams based on semantic embeddings.
- **Asynchronous Processing**: Used **Redis Streams** and **Redisson consumers** to handle interpretation jobs non-blockingly.
- **Performance**: Reduced average response time by optimizing cache hit ratio and async processing.
- **Monitoring**: Configured **Prometheus + Grafana** via Docker Compose for live metrics tracking.



## Run My Project Locally

Here's how I run it on my machine:
```bash
$ git clone 
$ docker-compose up -d
```
Visit `http://localhost:8080` to see it working



## My Tech Stack

**Backend:**  
Java · Spring Boot · Spock (Groovy)

**Database & Cache:**  
MySQL · Redis · Caffeine · Qdrant

**AI & Integration:**  
OpenAI GPT · Python FastAPI (Embedding Service)

**DevOps:**  
Docker · Prometheus · Grafana
-->
