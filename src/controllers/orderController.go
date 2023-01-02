package controllers

import (
	"ambassador/src/database"
	"ambassador/src/models"
	"context"
	"fmt"
	"net/smtp"

	"github.com/gofiber/fiber/v2"
	"github.com/stripe/stripe-go/v74"
	"github.com/stripe/stripe-go/v74/checkout/session"
)

func Orders(c *fiber.Ctx) error {
	var orders []models.Order

	database.DB.Preload("OrderItems").Find(&orders)

	for i, order := range orders {
		orders[i].Name = order.FullName()
		orders[i].Total = order.GetTotal()
	}

	return c.JSON(orders)
}

type CreateOrderRequest struct {
	Code      string
	FirstName string
	LastName  string
	Email     string
	Address   string
	Country   string
	City      string
	Zip       string
	Products  []map[string]int
}

func CreateOrder(c *fiber.Ctx) error {
	var request CreateOrderRequest

	if err := c.BodyParser(&request); err != nil {
		return err
	}

	link := models.Link{
		Code: request.Code,
	}

	database.DB.Preload("user").First(&link)

	if link.Id == 0 {
		return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
			"message": "Invalid link",
		})
	}

	order := models.Order{
		Code:            link.Code,
		UserId:          link.UserId,
		AmbassadorEmail: link.User.Email,
		FirstName:       request.FirstName,
		LastName:        request.LastName,
		Email:           request.Email,
		Address:         request.Address,
		Country:         request.Country,
		City:            request.City,
		Zip:             request.Zip,
	}

	tx := database.DB.Begin()

	if err := tx.Create(&order).Error; err != nil {
		tx.Rollback()
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map{
			"message": err.Error(),
		})
	}

	var lineItems []*stripe.CheckoutSessionLineItemParams

	for _, requestProduct := range request.Products {
		product := models.Product{}
		product.Id = uint(requestProduct["product_id"])
		database.DB.First(&product)

		total := float64(requestProduct["quantity"]) * product.Price

		item := models.OrderItem{
			OrderId:           order.Id,
			ProductTitle:      product.Title,
			Price:             product.Price,
			Quantity:          uint(requestProduct["quantity"]),
			AmbassadorRevenue: total * 0.1,
			AdminRevenue:      total * 0.9,
		}

		if err := tx.Create(&item).Error; err != nil {
			tx.Rollback()
			return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
				"message": err.Error(),
			})
		}

		lineItems = append(lineItems, &stripe.CheckoutSessionLineItemParams{
			Quantity: stripe.Int64(int64(item.Quantity)),
		})
	}

	stripe.Key = ""

	params := &stripe.CheckoutSessionParams{
		SuccessURL:         stripe.String("http://localhost:5000/success?session_id={CHECKOUT_SESSION_ID}"),
		CancelURL:          stripe.String("http://localhost:5000/error"),
		PaymentMethodTypes: stripe.StringSlice([]string{"card"}),
		LineItems:          lineItems,
	}

	source, err := session.New(params)

	if err != nil {
		tx.Rollback()
		return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
			"message": err.Error(),
		})
	}

	order.TransactionId = source.ID

	if err := tx.Save(&order).Error; err != nil {
		tx.Rollback()
		return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
			"message": err.Error(),
		})
	}

	tx.Commit()

	return c.JSON(source)
}

func CompleteOrder(c *fiber.Ctx) error {
	var data map[string]string

	if err := c.BodyParser(&data); err != nil {
		return err
	}

	order := models.Order{}

	database.DB.Preload("OrderItems").First(&order, models.Order{
		TransactionId: data["source"],
	})

	if order.Id == 0 {
		return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
			"message": "Invalid order",
		})
	}

	order.Complete = true
	database.DB.Save(&order)

	go func(order models.Order) {
		ambassadorRevenue := 0.0
		adminRevenue := 0.0

		for _, item := range order.OrderItems {
			ambassadorRevenue += item.AmbassadorRevenue
			adminRevenue += item.AdminRevenue
		}

		user := models.User{}
		user.Id = order.UserId

		database.DB.First(&user)

		database.Cache.ZIncrBy(context.Background(), "rankings", ambassadorRevenue, user.Name())

		ambassadorMessage := []byte(fmt.Sprintf("You have earned $%f from the link #%s", ambassadorRevenue, order.Code))

		smtp.SendMail("host.docker.internal:1025", nil, "no-reply@email.com", []string{order.AmbassadorEmail}, ambassadorMessage)

		adminMessage := []byte(fmt.Sprintf("Order #%d with total of $%f has been completed", order.Id, adminRevenue))

		smtp.SendMail("host.docker.internal:1025", nil, "no-reply@email.com", []string{"admin@admin.com"}, adminMessage)
	}(order)

	return c.JSON(fiber.Map{
		"message": "Order completed",
	})
}
