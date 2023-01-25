FROM golang:1.19.4
WORKDIR /app/backend
COPY go.mod .
COPY go.sum .
RUN go mod download

COPY . .

RUN go install github.com/cosmtrek/air@latest

CMD ["air"]