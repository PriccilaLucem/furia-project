# Fúria - Sistema de Coleta de Dados de Pessoas

O **Fúria** é um sistema projetado para coletar dados de pessoas e permitir que um administrador visualize essas informações de forma organizada e eficiente.

## Tecnologias

- **Frontend**: React + Vite
- **Backend**: Spring Boot (Java)
- **Banco de Dados**: MySQL
- **Docker**: Para orquestrar os containers de Frontend e Backend.

## Pré-requisitos

Antes de rodar a aplicação, você precisará ter as seguintes ferramentas instaladas:

- **Docker**: Para executar os containers (Frontend e Backend)
- **Docker Compose**: Para gerenciar os containers de forma orquestrada
- **Java 17+** (caso queira rodar o backend manualmente sem Docker)

## Estrutura do Projeto

- /frontend # Código do frontend (React + Vite)
- /backend # Código do backend (Spring Boot)
- docker-compose.yaml # Orquestração de containers
- Makefile # Automação para rodar os containers
- README.md # Este arquivo


## Como rodar o projeto

### 1. **Usando Docker Compose (Recomendado)**

Para facilitar a execução do projeto, você pode usar o **Docker Compose**, que orquestra os containers do frontend e backend. Siga os passos abaixo:

1. Clone o repositório:
    ```bash
    git clone https://github.com/PriccilaLucem/furia-project.git
    cd fury-project
    ```

2. Verifique se o **Docker** está instalado corretamente:
    ```bash
    docker --version
    docker-compose --version
    ```

3. Suba os containers com o comando:
    ```bash
    make up
    ```
   Isso irá iniciar tanto o backend quanto o frontend em containers Docker.

4. Acesse a aplicação:
    - **Frontend**: [http://localhost:5173](http://localhost:5173)
    - **Backend**: [http://localhost:8080](http://localhost:8080)

### 2. **Rodando o Backend Separado (Sem Docker)**

Se preferir rodar o backend diretamente, siga as etapas abaixo:

1. Navegue até a pasta do backend:
    ```bash
    cd backend
    ```

2. Compile e execute a aplicação Spring Boot com Maven ou Gradle:

   - **Maven**:
     ```bash
     ./mvnw spring-boot:run
     ```
   
   - **Gradle**:
     ```bash
     ./gradlew bootRun
     ```

3. O backend estará rodando em `http://localhost:8080`.

### 3. **Rodando o Frontend Separado (Sem Docker)**

Se preferir rodar o frontend separadamente:

1. Navegue até a pasta do frontend:
    ```bash
    cd frontend
    ```

2. Instale as dependências:
    ```bash
    npm install
    ```

3. Inicie o servidor de desenvolvimento:
    ```bash
    npm run dev
    ```

4. O frontend estará disponível em [http://localhost:5173](http://localhost:5173).

## Configuração de CORS

A aplicação backend está configurada para aceitar requisições do frontend rodando em `http://localhost:5173`. Caso esteja utilizando outra porta, modifique a configuração de CORS no arquivo de configuração Spring Boot.

## Arquitetura

### Backend (Spring Boot)
O backend é responsável por gerenciar os dados das pessoas e fornecer as APIs necessárias para o frontend. Ele utiliza uma arquitetura RESTful para interação com o cliente.

### Frontend (React + Vite)
O frontend foi desenvolvido utilizando **React** e **Vite** para um desempenho rápido. Ele se comunica com o backend para exibir e registrar as informações das pessoas.

## Troubleshooting

- **Problema ao conectar ao backend**:
    - Verifique se o backend está rodando corretamente.
    - Caso esteja utilizando Docker, verifique os logs com:
      ```bash
      docker-compose logs backend
      ```

- **Erro `Network Error` no Axios**:
    - Verifique se o backend está rodando e escutando na porta correta.
    - Verifique a URL da API no frontend.

- **Problema de CORS**:
    - Caso o frontend não consiga acessar o backend, verifique as configurações de CORS no backend.

## Licença

Este projeto está sob a licença MIT. Consulte o arquivo LICENSE para mais detalhes.

