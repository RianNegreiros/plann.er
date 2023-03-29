# Ambassador App

## Application to ambassadors earn money sharing links

## [API](https://go-ambassador.onrender.com/api)

![Vercel](https://vercelbadge.vercel.app/api/riannegreiros/go-ambassador) <br />
[Ambassador live preview](https://go-ambassador.vercel.app/)

### Preview

![Application Preview 1](/clients/Documentation/imgs/Preview.png)
![Admin Dashboard Preview](/clients/Documentation/imgs/AdminPreview.png)

### Tecnologies and Tools

- [Golang](https://go.dev/)
- [PostgresQL](https://www.postgresql.org/)
- [ReactJs](https://reactjs.org/)
- [Next.js](https://nextjs.org/)
- [Redis](https://github.com/redis/go-redis)
- [GORM](https://gorm.io/)
- [Json Web Token](https://github.com/golang-jwt/jwt)
- [Stripe](https://github.com/stripe/stripe-go)
- [GoDotEnv](https://github.com/joho/godotenv)

## How to run

### Prerequisites

[Docker Compose](https://docs.docker.com/compose/gettingstarted/)
<hr/>

1. Create a ```.env``` file based on ```.env.example``` and fill the values according to PostgreSQL and Redis config on ```docker-compose.yml``` or with your production databases

2. Run ```docker-compose up```

3. Go to ```localhost:8000/api``` on your preferably browser to use the **API**. ```localhost:3000``` to access **admin dashboard**. ```localhost:3001``` to use the application as **ambassador**.
