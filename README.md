Artists API – Projeto Full Stack (Java + React)

Este projeto foi desenvolvido como parte de um processo seletivo, com o objetivo de demonstrar conhecimento em Java com Spring Boot, construção de APIs REST seguras, integração com serviços externos, Docker, e um front-end em React consumindo a API.
A aplicação permite gerenciar artistas, álbuns e capas de álbuns, com upload de imagens no MinIO, autenticação JWT, versionamento de endpoints e execução completa via Docker Compose.

🛠 Tecnologias Utilizadas
Back-end
Java 17
Spring Boot
Spring Web
Spring Security (JWT)
Spring Data JPA
PostgreSQL
Flyway (migrations)
MinIO (API compatível com S3)
Swagger / OpenAPI
Spring Actuator (Health, Liveness, Readiness)
WebSocket
Rate Limit
Docker & Docker Compose

Front-end
React + TypeScript
Vite
Axios
TailwindCSS
React Router

📦 Arquitetura Geral
API REST desenvolvida em Spring Boot
Banco de dados PostgreSQL
Armazenamento de imagens no MinIO
Front-end em React consumindo a API
Containers orquestrados via docker-compose
Autenticação JWT com expiração e renovação
Relacionamento N:N entre Artistas e Álbuns
Endpoints versionados (/api/v1)

▶️ Como Executar o Projeto
Pré-requisitos
Docker
Docker Compose

Passos
Na raiz do projeto:

docker compose down -v
docker compose up --build

Após subir os containers:

API: http://localhost:8080
Swagger: http://localhost:8080/swagger-ui/index.html
Front-end: http://localhost:3000
MinIO Console: http://localhost:9001

Credenciais do MinIO:
Usuário: minioadmin
Senha: minioadmin

🔐 Autenticação (JWT)
A aplicação utiliza JWT para proteger os endpoints.

Login
POST /api/v1/auth/login

Payload de exemplo:
{
"username": "admin",
"password": "admin123"
}


Resposta:
{
"accessToken": "token..."
}


O accessToken deve ser enviado no header:

Authorization: Bearer {token}
O refresh token não foi exposto nesta versão do front-end, pois o edital não exige obrigatoriamente o fluxo completo no cliente. A API já está preparada para evolução.

📚 Documentação da API (Swagger)
A documentação completa dos endpoints está disponível em:

👉 http://localhost:8080/swagger-ui/index.html
O Swagger já está configurado com botão Authorize, permitindo informar o token JWT e testar os endpoints protegidos.

🎤 Endpoints Principais
Artistas

GET /api/v1/artists
Lista artistas (paginação, filtro por nome e ordenação ASC/DESC).

GET /api/v1/artists/{id}
Busca artista por ID.

POST /api/v1/artists
Cria novo artista.

PUT /api/v1/artists/{id}
Atualiza artista.

Álbuns

GET /api/v1/albums
Lista álbuns (com paginação e filtro por artista).

GET /api/v1/albums/{id}
Busca álbum por ID.

POST /api/v1/albums
Cria álbum e associa a um artista.

PUT /api/v1/albums/{id}
Atualiza álbum.

Capas de Álbum (Upload)

POST /api/v1/albums/{id}/covers

Permite upload de uma ou mais imagens para o álbum.

As imagens são:
Armazenadas no MinIO
Vinculadas ao álbum no banco
Recuperadas via URL pré-assinada (presigned URL)

🖼 Presigned URL (MinIO)
As capas dos álbuns são retornadas como links temporários, com expiração de 30 minutos, garantindo segurança no acesso aos arquivos.
Esse mecanismo evita expor o bucket publicamente e é compatível com cenários de produção.

🌍 Regionais
Foi implementado o endpoint de Regionais, conforme solicitado no edital:
Importação de dados externos
Persistência em tabela própria
Controle de ativo/inativo
Sincronização incremental (inserção, inativação e versionamento)

❤️ Health, Liveness e Readiness
Disponíveis via Spring Actuator:

/actuator/health
/actuator/health/liveness
/actuator/health/readiness

Esses endpoints permitem verificar:
Se a aplicação está viva
Se está pronta para receber tráfego
Se dependências como banco estão funcionando

🚦 Rate Limit
Foi implementado rate limit de requisições por usuário, evitando abuso da API e atendendo aos requisitos do edital.

🧪 Testes
Testes unitários não foram implementados nesta versão devido ao tempo disponível.
Essa decisão foi consciente e documentada, priorizando a entrega completa dos requisitos funcionais e arquiteturais solicitados.

🖥 Front-end (React)
O front-end foi desenvolvido em React + TypeScript, consumindo a API.
Funcionalidades implementadas:
Tela de login com JWT
Listagem de artistas com paginação e ordenação
Detalhe do artista com listagem de álbuns e capas
Cadastro e edição de artistas
Cadastro e edição de álbuns
Upload de capas de álbuns
Layout responsivo com TailwindCSS

O front foi integrado ao docker-compose, rodando como container junto com API, banco e MinIO.

📌 Observações Finais
O projeto foi desenvolvido pensando em legibilidade, organização e facilidade de evolução.
Commits foram feitos de forma incremental.
Onde algo não foi aprofundado, a decisão foi consciente e priorizada conforme o edital.

👨‍💻 Autor
Flávio Rosa Nicanor de Souza
Projeto desenvolvido para processo seletivo – Java / Backend / Full Stack.

🔗 Repositório:
https://github.com/flavionicanor/flaviorosanicanordesouza701140
