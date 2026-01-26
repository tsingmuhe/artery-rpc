package service

import (
	"github.com/tsingmuhe/artery-rpc/client"
	"github.com/tsingmuhe/artery-rpc/server"
)

type Options struct {
	Client client.Client
	Server server.Server
}

type Option func(*Options)

func newOptions(opts ...Option) Options {
	opt := Options{
		Client: client.DefaultClient,
		Server: server.DefaultServer,
	}

	for _, o := range opts {
		o(&opt)
	}

	return opt
}
