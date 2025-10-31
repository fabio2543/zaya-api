
---

## 📄 **docs/Sprint1_Auth_Usuarios.md**

Salve este arquivo em:  
`~/dev/zaya-api/docs/Sprint1_Auth_Usuarios.md`

```markdown
# 🧩 Sprint 1 — Autenticação e Usuários

---

## 🎯 Objetivo

Criar o módulo de **autenticação e controle de usuários** da Zaya API, com:
- Registro e login de usuários;
- Geração e validação de tokens JWT com expiração configurável;
- Controle de acesso via perfis (`ADMIN` e `USER`);
- CRUD completo de usuários (restrito a ADMIN).

---

## 📅 Duração

**2 semanas (14 dias)**  
Início: `2025-10-14`  
Término previsto: `2025-10-28`

---

## ⚙️ Backlog da Sprint 1

| Nº | Atividade | Descrição | Status |
|----|------------|------------|:------:|
| 1 | Configurar ambiente Spring Boot + PostgreSQL | Projeto base + banco via Docker | ✅ |
| 2 | Criar `.gitignore`, `.editorconfig`, CI (GitHub Actions) | Padronização e pipeline | ✅ |
| 3 | Criar entidade `User` e `UserRepository` | Estrutura e persistência de usuários | ✅ |
| 4 | Criar DTOs (`RegisterRequest`, `LoginRequest`, `AuthResponse`) | Transferência de dados segura | ✅ |
| 5 | Implementar `JwtService` | Geração e validação de tokens JWT | ✅ |
| 6 | Criar `JwtAuthenticationFilter` + `SecurityConfig` | Integração do token com Spring Security | ✅ |
| 7 | Implementar `AuthController` (`/auth/register`, `/auth/login`) | Endpoints de autenticação | ✅ |
| 8 | Testar autenticação (Postman + curl) | Validação do fluxo JWT completo | ✅ |
| 9 | Criar `UserController` (CRUD ADMIN) | Gestão de usuários restrita | ✅ |
| 10 | Documentar exemplos no README | Requisições e respostas completas | ✅ |
| 11 | Merge + release `v0.1.0` | Encerramento da sprint | ✅ |

---

## 🧩 Estrutura Técnica

- **Entidades:** `User`, `Role`
- **Repositório:** `UserRepository` (Spring Data JPA)
- **Serviços:** `JwtService`, `UserService`
- **Configurações:** `SecurityConfig`, `JwtAuthenticationFilter`, `MethodSecurityConfig`
- **Controllers:** `AuthController`, `UserController`
- **DTOs:** `RegisterRequest`, `LoginRequest`, `AuthResponse`, `UserCreateRequest`, `UserUpdateRequest`, `UserResponse`

---

## 🧱 Entidade `User`

```java
@Entity
@Table(name = "users")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nome;

  @Column(unique = true, nullable = false)
  private String email;

  private String senha;

  @Enumerated(EnumType.STRING)
  private Role role; // ADMIN ou USER
}

🔐 Autenticação JWT
Serviço JwtService

Responsável por gerar e validar tokens:

@Service
public class JwtService {

  @Value("${zaya.jwt.secret}")
  private String secret;

  @Value("${zaya.jwt.expiration-ms}")
  private long expirationMs;

  private Key getSignKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(String email) {
    return Jwts.builder()
      .setSubject(email)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
      .signWith(getSignKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public boolean isTokenValid(String token, String userEmail) {
    final String username = extractClaim(token, Claims::getSubject);
    return username.equals(userEmail) && !isTokenExpired(token);
  }
}

⚙️ Filtro de autenticação (JWT)

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
          throws ServletException, IOException {

      String header = req.getHeader(HttpHeaders.AUTHORIZATION);
      if (header == null || !header.startsWith("Bearer ")) {
          chain.doFilter(req, res);
          return;
      }

      String token = header.substring(7);
      String email = jwtService.extractUsername(token);

      if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          var user = userRepository.findByEmail(email).orElse(null);
          if (user != null && jwtService.isTokenValid(token, email)) {
              var auth = new UsernamePasswordAuthenticationToken(
                      email,
                      null,
                      List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
              );
              SecurityContextHolder.getContext().setAuthentication(auth);
          }
      }
      chain.doFilter(req, res);
  }
}


| Método   | Endpoint         | Acesso  | Descrição         |
| -------- | ---------------- | ------- | ----------------- |
| `POST`   | `/auth/register` | Público | Cria novo usuário |
| `POST`   | `/auth/login`    | Público | Retorna token JWT |
| `GET`    | `/usuarios`      | ADMIN   | Lista usuários    |
| `POST`   | `/usuarios`      | ADMIN   | Cria usuário      |
| `PUT`    | `/usuarios/{id}` | ADMIN   | Atualiza usuário  |
| `DELETE` | `/usuarios/{id}` | ADMIN   | Exclui usuário    |


📘 Exemplos de requisição e resposta
Registro (/auth/register)

Request

{
  "nome": "Fabio",
  "email": "fabio@zaya.com",
  "senha": "Zaya@123"
}

Response

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5..."
}

Login (/auth/login)

Request

{
  "email": "fabio@zaya.com",
  "senha": "Zaya@123"
}

Response

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5..."
}

Listar usuários (/usuarios)

Header

Authorization: Bearer eyJhbGciOiJIUzI1NiIs...

Response

{
  "content": [
    {
      "id": 1,
      "nome": "Admin",
      "email": "admin@zaya.com",
      "role": "ADMIN"
    },
    {
      "id": 2,
      "nome": "Fabio",
      "email": "fabio@zaya.com",
      "role": "USER"
    }
  ]
}

🧪 Testes no Postman

Ambiente: base_url = http://127.0.0.1:8081

Variável {{token}} salva automaticamente após login.

Headers:

Content-Type: application/json
Authorization: Bearer {{token}}

✅ Resultados da Sprint

Autenticação JWT funcional com expiração configurável.

Senhas criptografadas com BCrypt.

CRUD de usuários protegido por Role.ADMIN.

CI/CD configurado (build + test).

Banco de dados PostgreSQL via Docker.

Postman Collection documentada.

🔗 Próximos passos (Sprint 2)

Implementar Custos Fixos e Procedimentos.

Calcular margem e custo por serviço.

Criar endpoints /custos, /procedimentos.

Relacionar usuários e lançamentos financeiros.

📦 Release

Versão: v0.1.0
Status: ✅ Entregue
Tag Git: release/v0.1.0