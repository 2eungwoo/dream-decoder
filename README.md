# Dream Decoder
> <!> project is still proceeding 

> is an LLM (OpenAI)-based service that provides meaningful interpretations by intelligently analyzing user-inputted dream content, focusing on complex symbol combinations. 
> 
> It aims to unlock the full potential of dreams by offering high-quality interpretations that consider the context of the user's free-form input, rather than one-dimensional analyses based solely on individual symbols.

<br/>

> ### 🚧 **Under Refactoring** 🚧
>
> This project is currently undergoing a significant refactoring effort, transitioning from a **Web-based application** to a **pure Java Shell (CLI) application.**
>
> As a result, **most features may not be fully functional at this time**, and the project structure and codebase are continuously evolving. Our ultimate goal is to achieve a lighter and more scalable architecture.

<br/>

### ✨ Planned Core Features

-   **Dream Logging and Management**: Users can record and manage their dream content as text.
-   **AI-Powered Dream Analysis**: AI provides analysis of dream symbols, atmosphere, and potential interpretations based on recorded dream content.
-   **Key Symbol Extraction**: Automatically extracts important keywords and symbols from dream narratives.
-   **User-Friendly CLI**: Offers an intuitive command-line interface for all functionalities.

### 🏛️ Core Architecture

This project is designed to be both efficient and resilient, focusing on response time and cost-effectiveness when interacting with external LLMs.

-   **Asynchronous Processing with Redis Stream**: Utilizes a Redis Stream-based pipeline to decouple time-consuming LLM operations from the user-facing API response. This provides an immediate user experience by effectively eliminating perceived wait times for dream analysis.

-   **Intelligent Dual Caching**: To enhance cost-efficiency, the system employs a dual-caching architecture based on **semantic identity** (using embeddings) and **sentence similarity**. This minimizes redundant LLM API calls for similar free-form user inputs.

-   **Dual Request Control**: Implements a robust request control architecture using Redis (for preventing duplicate API calls) and in-memory Semaphores (for limiting concurrency). This dual-layered approach ensures cost stability and prevents system overload from external API integrations.

-   **Data-Loss Prevention**: A reprocessing mechanism based on the Redis Stream `ACK` protocol is in place to gracefully handle asynchronous processing failures. This ensures user data is not lost and prevents wasted API costs.

### 🚀 Running Locally (Coming Soon)

> As refactoring is currently in progress, stable build and execution instructions will be updated in due course.

### 🛠️ Tech Stack

-   **Language**: `Java 17`
-   **Framework**: `Spring Shell`, `Spring Boot`
-   **Persistence**: `JPA / Hibernate`, `QueryDSL`, `H2 / MySQL`
-   **Caching & Messaging**: `Redis`, `Redis Stream`
-   **Build**: `Gradle`
-   **External & Infra**: `OpenAI API`, `Vector DB` (for Embedding)