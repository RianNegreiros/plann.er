package database

import (
	"context"
	"fmt"
	"os"
	"time"

	"github.com/go-redis/redis/v9"
)

var Cache *redis.Client
var CacheChannel chan string

func SetupRedis() {
	addr := os.Getenv("REDIS_ADDRESS")

	opt, _ := redis.ParseURL(addr)
	Cache = redis.NewClient(opt)
}

func SetupCacheChannel() {
	CacheChannel = make(chan string)

	go func(ch chan string) {
		for {
			time.Sleep(5 * time.Second)

			key := <-ch

			Cache.Del(context.Background(), key)

			fmt.Println("Cache deleted" + key)
		}
	}(CacheChannel)
}

func ClearCache(keys ...string) {
	for _, key := range keys {
		CacheChannel <- key
	}
}
