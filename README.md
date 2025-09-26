# 🏋️‍♂️ Sistema de Automação de Academia

📝 Projeto desenvolvido com o objetivo de automatizar o gerenciamento de uma academia, oferecendo funcionalidades completas para professores e alunos. Criado como parte da minha formação em desenvolvimento back-end com **Spring Boot**, este sistema simula um ambiente real de controle de treinos e avaliações físicas, com foco em organização, escalabilidade e boas práticas de desenvolvimento.

---

## 📌 Descrição

O sistema permite que **professores** realizem o cadastro, edição e listagem de alunos, criem fichas de treinamento e avaliações físicas, além de gerenciar essas informações com facilidade. Já os **alunos** têm acesso restrito ao sistema, podendo visualizar suas fichas de treino e avaliações físicas de forma prática e intuitiva.

A aplicação foi construída com **Java 17** e **Spring Boot**, utilizando a IDE **Spring Tools for Eclipse**. A estrutura do projeto foi gerada via **Spring Initializr**, com gerenciamento de dependências pelo **Maven**, e organizada em pacotes específicos para cada camada da aplicação.

---

## 🚀 Objetivo

Automatizar processos internos de academias, promovendo uma experiência mais eficiente para professores e alunos. O projeto também tem como finalidade consolidar conhecimentos em:

- APIs RESTful com Spring Boot  
- Arquitetura em camadas  
- Persistência de dados com JPA  
- Injeção de dependência  
- Controle de acesso por perfil  

---

## 💻 Tecnologias Utilizadas

- Java 17  
- Spring Boot
- Spring Security
- Maven  
- JPA / Hibernate  
- Spring Tools for Eclipse  
- REST API  
- Spring Web  
- Spring Data JPA  
- Spring Initializr
- Mysql

---

## 🔧 Ferramentas

- **Spring Initializr** – estruturação do projeto  
- **Postman** – testes de API  
- **Spring Tools for Eclipse** – ambiente de desenvolvimento  
- **GitHub** – versionamento e hospedagem do código  

---

## 📂 Estrutura do Projeto

Organizado em pacotes seguindo boas práticas de arquitetura:

- src/ 
- ├── controllers/ # Camada de controle (API REST) 
- │ ├── AlunoController.java 
- │ ├── FichaTreinoController.java 
- │ └── AvaliacaoFisicaController.java 
- │ └── DashboardController.java 
- │ └── LoginController.java 
- │ └── SuporteController.java 
- │ └── TreinoController.java 
- │ └── UsuarioController.java 
- │
- ├── entities/ # Entidades JPA 
- │ ├── Aluno.java 
- │ ├── FichaTreino.java 
- │ └── AvaliacaoFisica.java 
- │ └── Exercicios.java 
- │ └── MensagemSuporte.java 
- │ └── Perfil.java
- │ └── Treino.java
- │ └── Usuario.java 
- │  
- ├── repository/ # Interfaces de persistência 
- │ ├── AlunoRepository.java 
- │ ├── FichaDeTreinamentoRepository.java 
- │ └── AvaliacaoFisicaRepository.java 
- │ └── AvaliacaoFisicaRepository.java 
- │ └── ExercicioRepository.java 
- │ └── TreinoRepository.java
- │ └── UsuarioRepository.java 
- │ 
- ├── services/ # Regras de negócio 
- │ ├── UsuarioService.java 
- │ 
- ├── Security/ # Regras de negócio 
- │ ├── SecurityConfig.java 
- │ ├── UsuarioDetalhes.java 



---

## 🔗 Funcionalidades

### ⚙️ Admin

- Gerencia todo o sistema na palma da sua mão, tem acesso a todos os campos e mais alguns exclusivo para quem é administrador.

### 👨‍🏫 Professor

- Cadastrar aluno  
- Listar aluno  
- Editar aluno  
- Criar ficha de treinamento  
- Editar ficha de treinamento  
- Listar ficha de treinamento  
- Excluir ficha de treinamento  
- Cadastrar avaliação física  
- Editar avaliação física  
- Listar avaliação física  
- Excluir avaliação física  

### 🧍‍♂️ Aluno

- Visualizar ficha de treinamento  
- Visualizar avaliação física  

---

## 📡 Endpoints REST

> Os endpoints seguem o padrão REST e estão organizados por entidade. Exemplos:

### Aluno

- `GET /aluno` – Lista todos os alunos  
- `POST /aluno` – Cadastra novo aluno  
- `PUT /aluno/{id}` – Edita dados do aluno  
- `DELETE /aluno/{id}` – Remove aluno  

### Ficha de Treinamento

- `GET /ficha` – Lista todas as fichas  
- `POST /ficha` – Cria nova ficha  
- `PUT /ficha/{id}` – Edita ficha existente  
- `DELETE /ficha/{id}` – Exclui ficha  

### Avaliação Física

- `GET /avaliacao` – Lista todas as avaliações  
- `POST /avaliacao` – Cadastra nova avaliação  
- `PUT /avaliacao/{id}` – Edita avaliação existente  
- `DELETE /avaliacao/{id}` – Exclui avaliação  

---

## 🛠️ Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   
2. Importe o projeto na IDE Spring Tools for Eclipse
3. Execute como Spring Boot App
4. Teste os endpoints com ferramentas como Postman ou Insomnia

## 📚 Aprendizados

- Criação de APIs RESTful com Spring Boot

- Organização de camadas e responsabilidades

- Persistência com JPA e Hibernate

- Separação de funcionalidades por perfil (admin/professor/aluno)

- Boas práticas de versionamento com Git

## 🔐 Certificado de Autoria

- Este projeto foi criado e desenvolvido por Lucas Augusto - cnataofps como parte da formação em desenvolvimento back-end. Todos os direitos reservados. Caso utilize partes do código, por favor, mencione a autoria original. 🚀

