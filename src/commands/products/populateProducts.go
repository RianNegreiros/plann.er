package main

import (
	"ambassador/src/database"
	"ambassador/src/models"
	"math/rand"

	"github.com/go-faker/faker/v4"
)

func main() {
	database.Connect()

	for i := 0; i < 30; i++ {
		product := models.Product{
			Title:       faker.Name(),
			Description: faker.Paragraph(),
			Image:       faker.URL(),
			Price:       float64(rand.Intn(90) * 10),
		}

		database.DB.Create(&product)
	}
}
