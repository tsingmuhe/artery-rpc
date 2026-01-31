package server

import (
	"context"

	"github.com/tsingmuhe/artery-rpc/codec"
)

var (
	DefaultServer = newRPCServer()
)

type Server interface {
	Handler(handler interface{}) error
	Run() error
	Stop() error
}

type Router interface {
	Handle(Handler) error
	ServeRequest(context.Context, Request, Response) error
}

type Request interface {
	Service() string
	Method() string
	Endpoint() string
	ContentType() string
	Header() map[string]string
	Body() interface{}
	Read() ([]byte, error)
	Codec() codec.Reader
	Stream() bool
}

type Response interface {
	Codec() codec.Writer
	WriteHeader(map[string]string)
	Write([]byte) error
}
