package server

import "github.com/tsingmuhe/artery-rpc/transport"

type Options struct {
	Address   string
	Transport transport.Transport
}

type Option func(*Options)

func newOptions(opt ...Option) Options {
	opts := Options{
		Transport: transport.DefaultTransport,
	}

	for _, o := range opt {
		o(&opts)
	}

	return opts
}
