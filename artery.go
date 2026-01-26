package rpc

import (
	"github.com/tsingmuhe/artery-rpc/client"
	"github.com/tsingmuhe/artery-rpc/server"
	"github.com/tsingmuhe/artery-rpc/service"
)

type Service interface {
	Name() string

	Client() client.Client

	Server() server.Server
}

func NewService(name string, opts ...service.Option) Service {
	return service.New(name, opts...)
}
