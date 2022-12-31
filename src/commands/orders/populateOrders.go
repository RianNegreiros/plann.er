package main

import (
	"ambassador/src/database"
	"ambassador/src/models"
	"math/rand"

	"github.com/go-faker/faker/v4"
)

func main() {
	database.Connect()

	for i := 0; i < 10; i++ {
		var orderItems []models.OrderItem

		for j := 0; j < rand.Intn(5); j++ {
			price := float64(rand.Intn(90)) * 10
			quantity := uint(rand.Intn(10))

			orderItems = append(orderItems, models.OrderItem{
				ProductTitle:      faker.Word(),
				Price:             price,
				Quantity:          quantity,
				AdminRevenue:      price * 0.1 * float64(quantity),
				AmbassadorRevenue: price * 0.1 * float64(quantity),
			})
		}

		database.DB.Create(&models.Order{
			UserId:          uint(rand.Intn(10) + 1),
			Code:            faker.Paragraph(),
			AmbassadorEmail: faker.Email(),
			FirstName:       faker.FirstName(),
			LastName:        faker.LastName(),
			Email:           faker.Email(),
			Complete:        true,
			OrderItems:      orderItems,
		})
	}
}
