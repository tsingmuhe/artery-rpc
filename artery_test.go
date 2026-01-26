package rpc_test

import (
	"context"
	"log"
	"testing"

	"github.com/tsingmuhe/artery-rpc"
)

type Request struct {
	Name string `json:"name"`
}

type Response struct {
	Message string `json:"message"`
}

type Say struct{}

func (h *Say) Hello(ctx context.Context, req *Request, rsp *Response) error {
	rsp.Message = "Hello " + req.Name
	return nil
}

func TestServiceServer(t *testing.T) {
	service := rpc.NewService("p.s.m")

	server := service.Server()
	if err := server.Handler(new(Say)); err != nil {
		log.Fatal(err)
	}

	if err := server.Run(); err != nil {
		log.Fatal(err)
	}
}

func TestServiceClient(t *testing.T) {
	service := rpc.NewService("p.s.m")

	_ = service.Client()
}
