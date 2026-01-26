package client

import "github.com/tsingmuhe/artery-rpc/transport"

type Options struct {
	Transport transport.Transport
}

type Option func(*Options)

func newOptions(options ...Option) Options {
	opts := Options{
		Transport: transport.DefaultTransport,
	}

	for _, o := range options {
		o(&opts)
	}

	return opts
}
