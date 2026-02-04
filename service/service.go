package service

import (
	"github.com/tsingmuhe/artery-rpc/client"
	"github.com/tsingmuhe/artery-rpc/server"
)

type service struct {
	name string
	opts Options
}

func New(name string, opts ...Option) *service {
	return &service{
		name: name,
		opts: newOptions(opts...),
	}
}

func (s *service) Name() string {
	return s.name
}

func (s *service) Client() client.Client {
	return s.opts.Client
}

func (s *service) Server() server.Server {
	return s.opts.Server
}
