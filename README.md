# Integrantes

José Maia de Oliveira - 823395  
Enzo Yasumasa Hirotani - 823839  
Diogo Conforti Vaz Bellini - 823829

# UPeek

Aplicativo Android para descoberta e reserva de restaurantes, desenvolvido como projeto acadêmico na UFSCar (DC).

## Visão Geral

O UPeek permite que usuários encontrem restaurantes próximos, visualizem cardápios e realizem reservas de mesas diretamente pelo celular. O projeto é composto por um app Android nativo e um backend mock com json-server.

## Funcionalidades

- **Autenticação** — Login e cadastro de usuários (com validação local e persistência via Room)
- **Home** — Lista de restaurantes sugeridos e recentes com banners
- **Busca** — Pesquisa por nome e filtragem por categoria, preço médio, distância e avaliação
- **Restaurante** — Tela de detalhe com localização no mapa (OpenStreetMap), horários de funcionamento, mapa de mesas e cardápio
- **Reserva** — Fluxo completo de reserva (número de pessoas, data, horário, mesa) com tela de confirmação
- **Perfil** — Exibição dos dados do usuário e logout
- **Internacionalização** — Suporte a Português (padrão) e Inglês

## Tecnologias

### Mobile (Android)

| Tecnologia                    | Uso                                        |
| ----------------------------- | ------------------------------------------ |
| Kotlin + Jetpack Compose      | UI declarativa                             |
| Navigation Compose            | Navegação entre telas                      |
| ViewModel + StateFlow         | Gerenciamento de estado                    |
| Repository Pattern            | Camada de abstração entre VM e fontes de dados |
| Room                          | Persistência local do usuário              |
| Retrofit + Gson               | Consumo da API REST                        |
| osmdroid                      | Mapa OpenStreetMap                         |
| Coil                          | Carregamento de imagens                    |
| Google Play Services Location | Geolocalização                             |

- **minSdk:** 26 (Android 8.0)
- **targetSdk:** 36

### Backend

- **Node.js** com `json-server` (mock REST API na porta `3000`)
- Endpoint customizado de login (`POST /login`) com validação de credenciais

## Estrutura do Projeto

```
projeto-DevMobile/
├── mobile/          # App Android
│   └── app/src/main/java/br/dc/ufscar/devmobile/
│       ├── views/        # Telas (Composables de nível de rota)
│       ├── viewmodels/   # ViewModels por tela
│       ├── repositories/ # Camada de repositório (abstração de dados)
│       │   ├── StoreRepository.kt
│       │   ├── MenuRepository.kt
│       │   ├── ReservationRepository.kt
│       │   └── UserRepository.kt
│       ├── composables/  # Componentes reutilizáveis
│       ├── network/      # DTOs e cliente Retrofit
│       ├── entities/     # Entidades Room e modelos de domínio
│       ├── daos/         # DAOs Room
│       ├── configs/      # Database, converters, serviço de localização
│       └── Routes.kt     # Definição central de rotas de navegação
└── backend/         # Mock API
    ├── server.js
    └── db.json
```

## Como Rodar

### Backend

```bash
cd backend
npm install
npm run dev
```

### App Android

1. Abra a pasta `mobile/` no Android Studio
2. Execute em um emulador com Android 8.0+

> O app já está configurado para conectar ao backend via `http://10.0.2.2:3000` (endereço padrão do emulador Android para acessar o localhost da máquina).

## Telas e Rotas

| Rota                       | Tela                   |
| -------------------------- | ---------------------- |
| `login`                    | Login                  |
| `register`                 | Cadastro               |
| `home`                     | Home                   |
| `search`                   | Busca por categoria    |
| `filters?category=`        | Filtros avançados      |
| `searchResult?...`         | Resultados da busca    |
| `restaurantHome/{storeId}` | Detalhe do restaurante |
| `restaurantMenu/{storeId}` | Cardápio               |
| `reserve/{storeId}`        | Formulário de reserva  |
| `reserveConfirmation`      | Confirmação de reserva |
| `profile`                  | Perfil do usuário      |

## Arquitetura

O projeto segue uma arquitetura em camadas:

```
View (Composable)
    ↓
ViewModel (StateFlow)
    ↓
Repository (abstração de dados)
    ↓         ↓
  API       Room DB
(Retrofit)  (local)
```

Os repositórios isolam os ViewModels das fontes de dados concretas (API REST e banco local), facilitando testes e troca de implementação:

| Repositório            | Responsabilidade                                          |
| ---------------------- | --------------------------------------------------------- |
| `StoreRepository`      | Busca e filtragem de restaurantes via API                 |
| `MenuRepository`       | Carregamento de itens de cardápio por restaurante         |
| `ReservationRepository`| Criação de reservas via API                               |
| `UserRepository`       | Login, cadastro, logout e persistência local do usuário   |

## Equipe

Projeto desenvolvido para a disciplina de Desenvolvimento Mobile — DC/UFSCar.
