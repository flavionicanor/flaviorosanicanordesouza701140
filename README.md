🎵 Artists API – Projeto Concurso Java

Este projeto foi desenvolvido como parte de um processo seletivo, com o objetivo de demonstrar experiência em Java + Spring Boot, construção de APIs REST, segurança, integração com serviços externos e organização de um projeto pronto para evoluir.

A aplicação gerencia artistas, álbuns e capas de álbuns, com upload de imagens no MinIO, autenticação via JWT, versionamento de endpoints e execução completa via Docker.

🛠 Tecnologias utilizadas

Java 17

Spring Boot

Spring Web

Spring Security (JWT)

Spring Data JPA

PostgreSQL

Flyway (migrations)

MinIO (armazenamento S3)

Swagger / OpenAPI

Docker & Docker Compose

WebSocket (notificação de novos álbuns)

Spring Actuator (Health / Liveness / Readiness)

📦 Arquitetura geral

API REST desenvolvida em Spring Boot

Banco de dados PostgreSQL

Armazenamento de imagens no MinIO

Containers orquestrados via docker-compose

Autenticação JWT com expiração e refresh

Relacionamento N:N entre Artistas e Álbuns

Endpoints versionados (/api/v1)

▶️ Como executar o projeto
Pré-requisitos

Docker

Docker Compose

Passos
docker compose down -v
docker compose up --build


Após subir os containers, os serviços estarão disponíveis em:

API: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui

MinIO Console: http://localhost:9001

Credenciais do MinIO:

Usuário: minioadmin

Senha: minioadmin

🔐 Autenticação (JWT)

A API utiliza autenticação baseada em JWT.

Login

POST /api/v1/auth/login

Exemplo de payload:

{
"username": "admin",
"password": "admin123"
}


Resposta:

{
"accessToken": "token..."
}

Refresh Token

POST /api/v1/auth/refresh

📚 Documentação da API (Swagger)

A documentação interativa dos endpoints pode ser acessada em:

👉 http://localhost:8080/swagger-ui

O Swagger já está configurado com o botão Authorize, permitindo informar o token JWT e testar os endpoints protegidos diretamente pela interface.

🎤 Endpoints principais
🎵 Artistas

GET /api/v1/artists
Lista artistas com paginação, filtro por nome e ordenação.

GET /api/v1/artists/{id}
Busca artista por ID.

POST /api/v1/artists
Cria um novo artista.

PUT /api/v1/artists/{id}
Atualiza um artista existente.

💿 Álbuns

GET /api/v1/albums
Lista álbuns com paginação.

GET /api/v1/albums/{id}
Busca álbum por ID.

POST /api/v1/albums
Cria um álbum e associa a um ou mais artistas.

PUT /api/v1/albums/{id}
Atualiza os dados de um álbum.

🖼 Capas de Álbum (Upload)

POST /api/v1/albums/{id}/covers

Permite o upload de uma ou mais imagens de capa para um álbum.

As imagens são:

Armazenadas no MinIO

Vinculadas ao álbum no banco de dados

Recuperadas por meio de URLs pré-assinadas

🖼 Presigned URL (MinIO)

As capas dos álbuns são retornadas como links temporários, gerados via presigned URL do MinIO, com expiração de 30 minutos.

Isso garante que os arquivos fiquem protegidos e não expostos publicamente.

🌍 Regionais

Foi implementado o endpoint de Regionais, conforme solicitado no edital, incluindo:

Importação de dados a partir de um serviço externo

Persistência em tabela própria

Controle de ativo/inativo

Atualização incremental conforme mudanças nos dados

❤️ Health, Liveness e Readiness

A aplicação disponibiliza endpoints de monitoramento via Spring Actuator:

/actuator/health

/actuator/health/liveness

/actuator/health/readiness

Esses endpoints permitem verificar se:

A aplicação está ativa

Está pronta para receber tráfego

Dependências como banco de dados estão funcionando corretamente

🚦 Rate Limit

Foi implementado rate limit de 10 requisições por minuto por usuário, evitando abuso da API e simulando um cenário mais próximo de produção.

🧪 Testes

Testes unitários não foram implementados nesta versão devido ao tempo disponível para o desafio.

Essa foi uma decisão consciente, priorizando a entrega completa dos requisitos funcionais descritos no edital, e está documentada de forma transparente.

📌 Observações finais

O projeto foi desenvolvido pensando em legibilidade, organização e facilidade de evolução.

As decisões técnicas foram feitas buscando simplicidade e aderência ao edital.

Os commits foram realizados de forma incremental, acompanhando a evolução do projeto.

Pontos não aprofundados foram escolhas conscientes para priorizar os itens mais relevantes.

👨‍💻 Autor

Flávio Rosa Nicanor de Souza
Projeto desenvolvido para processo seletivo – Backend Java.

Repositório do projeto:
👉 https://github.com/flavionicanor/flaviorosanicanordesouza701140