# 🧩 Zaya API — Backend da Plataforma de Gestão Estética

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/Security-JWT-green)](https://jwt.io/)
[![License](https://img.shields.io/badge/license-MIT-lightgrey)](LICENSE)

---

## 📖 Sobre o projeto

A **Zaya API** é o backend da plataforma de gestão da **Clínica Zaya Estética Avançada**, desenvolvida em **Java 21 + Spring Boot 3.3.4** com autenticação segura via **JWT**, persistência em **PostgreSQL**, e deploy em ambiente **Docker**.

O projeto será desenvolvido de forma modular, dividido em **sprints temáticas**, cada uma entregando um conjunto funcional completo.

---

## ⚙️ Tecnologias principais

- **Java 21**
- **Spring Boot 3.3.4**
- **Spring Security 6**
- **Spring Data JPA**
- **PostgreSQL 16**
- **Lombok**
- **JWT (io.jsonwebtoken 0.11.5)**
- **Docker / Docker Compose**
- **GitHub Actions (CI/CD)**

---

## 🧭 Estrutura do projeto

zaya-api/
├── src/main/java/com/zaya/api/
│ ├── model/ # Entidades (User, Role)
│ ├── dto/ # Data Transfer Objects
│ ├── repository/ # Repositórios JPA
│ ├── service/ # Lógica de negócios
│ ├── controller/ # Endpoints REST
│ ├── config/ # Segurança, JWT e Seeds
│ └── ZayaApiApplication.java
│
├── infra/db/ # Docker Compose e scripts de banco
├── docs/ # Documentações de cada sprint
├── pom.xml # Dependências Maven
└── README.md


---

## 🚀 Execução local

```bash
# Clonar repositório
git clone https://github.com/fabio2543/zaya-api.git
cd zaya-api

🧰 Fluxo de versionamento (Git)
📦 Inicialização do projeto

# Configuração inicial
git init -b main
git remote add origin git@github.com:fabio2543/zaya-api.git

# Confirmar configuração
git remote -v

# Criar branch de desenvolvimento
git checkout -b feature/sprint1-user-module

# Verificar status
git status

# Adicionar e commitar alterações
git add .
git commit -m "feat: implementação do módulo de usuários e autenticação"

# Voltar para main
git checkout main

# Atualizar branch principal
git pull origin main

# Fazer merge da branch de feature
git merge feature/sprint1-user-module

# Resolver conflitos (se houver), depois commitar o merge
git commit -am "merge: integração do módulo de usuários na main"

# Enviar atualização final para o GitHub
git push origin main

# Criar tag de versão
git tag -a v0.1.0 -m "Release Sprint 1 - Autenticação e Usuários"

# Enviar tag para o repositório remoto
git push origin v0.1.0


# Subir banco PostgreSQL
cd infra/db
docker compose up -d

# Rodar API
cd dev/zaya-api/
mvn clean install
mvn spring-boot:run

Aplicação local:
👉 http://127.0.0.1:8081

| Sprint | Módulo                           | Status          | Documentação                                                   |
| ------ | -------------------------------- | --------------- | -------------------------------------------------------------- |
| **1**  | Autenticação e Usuários          | ✅ Concluída     | [docs/Auth_Usuarios.md](docs/Auth_Usuarios.md)               |

👨‍💻 Autor

Fabio Junior

Product Owner e Desenvolvedor da Clínica Zaya Estética Avançada
Projeto com foco em escalabilidade, segurança e integração multiplataforma.